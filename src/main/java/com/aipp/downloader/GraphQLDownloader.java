package com.aipp.downloader;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jsoup.nodes.Document;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;

/**
 * Downloader for GraphQL queries
 */
public class GraphQLDownloader extends Downloader {
    
    private String endpoint;
    private String query;
    private Map<String, Object> variables;
    private String authToken;
    private static final OkHttpClient httpClient = new OkHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();
    
    public GraphQLDownloader(String endpoint, String query, Map<String, Object> variables, String authToken) {
        super(endpoint + "-" + query.hashCode(), 3600000); // 1 hour default cache TTL
        this.endpoint = endpoint;
        this.query = query;
        this.variables = variables != null ? variables : new HashMap<>();
        this.authToken = authToken;
    }
    
    @Override
    public Document download() throws Exception {
        // Build GraphQL request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("query", query);
        requestBody.put("variables", variables);
        
        String jsonBody = mapper.writeValueAsString(requestBody);
        RequestBody body = RequestBody.create(jsonBody, okhttp3.MediaType.get("application/json"));
        
        Request.Builder requestBuilder = new Request.Builder()
            .url(endpoint)
            .post(body)
            .addHeader("Content-Type", "application/json");
        
        if (authToken != null && !authToken.isEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer " + authToken);
        }
        
        Request request = requestBuilder.build();
        
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new Exception("GraphQL error: " + response.code());
            }
            
            String responseBody = response.body() != null ? response.body().string() : "{}";
            // Return parsed response as Document-like structure
            return null; // TODO: Implement GraphQL response parsing
        }
    }
    
    public String getEndpoint() {
        return endpoint;
    }
    
    public String getQuery() {
        return query;
    }
    
    public Map<String, Object> getVariables() {
        return variables;
    }
}
