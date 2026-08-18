package com.aipp.ofmx;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * OFMX (Open FlightMaps eXchange) document generator
 * Converts parsed features to OFMX XML format
 */
public class OFMXDocument {
    
    private String namespace = "http://openflightmaps.org/ofmx/0.1";
    private String gmlNamespace = "http://www.opengis.net/gml/3.2.1";
    
    private Map<String, Object> features;
    private String documentId;
    private String creator;
    private String creationTime;
    private String namespaceUUID;
    
    public OFMXDocument() {
        this.features = new LinkedHashMap<>();
        this.documentId = UUID.randomUUID().toString();
        this.creator = "AIPP Java";
        this.creationTime = String.valueOf(System.currentTimeMillis() / 1000);
        this.namespaceUUID = UUID.randomUUID().toString();
    }
    
    /**
     * Add a feature to the document
     */
    public void addFeature(String id, Object feature) {
        features.put(id, feature);
    }
    
    /**
     * Generate OFMX XML document
     */
    public String toXML() {
        StringBuilder xml = new StringBuilder();
        
        // XML Declaration
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        
        // OFMX root element
        xml.append(String.format(
            "<ofmx:OpenFlightMaps xmlns:ofmx=\"%s\" "
            + "xmlns:gml=\"%s\" "
            + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
            + "xsi:schemaLocation=\"%s\">\n",
            namespace, gmlNamespace, namespace
        ));
        
        // Document metadata
        xml.append("  <ofmx:metadata>\n");
        xml.append(String.format("    <ofmx:id>%s</ofmx:id>\n", documentId));
        xml.append(String.format("    <ofmx:creator>%s</ofmx:creator>\n", creator));
        xml.append(String.format("    <ofmx:created>%s</ofmx:created>\n", creationTime));
        xml.append(String.format("    <ofmx:namespaceUUID>%s</ofmx:namespaceUUID>\n", namespaceUUID));
        xml.append("  </ofmx:metadata>\n");
        
        // Add features
        xml.append("  <ofmx:features>\n");
        for (Map.Entry<String, Object> entry : features.entrySet()) {
            xml.append(convertFeatureToXML(entry.getKey(), entry.getValue()));
        }
        xml.append("  </ofmx:features>\n");
        
        xml.append("</ofmx:OpenFlightMaps>\n");
        
        return xml.toString();
    }
    
    /**
     * Convert a single feature to OFMX XML
     */
    private String convertFeatureToXML(String id, Object feature) {
        StringBuilder xml = new StringBuilder();
        
        if (feature instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) feature;
            
            String section = (String) map.getOrDefault("section", "UNKNOWN");
            String type = detectFeatureType(section);
            
            xml.append(String.format("    <ofmx:%s gml:id=\"%s\">\n", type, id));
            
            // Add properties
            for (Map.Entry<String, Object> prop : map.entrySet()) {
                if (!prop.getKey().equals("section")) {
                    xml.append(String.format(
                        "      <ofmx:%s>%s</ofmx:%s>\n",
                        prop.getKey(),
                        escapeXML(String.valueOf(prop.getValue())),
                        prop.getKey()
                    ));
                }
            }
            
            xml.append(String.format("    </ofmx:%s>\n", type));
        }
        
        return xml.toString();
    }
    
    /**
     * Detect OFMX feature type from AIP section
     */
    private String detectFeatureType(String section) {
        if (section.contains("AD")) {
            return "Airport";
        } else if (section.contains("ENR")) {
            return "Navaid";
        } else if (section.contains("AIRSPACE")) {
            return "Airspace";
        }
        return "Feature";
    }
    
    /**
     * Escape XML special characters
     */
    private String escapeXML(String text) {
        return text
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&apos;");
    }
    
    public Map<String, Object> getFeatures() {
        return features;
    }
    
    public int getFeatureCount() {
        return features.size();
    }
}
