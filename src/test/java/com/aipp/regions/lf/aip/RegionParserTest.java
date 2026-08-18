package com.aipp.regions.lf.aip;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.aipp.AIPP;

/**
 * Feature tests for region-specific parsers
 */
public class RegionParserTest {
    
    @Before
    public void setUp() {
        AIPP.initializeDefault();
    }
    
    @Test
    public void testAD2ParserInitialization() {
        AD2 parser = new AD2();
        assertEquals("LF", parser.getRegion());
        assertEquals("AIP", parser.getScope());
        assertEquals("AD-2", parser.getSection());
    }
    
    @Test
    public void testAD2DependenciesResolution() {
        AD2 parser = new AD2();
        // AD-2 doesn't depend on other sections
        assertTrue(parser.getDependencies().isEmpty());
    }
    
    @Test
    public void testENR43ParserInitialization() {
        ENR43 parser = new ENR43();
        assertEquals("LF", parser.getRegion());
        assertEquals("AIP", parser.getScope());
        assertEquals("ENR-4.3", parser.getSection());
    }
    
    @Test
    public void testENR43Dependencies() {
        ENR43 parser = new ENR43();
        // ENR-4.3 depends on ENR-2.1 and ENR-2.2
        assertEquals(2, parser.getDependencies().size());
        assertTrue(parser.getDependencies().contains("ENR21"));
        assertTrue(parser.getDependencies().contains("ENR22"));
    }
    
    @Test
    public void testParserFeatureAddition() {
        AD2 parser = new AD2();
        java.util.Map<String, Object> aerodrome = new java.util.HashMap<>();
        aerodrome.put("id", "TEST001");
        aerodrome.put("name", "Test Aerodrome");
        
        parser.add("TEST001", aerodrome);
        assertTrue(parser.getFeatures().containsKey("TEST001"));
        assertEquals(1, parser.getFeatures().size());
    }
}
