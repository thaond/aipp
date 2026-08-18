package com.aipp.cli;

import com.aipp.AIPP;
import com.aipp.environment.Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * AIP to OFMX converter executable
 * Converts aeronautical information publications to OFMX format
 */
public class Aip2Ofmx {
    
    private static final Logger logger = LogManager.getLogger(Aip2Ofmx.class);
    
    public static void main(String[] args) {
        try {
            // Initialize AIPP
            AIPP.initializeDefault();
            
            // Parse command-line arguments
            Options options = AIPP.options();
            options.parseArgs(args);
            
            // Show help if requested
            if (options.has("help") || options.has("h")) {
                showHelp();
                return;
            }
            
            // Show list if requested
            if (options.has("list")) {
                showList();
                return;
            }
            
            // Extract options
            String region = (String) options.get("region", (String) options.get("r"));
            String scope = (String) options.get("scope", (String) options.get("s", "AIP"));
            String section = (String) options.get("section", null);
            String storageDir = (String) options.get("storage", (String) options.get("S"));
            boolean verbose = options.has("verbose") || options.has("v");
            boolean debugOnError = options.has("debug-on-error");
            
            if (region == null || region.isEmpty()) {
                logger.error("Region is required. Use -r or --region");
                showHelp();
                System.exit(1);
            }
            
            // TODO: Implement conversion logic
            logger.info("Converting AIP to OFMX for region: {}", region);
            logger.info("Scope: {}", scope);
            if (section != null) {
                logger.info("Section: {}", section);
            }
            
        } catch (Exception e) {
            logger.error("Error during conversion", e);
            System.exit(1);
        }
    }
    
    private static void showHelp() {
        System.out.println("Usage: aip2ofmx [options]");
        System.out.println();
        System.out.println("Options:");
        System.out.println("  -r, --region REGION       Aeronautical region (e.g., LF, LS)");
        System.out.println("  -s, --scope SCOPE         Scope: AIP (default), NOTAM, or SHOOT");
        System.out.println("  -S, --section SECTION     Specific section to parse");
        System.out.println("  --storage DIR             Storage directory (default: ~/.aipp)");
        System.out.println("  -l, --list                List available regions and sections");
        System.out.println("  -v, --verbose             Verbose output");
        System.out.println("  --debug-on-error          Open debugger on error");
        System.out.println("  -h, --help                Show this help message");
    }
    
    private static void showList() {
        System.out.println("Available regions:");
        System.out.println("  LF - France Mainland");
        System.out.println("  LS - Switzerland");
        System.out.println();
        System.out.println("Available scopes:");
        System.out.println("  AIP - Aeronautical Information Publication");
        System.out.println("  NOTAM - Notice to Airmen");
        System.out.println("  SHOOT - Firing ranges");
    }
}
