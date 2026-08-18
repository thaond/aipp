package com.aipp;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Basic test suite for Parser functionality
 */
public class ParserTest {
    
    @Before
    public void setUp() {
        AIPP.initializeDefault();
    }
    
    @Test
    public void testAIPPInitialization() {
        assertNotNull(AIPP.cache());
        assertNotNull(AIPP.borders());
        assertNotNull(AIPP.fixtures());
        assertNotNull(AIPP.options());
        assertNotNull(AIPP.config());
    }
    
    @Test
    public void testCacheOperations() {
        AIPP.cache().set("test_key", "test_value");
        assertEquals("test_value", AIPP.cache().get("test_key"));
        assertTrue(AIPP.cache().has("test_key"));
    }
    
    @Test
    public void testOptionsOperations() {
        AIPP.options().set("region", "LF");
        assertEquals("LF", AIPP.options().get("region"));
        assertTrue(AIPP.options().has("region"));
    }
}
