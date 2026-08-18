package com.aipp.environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Container for static YAML fixture data.
 * Fixtures are read automatically from the fixtures/ subdirectory.
 */
public class Fixtures {
    
    private Map<String, Object> fixtures;
    private static final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
    
    public Fixtures() {
        this.fixtures = new LinkedHashMap<>();
    }
    
    /**
     * Add a fixture
     */
    public void add(String name, Object fixture) {
        fixtures.put(name, fixture);
    }
    
    /**
     * Get a fixture by name
     */
    public Object get(String name) {
        return fixtures.get(name);
    }
    
    /**
     * Check if fixture exists
     */
    public boolean has(String name) {
        return fixtures.containsKey(name);
    }
    
    /**
     * Get all fixtures
     */
    public Map<String, Object> getAll() {
        return fixtures;
    }
    
    /**
     * Load fixtures from YAML files in a directory
     */
    public void loadFromDirectory(String directory) throws Exception {
        File dir = new File(directory);
        if (!dir.isDirectory()) {
            throw new Exception("Fixtures directory not found: " + directory);
        }
        
        File[] yamlFiles = dir.listFiles((d, name) -> name.endsWith(".yml") || name.endsWith(".yaml"));
        if (yamlFiles != null) {
            for (File file : yamlFiles) {
                String fixtureName = file.getName().replaceAll("\\.ya?ml$", "");
                Object data = yamlMapper.readValue(file, Object.class);
                fixtures.put(fixtureName, data);
            }
        }
    }
    
    /**
     * Clear fixtures
     */
    public void clear() {
        fixtures.clear();
    }
}
