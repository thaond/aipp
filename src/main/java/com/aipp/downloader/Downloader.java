package com.aipp.downloader;

import org.jsoup.nodes.Document;

/**
 * Abstract base class for all downloaders.
 * Different implementations handle File, HTTP, GraphQL, Archive sources.
 */
public abstract class Downloader {
    
    protected String cacheKey;
    protected long cacheTTL; // Time to live in milliseconds
    
    public Downloader(String cacheKey, long cacheTTL) {
        this.cacheKey = cacheKey;
        this.cacheTTL = cacheTTL;
    }
    
    /**
     * Download and cache the document
     */
    public abstract Document download() throws Exception;
    
    /**
     * Get cache key
     */
    public String getCacheKey() {
        return cacheKey;
    }
    
    /**
     * Get cache TTL in milliseconds
     */
    public long getCacheTTL() {
        return cacheTTL;
    }
}
