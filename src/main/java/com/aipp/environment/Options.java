package com.aipp.environment;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Command-line options and arguments.
 * Behaves like OpenStruct - allows dynamic property access.
 */
public class Options {
    
    private Map<String, Object> options;
    
    public Options() {
        this.options = new LinkedHashMap<>();
    }
    
    /**
     * Set an option
     */
    public void set(String key, Object value) {
        options.put(key, value);
    }
    
    /**
     * Get an option
     */
    public Object get(String key) {
        return options.get(key);
    }
    
    /**
     * Get an option with default value
     */
    public Object get(String key, Object defaultValue) {
        return options.getOrDefault(key, defaultValue);
    }
    
    /**
     * Check if option exists
     */
    public boolean has(String key) {
        return options.containsKey(key);
    }
    
    /**
     * Get all options
     */
    public Map<String, Object> getAll() {
        return options;
    }
    
    /**
     * Parse command-line arguments
     */
    public void parseArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.startsWith("-")) {
                if (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                    options.put(arg.replaceFirst("^-+", ""), args[++i]);
                } else {
                    options.put(arg.replaceFirst("^-+", ""), true);
                }
            }
        }
    }
    
    /**
     * Clear options
     */
    public void clear() {
        options.clear();
    }
}
