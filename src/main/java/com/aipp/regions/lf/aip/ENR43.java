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
 * Parser for LF ENR-4.3 (Navigation Aids)
 * Parses navigation aid data (VOR, NDB, etc.)
 */
public class ENR43 extends LFParser {
    
    public ENR43() {
        super("AIP", "ENR-4.3");
        dependsOn("ENR21", "ENR22");
    }
    
    @Override
    public void parse() throws Exception {
        Document html = read();
        
        // Extract navigation aid data
        Elements navaidsRows = html.select("table.navaids tbody tr");
        
        for (Element row : navaidsRows) {
            try {
                String id = row.selectFirst("td:nth-child(1)").text();
                String name = row.selectFirst("td:nth-child(2)").text();
                String type = row.selectFirst("td:nth-child(3)").text();
                String frequency = row.selectFirst("td:nth-child(4)").text();
                String latitude = row.selectFirst("td:nth-child(5)").text();
                String longitude = row.selectFirst("td:nth-child(6)").text();
                
                Map<String, Object> navaid = new HashMap<>();
                navaid.put("id", id);
                navaid.put("name", name);
                navaid.put("type", type);
                navaid.put("frequency", frequency);
                navaid.put("latitude", latitude);
                navaid.put("longitude", longitude);
                navaid.put("section", this.section);
                
                if (unique(navaid)) {
                    add(id, navaid);
                    verbose_info(String.format("Added NAVAID: %s (%s)", name, type));
                }
            } catch (Exception e) {
                warn(String.format("Error parsing navaid row: %s", e.getMessage()));
            }
        }
        
        info(String.format("ENR-4.3: Parsed %d navaids", features.size()));
    }
    
    @Override
    public Downloader getOriginFor(String document) {
        String url = String.format("https://www.sia.aviation-civile.gouv.fr/siadepart/textes/aip/ENR/ENR-4.3/%s", document);
        return new HttpDownloader(url, "html");
    }
    
    private void verbose_info(String message) {
        if (com.aipp.AIPP.options().has("verbose")) {
            info(message);
        }
    }
}
