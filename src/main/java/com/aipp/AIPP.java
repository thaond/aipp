package com.aipp;

import com.aipp.environment.Cache;
import com.aipp.environment.Borders;
import com.aipp.environment.Fixtures;
import com.aipp.environment.Options;
import com.aipp.environment.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Main AIPP environment and context holder.
 * Provides access to runtime environment objects like cache, borders, fixtures, options, and config.
 */
public class AIPP {
    private static final Logger logger = LogManager.getLogger(AIPP.class);
    
    private static Cache cache;
    private static Borders borders;
    private static Fixtures fixtures;
    private static Options options;
    private static Config config;
    private static Path storageDirectory;
    
    /**
     * Initialize AIPP environment
     */
    public static void initialize(Path storage) {
        storageDirectory = storage;
        cache = new Cache();
        borders = new Borders();
        fixtures = new Fixtures();
        options = new Options();
        config = new Config();
        logger.info("AIPP initialized with storage: {}", storageDirectory);
    }
    
    /**
     * Initialize with default storage location (~/.aipp)
     */
    public static void initializeDefault() {
        Path defaultStorage = Paths.get(System.getProperty("user.home"), ".aipp");
        initialize(defaultStorage);
    }
    
    public static Cache getCache() {
        return cache;
    }
    
    public static Borders getBorders() {
        return borders;
    }
    
    public static Fixtures getFixtures() {
        return fixtures;
    }
    
    public static Options getOptions() {
        return options;
    }
    
    public static Config getConfig() {
        return config;
    }
    
    public static Path getStorageDirectory() {
        return storageDirectory;
    }
    
    /**
     * Static accessor for cache (convenience method)
     */
    public static Cache cache() {
        return cache;
    }
    
    /**
     * Static accessor for borders (convenience method)
     */
    public static Borders borders() {
        return borders;
    }
    
    /**
     * Static accessor for fixtures (convenience method)
     */
    public static Fixtures fixtures() {
        return fixtures;
    }
    
    /**
     * Static accessor for options (convenience method)
     */
    public static Options options() {
        return options;
    }
    
    /**
     * Static accessor for config (convenience method)
     */
    public static Config config() {
        return config;
    }
}
