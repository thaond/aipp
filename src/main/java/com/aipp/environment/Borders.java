package com.aipp.environment;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Container for border definitions (GeoJSON and coordinate-based).
 * Borders represent geographical boundaries for regions.
 */
public class Borders {
    
    private Map<String, Object> borders;
    
    public Borders() {
        this.borders = new LinkedHashMap<>();
    }
    
    /**
     * Add a border
     */
    public void add(String name, Object border) {
        borders.put(name, border);
    }
    
    /**
     * Get a border by name
     */
    public Object get(String name) {
        return borders.get(name);
    }
    
    /**
     * Check if border exists
     */
    public boolean has(String name) {
        return borders.containsKey(name);
    }
    
    /**
     * Get all borders
     */
    public Map<String, Object> getAll() {
        return borders;
    }
    
    /**
     * Load borders from GeoJSON files in a directory
     */
    public void loadFromGeoJSON(String directory) {
        // TODO: Implement GeoJSON loading
    }
    
    /**
     * Clear borders
     */
    public void clear() {
        borders.clear();
    }
}
