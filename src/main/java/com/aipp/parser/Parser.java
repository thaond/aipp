package com.aipp.parser;

import com.aipp.AIPP;
import com.aipp.downloader.Downloader;
import com.aipp.environment.Cache;
import com.aipp.environment.Borders;
import com.aipp.environment.Fixtures;
import org.jsoup.nodes.Document;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Base parser class for all AIP, NOTAM, and SHOOT parsers.
 * Provides common functionality for downloading, parsing, and building features.
 */
public abstract class Parser {
    protected static final Logger logger = LogManager.getLogger(Parser.class);
    
    protected String region;
    protected String scope;
    protected String section;
    protected List<String> dependencies;
    protected Map<String, Object> features;
    
    /**
     * Constructor
     */
    public Parser(String region, String scope, String section) {
        this.region = region;
        this.scope = scope;
        this.section = section;
        this.dependencies = new ArrayList<>();
        this.features = new LinkedHashMap<>();
    }
    
    /**
     * Main parse method - must be implemented by subclasses
     */
    public abstract void parse() throws Exception;
    
    /**
     * Return the origin/downloader for a document
     */
    public abstract Downloader getOriginFor(String document);
    
    /**
     * Optional setup method called when parser is instantiated
     */
    public void setup() {
        // Override in subclasses if needed
    }
    
    /**
     * Read a document from source (download, cache, and parse)
     */
    public Document read() throws Exception {
        return read(this.section);
    }
    
    /**
     * Read a document with a non-standard name
     */
    public Document read(String documentName) throws Exception {
        Downloader downloader = getOriginFor(documentName);
        return downloader.download();
    }
    
    /**
     * Add a feature to the document
     */
    public void add(String key, Object feature) {
        features.put(key, feature);
    }
    
    /**
     * Find previously written features by object
     */
    public Object find(Class<?> type, String id) {
        for (Object feature : features.values()) {
            if (feature.getClass().equals(type)) {
                // Match by ID - depends on feature implementation
                return feature;
            }
        }
        return null;
    }
    
    /**
     * Find previously written features by class and attribute
     */
    public List<Object> findBy(Class<?> type, String attributeName, Object attributeValue) {
        List<Object> results = new ArrayList<>();
        for (Object feature : features.values()) {
            if (feature.getClass().equals(type)) {
                // Match by attribute - depends on feature implementation
                results.add(feature);
            }
        }
        return results;
    }
    
    /**
     * Prevent duplicate features
     */
    public boolean unique(Object feature) {
        // Implementation depends on feature identity
        return !features.containsValue(feature);
    }
    
    /**
     * Inline condition for assignments
     */
    public <T> T given(boolean condition, T value) {
        return condition ? value : null;
    }
    
    /**
     * Declare dependencies on other parsers
     */
    public void dependsOn(String... parserNames) {
        this.dependencies.addAll(Arrays.asList(parserNames));
    }
    
    /**
     * Access to cache
     */
    protected Cache cache() {
        return AIPP.cache();
    }
    
    /**
     * Access to borders
     */
    protected Borders borders() {
        return AIPP.borders();
    }
    
    /**
     * Access to fixtures
     */
    protected Fixtures fixtures() {
        return AIPP.fixtures();
    }
    
    /**
     * Log info message
     */
    protected void info(String message) {
        logger.info(message);
    }
    
    /**
     * Log warning message
     */
    protected void warn(String message) {
        logger.warn(message);
    }
    
    /**
     * Log error/fail message
     */
    protected void fail(String message) {
        logger.error(message);
        throw new RuntimeException(message);
    }
    
    // Getters
    public String getRegion() {
        return region;
    }
    
    public String getScope() {
        return scope;
    }
    
    public String getSection() {
        return section;
    }
    
    public List<String> getDependencies() {
        return dependencies;
    }
    
    public Map<String, Object> getFeatures() {
        return features;
    }
}
