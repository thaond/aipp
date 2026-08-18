package com.aipp.aixm;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * AIXM (Aeronautical Information Exchange Model) document generator
 * Converts parsed features to AIXM 5.1 XML format
 */
public class AIXMDocument {
    
    private String namespace = "http://www.aixm.aero/5.1.1";
    private String gmlNamespace = "http://www.opengis.net/gml/3.2.1";
    private String xsNamespace = "http://www.w3.org/2001/XMLSchema";
    private String gcoNamespace = "http://www.isotc211.org/2005/gco";
    
    private Map<String, Object> features;
    private String messageIdentifier;
    private String sender;
    private String origin;
    private String timeSlice;
    
    public AIXMDocument() {
        this.features = new LinkedHashMap<>();
        this.messageIdentifier = UUID.randomUUID().toString();
        this.sender = "AIPP Java";
        this.origin = "AIP";
        this.timeSlice = String.valueOf(System.currentTimeMillis() / 1000);
    }
    
    /**
     * Add a feature to the document
     */
    public void addFeature(String id, Object feature) {
        features.put(id, feature);
    }
    
    /**
     * Generate AIXM XML document
     */
    public String toXML() {
        StringBuilder xml = new StringBuilder();
        
        // XML Declaration
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        
        // AIXM Message root element
        xml.append(String.format(
            "<aixm:AIXMBasicMessage xmlns:aixm=\"%s\" "
            + "xmlns:gml=\"%s\" "
            + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
            + "xmlns:xsd=\"%s\" "
            + "xmlns:gco=\"%s\" "
            + "xsi:schemaLocation=\"http://www.aixm.aero/5.1.1 http://www.aixm.aero/schema/5.1.1/AIXM_Features.xsd\">\n",
            namespace, gmlNamespace, xsNamespace, gcoNamespace
        ));
        
        // Message metadata
        xml.append("  <aixm:hasMember>\n");
        xml.append(String.format("    <aixm:AIXMFeature gml:id=\"%s\">\n", messageIdentifier));
        xml.append(String.format("      <gml:identifier>%s</gml:identifier>\n", messageIdentifier));
        xml.append(String.format("      <gml:description>AIPP Conversion</gml:description>\n"));
        xml.append(String.format("      <gml:boundedBy><gml:Envelope/></gml:boundedBy>\n"));
        
        // Add features
        for (Map.Entry<String, Object> entry : features.entrySet()) {
            xml.append(convertFeatureToXML(entry.getKey(), entry.getValue()));
        }
        
        xml.append("    </aixm:AIXMFeature>\n");
        xml.append("  </aixm:hasMember>\n");
        xml.append("</aixm:AIXMBasicMessage>\n");
        
        return xml.toString();
    }
    
    /**
     * Convert a single feature to AIXM XML
     */
    private String convertFeatureToXML(String id, Object feature) {
        StringBuilder xml = new StringBuilder();
        
        if (feature instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) feature;
            
            // Detect feature type from section
            String section = (String) map.getOrDefault("section", "UNKNOWN");
            String type = detectFeatureType(section);
            
            xml.append(String.format("      <gml:featureMember>\n"));
            xml.append(String.format("        <aixm:%s gml:id=\"%s\">\n", type, id));
            
            // Add common properties
            for (Map.Entry<String, Object> prop : map.entrySet()) {
                if (!prop.getKey().equals("section")) {
                    xml.append(String.format(
                        "          <aixm:%s>%s</aixm:%s>\n",
                        prop.getKey(),
                        escapeXML(String.valueOf(prop.getValue())),
                        prop.getKey()
                    ));
                }
            }
            
            xml.append(String.format("        </aixm:%s>\n", type));
            xml.append(String.format("      </gml:featureMember>\n"));
        }
        
        return xml.toString();
    }
    
    /**
     * Detect AIXM feature type from AIP section
     */
    private String detectFeatureType(String section) {
        if (section.contains("AD")) {
            return "AirportHeliport";
        } else if (section.contains("ENR")) {
            return "NavigationAid";
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
