package com.aipp.regions.ls.notam;

import com.aipp.regions.ls.LSParser;
import com.aipp.downloader.Downloader;
import com.aipp.downloader.GraphQLDownloader;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Map;

/**
 * Parser for LS NOTAM (Notice to Airmen)
 * Fetches NOTAM data from GraphQL API
 */
public class NOTAM extends LSParser {
    
    public NOTAM() {
        super("NOTAM", "notam");
    }
    
    @Override
    public void parse() throws Exception {
        // Download NOTAM data from GraphQL endpoint
        Document response = read();
        
        // Parse NOTAM entries
        // TODO: Implement NOTAM parsing from GraphQL response
        
        info(String.format("NOTAM: Parsed %d NOTAMs", features.size()));
    }
    
    @Override
    public Downloader getOriginFor(String document) {
        String endpoint = System.getenv("NEWAY_API_URL");
        String authToken = System.getenv("NEWAY_API_AUTHORIZATION");
        
        if (endpoint == null || authToken == null) {
            throw new RuntimeException("NEWAY_API_URL and NEWAY_API_AUTHORIZATION environment variables required");
        }
        
        String query = "query ($country: String!, $series: [String!], $region: String, $start: Int, $end: Int) {" +
                       "  queryNOTAMs(filter: {country: $country, series: $series, region: $region, start: $start, end: $end}) {" +
                       "    id name notamRaw series region country area effectiveFrom validUntil" +
                       "  }" +
                       "}";
        
        Map<String, Object> variables = new HashMap<>();
        variables.put("country", "CHE");
        variables.put("region", "LS");
        variables.put("start", System.currentTimeMillis() / 1000);
        variables.put("end", (System.currentTimeMillis() / 1000) + 86400);
        
        return new GraphQLDownloader(endpoint, query, variables, authToken);
    }
}
