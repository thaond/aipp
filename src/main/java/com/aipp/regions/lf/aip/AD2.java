package com.aipp.regions.lf.aip;

import com.aipp.regions.lf.LFParser;
import com.aipp.downloader.Downloader;
import com.aipp.downloader.HttpDownloader;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;

/**
 * Parser for LF AD-2 (Aerodromes)
 * Parses aerodrome data from AIP documents
 */
public class AD2 extends LFParser {
    
    public AD2() {
        super("AIP", "AD-2");
    }
    
    @Override
    public void setup() {
        super.setup();
        // AD-2 specific setup
    }
    
    @Override
    public void parse() throws Exception {
        // Parse aerodrome data from multiple parts
        String[] parts = {"one", "two", "three"};
        
        for (String part : parts) {
            String documentName = String.format("%s.%s", this.section, part);
            Document html = read(documentName);
            
            // Extract aerodrome data from HTML
            Elements aerodromeRows = html.select("table tbody tr");
            
            for (Element row : aerodromeRows) {
                try {
                    String id = row.selectFirst("td:nth-child(1)").text();
                    String name = row.selectFirst("td:nth-child(2)").text();
                    String icao = row.selectFirst("td:nth-child(3)").text();
                    String elevation = row.selectFirst("td:nth-child(4)").text();
                    
                    Map<String, Object> aerodrome = new HashMap<>();
                    aerodrome.put("id", id);
                    aerodrome.put("name", name);
                    aerodrome.put("icao", icao);
                    aerodrome.put("elevation", elevation);
                    aerodrome.put("section", this.section);
                    
                    if (unique(aerodrome)) {
                        add(id, aerodrome);
                        verbose_info(String.format("Added aerodrome: %s (%s)", name, icao));
                    }
                } catch (Exception e) {
                    warn(String.format("Error parsing aerodrome row: %s", e.getMessage()));
                }
            }
        }
        
        info(String.format("AD-2: Parsed %d aerodromes", features.size()));
    }
    
    @Override
    public Downloader getOriginFor(String document) {
        // Return HTTP downloader for SIA data
        String url = String.format("https://www.sia.aviation-civile.gouv.fr/siadepart/textes/aip/AD-2/%s", document);
        return new HttpDownloader(url, "html");
    }
    
    private void verbose_info(String message) {
        if (com.aipp.AIPP.options().has("verbose")) {
            info(message);
        }
    }
}
