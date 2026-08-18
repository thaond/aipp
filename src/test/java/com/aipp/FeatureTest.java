package com.aipp;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Feature test suite for key functionality
 */
public class FeatureTest {
    
    @Test
    public void testXMLEscaping() throws Exception {
        AIPP.initializeDefault();
        
        com.aipp.aixm.AIXMDocument doc = new com.aipp.aixm.AIXMDocument();
        java.util.Map<String, Object> feature = new java.util.HashMap<>();
        feature.put("id", "TEST&123");
        feature.put("name", "Test <Name>");
        feature.put("section", "AD-2");
        
        doc.addFeature("TEST&123", feature);
        String xml = doc.toXML();
        
        assertTrue(xml.contains("&amp;"));
        assertTrue(xml.contains("&lt;"));
        assertTrue(xml.contains("&gt;"));
    }
    
    @Test
    public void testMultipleFeatureOutput() throws Exception {
        AIPP.initializeDefault();
        
        com.aipp.aixm.AIXMDocument doc = new com.aipp.aixm.AIXMDocument();
        
        for (int i = 1; i <= 5; i++) {
            java.util.Map<String, Object> feature = new java.util.HashMap<>();
            feature.put("id", "AERODROME-" + i);
            feature.put("name", "Test Airport " + i);
            feature.put("section", "AD-2");
            doc.addFeature("AERODROME-" + i, feature);
        }
        
        assertEquals(5, doc.getFeatureCount());
        String xml = doc.toXML();
        assertTrue(xml.contains("AERODROME-1"));
        assertTrue(xml.contains("AERODROME-5"));
    }
    
    @Test
    public void testOFMXOutputFormat() throws Exception {
        AIPP.initializeDefault();
        
        com.aipp.ofmx.OFMXDocument doc = new com.aipp.ofmx.OFMXDocument();
        java.util.Map<String, Object> feature = new java.util.HashMap<>();
        feature.put("id", "LFLY");
        feature.put("name", "Lyon");
        feature.put("section", "AD-2");
        
        doc.addFeature("LFLY", feature);
        String xml = doc.toXML();
        
        assertTrue(xml.contains("OpenFlightMaps"));
        assertTrue(xml.contains("metadata"));
        assertTrue(xml.contains("namespaceUUID"));
    }
    
    @Test
    public void testParserDependencyResolution() throws Exception {
        AIPP.initializeDefault();
        ParserEngine engine = new ParserEngine("LF", "AIP", "aixm");
        engine.loadParsers();
        // Should have loaded parsers without errors
        assertNotNull(engine);
    }
}
