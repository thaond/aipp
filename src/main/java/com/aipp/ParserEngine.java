package com.aipp;

import com.aipp.aixm.AIXMDocument;
import com.aipp.ofmx.OFMXDocument;
import com.aipp.parser.Parser;
import com.aipp.regions.lf.aip.AD2;
import com.aipp.regions.lf.aip.ENR43;
import com.aipp.regions.ls.notam.NOTAM;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * ParserEngine orchestrates region, scope, and section parsing.
 * Manages dependencies and output generation.
 */
public class ParserEngine {
    
    private static final Logger logger = LogManager.getLogger(ParserEngine.class);
    
    private String region;
    private String scope;
    private String section;
    private Map<String, Parser> parsers;
    private Path storageDirectory;
    private String outputFormat; // "aixm" or "ofmx"
    
    public ParserEngine(String region, String scope, String outputFormat) {
        this.region = region;
        this.scope = scope != null ? scope : "AIP";
        this.outputFormat = outputFormat != null ? outputFormat : "aixm";
        this.parsers = new LinkedHashMap<>();
        this.storageDirectory = AIPP.getStorageDirectory();
    }
    
    /**
     * Register a parser
     */
    public void registerParser(Parser parser) {
        String key = String.format("%s-%s-%s", parser.getRegion(), parser.getScope(), parser.getSection());
        parsers.put(key, parser);
    }
    
    /**
     * Load region-specific parsers
     */
    public void loadParsers() throws Exception {
        if ("LF".equals(region)) {
            if ("AIP".equals(scope)) {
                registerParser(new AD2());
                registerParser(new ENR43());
            }
        } else if ("LS".equals(region)) {
            if ("NOTAM".equals(scope)) {
                registerParser(new NOTAM());
            }
        }
    }
    
    /**
     * Execute parsing and generate output
     */
    public void execute() throws Exception {
        logger.info("Starting parse: region={}, scope={}, format={}", region, scope, outputFormat);
        
        loadParsers();
        
        if (parsers.isEmpty()) {
            throw new Exception(String.format("No parsers found for region %s, scope %s", region, scope));
        }
        
        // Create output document
        Object document = outputFormat.equalsIgnoreCase("ofmx") 
            ? new OFMXDocument() 
            : new AIXMDocument();
        
        // Execute parsers with dependency resolution
        Set<String> executed = new HashSet<>();
        Queue<String> queue = new LinkedList<>(parsers.keySet());
        
        while (!queue.isEmpty()) {
            String key = queue.poll();
            Parser parser = parsers.get(key);
            
            // Check dependencies
            boolean depsReady = true;
            for (String dep : parser.getDependencies()) {
                String depKey = String.format("%s-%s-%s", parser.getRegion(), parser.getScope(), dep);
                if (!executed.contains(depKey)) {
                    depsReady = false;
                    queue.offer(key); // Re-queue if dependencies not met
                    break;
                }
            }
            
            if (depsReady) {
                try {
                    logger.info("Executing parser: {}", key);
                    parser.setup();
                    parser.parse();
                    
                    // Add features to document
                    for (Map.Entry<String, Object> entry : parser.getFeatures().entrySet()) {
                        if (document instanceof AIXMDocument) {
                            ((AIXMDocument) document).addFeature(entry.getKey(), entry.getValue());
                        } else if (document instanceof OFMXDocument) {
                            ((OFMXDocument) document).addFeature(entry.getKey(), entry.getValue());
                        }
                    }
                    
                    executed.add(key);
                    logger.info("Completed parser: {} ({} features)", key, parser.getFeatures().size());
                } catch (Exception e) {
                    logger.error("Parser failed: {}", key, e);
                    if (AIPP.options().has("debug-on-error")) {
                        throw e;
                    }
                }
            }
        }
        
        // Generate output file
        String outputFilename = generateOutputFilename();
        String outputPath = generateOutputXML(document, outputFilename);
        
        logger.info("Parse complete! Output: {}", outputPath);
    }
    
    /**
     * Generate output XML and save to file
     */
    private String generateOutputXML(Object document, String filename) throws Exception {
        String xml;
        if (document instanceof AIXMDocument) {
            xml = ((AIXMDocument) document).toXML();
        } else if (document instanceof OFMXDocument) {
            xml = ((OFMXDocument) document).toXML();
        } else {
            throw new Exception("Unknown document type");
        }
        
        // Save to current directory
        File outputFile = new File(filename);
        try (FileWriter writer = new FileWriter(outputFile)) {
            writer.write(xml);
        }
        
        // Also save to storage
        Path storagePath = storageDirectory.resolve(region).resolve(scope).resolve("builds");
        storagePath.toFile().mkdirs();
        File storageFile = storagePath.resolve(filename).toFile();
        try (FileWriter writer = new FileWriter(storageFile)) {
            writer.write(xml);
        }
        
        return outputFile.getAbsolutePath();
    }
    
    /**
     * Generate output filename with timestamp
     */
    private String generateOutputFilename() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        String extension = outputFormat.equalsIgnoreCase("ofmx") ? "ofmx" : "xml";
        return String.format("%s-%s-%s.%s", region, scope, timestamp, extension);
    }
    
    public String getRegion() {
        return region;
    }
    
    public String getScope() {
        return scope;
    }
    
    public int getTotalFeatures() {
        return parsers.values().stream()
            .mapToInt(p -> p.getFeatures().size())
            .sum();
    }
}
