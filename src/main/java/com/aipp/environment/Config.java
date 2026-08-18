package com.aipp.environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Configuration management from config.yml files.
 * Stores settings like namespace UUID for OFMX creation.
 */
public class Config {
    
    private Map<String, Object> config;
    private static final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
    
    public Config() {
        this.config = new LinkedHashMap<>();
        // Initialize with defaults
        this.config.put("namespace_uuid", UUID.randomUUID().toString());
    }
    
    /**
     * Load configuration from YAML file
     */
    public void loadFromFile(String filePath) throws Exception {
        File file = new File(filePath);
        if (file.exists()) {
            Map<String, Object> loaded = yamlMapper.readValue(file, Map.class);
            config.putAll(loaded);
        }
    }
    
    /**
     * Save configuration to YAML file
     */
    public void saveToFile(String filePath) throws Exception {
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        yamlMapper.writeValue(file, config);
    }
    
    /**
     * Set a configuration value
     */
    public void set(String key, Object value) {
        config.put(key, value);
    }
    
    /**
     * Get a configuration value
     */
    public Object get(String key) {
        return config.get(key);
    }
    
    /**
     * Get a configuration value with default
     */
    public Object get(String key, Object defaultValue) {
        return config.getOrDefault(key, defaultValue);
    }
    
    /**
     * Check if configuration key exists
     */
    public boolean has(String key) {
        return config.containsKey(key);
    }
    
    /**
     * Get all configuration
     */
    public Map<String, Object> getAll() {
        return config;
    }
    
    /**
     * Clear configuration
     */
    public void clear() {
        config.clear();
    }
}
