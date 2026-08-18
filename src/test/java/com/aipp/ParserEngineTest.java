package com.aipp;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.aipp.aixm.AIXMDocument;
import com.aipp.ofmx.OFMXDocument;
import com.aipp.environment.Cache;
import com.aipp.environment.Options;

/**
 * Integration tests for Parser and output generation
 */
public class ParserEngineTest {
    
    private ParserEngine engine;
    
    @Before
    public void setUp() {
        AIPP.initializeDefault();
        engine = new ParserEngine("LF", "AIP", "aixm");
    }
    
    @Test
    public void testAIXMDocumentGeneration() throws Exception {
        AIXMDocument doc = new AIXMDocument();
        doc.addFeature("aerodrome-001", createTestAerodrome());
        
        String xml = doc.toXML();
        assertNotNull(xml);
        assertTrue(xml.contains("<?xml"));
        assertTrue(xml.contains("AIXMBasicMessage"));
        assertTrue(xml.contains("aerodrome-001"));
        assertEquals(1, doc.getFeatureCount());
    }
    
    @Test
    public void testOFMXDocumentGeneration() throws Exception {
        OFMXDocument doc = new OFMXDocument();
        doc.addFeature("airport-001", createTestAerodrome());
        
        String xml = doc.toXML();
        assertNotNull(xml);
        assertTrue(xml.contains("<?xml"));
        assertTrue(xml.contains("OpenFlightMaps"));
        assertTrue(xml.contains("airport-001"));
        assertEquals(1, doc.getFeatureCount());
    }
    
    @Test
    public void testParserEngineInitialization() {
        ParserEngine eng = new ParserEngine("LF", "AIP", "aixm");
        assertEquals("LF", eng.getRegion());
        assertEquals("AIP", eng.getScope());
    }
    
    @Test
    public void testCacheOperations() {
        Cache cache = AIPP.cache();
        cache.set("test_key", "test_value");
        assertEquals("test_value", cache.get("test_key"));
    }
    
    @Test
    public void testOptionsOperations() {
        Options options = AIPP.options();
        options.set("region", "LS");
        assertEquals("LS", options.get("region"));
    }
    
    private java.util.Map<String, Object> createTestAerodrome() {
        java.util.Map<String, Object> aerodrome = new java.util.HashMap<>();
        aerodrome.put("id", "LFLY");
        aerodrome.put("name", "Lyon");
        aerodrome.put("icao", "LFLY");
        aerodrome.put("elevation", "417");
        aerodrome.put("section", "AD-2");
        return aerodrome;
    }
}
