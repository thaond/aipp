package com.aipp.environment;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Cache for storing transient objects across parsers and runs.
 * Behaves like OpenStruct - allows dynamic property access.
 */
public class Cache {
    
    private Map<String, Object> data;
    
    public Cache() {
        this.data = new LinkedHashMap<>();
    }
    
    /**
     * Get a cached value
     */
    public Object get(String key) {
        return data.get(key);
    }
    
    /**
     * Set a cached value
     */
    public void set(String key, Object value) {
        data.put(key, value);
    }
    
    /**
     * Check if key exists
     */
    public boolean has(String key) {
        return data.containsKey(key);
    }
    
    /**
     * Clear the cache
     */
    public void clear() {
        data.clear();
    }
    
    /**
     * Get all cached data
     */
    public Map<String, Object> getData() {
        return data;
    }
}
