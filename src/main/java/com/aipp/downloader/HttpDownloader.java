package com.aipp.downloader;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.util.HashMap;
import java.util.Map;

/**
 * Downloader for HTTP/HTTPS URLs
 */
public class HttpDownloader extends Downloader {
    
    private String url;
    private String fileType; // e.g., "pdf", "xlsx", "html"
    private String archivePath; // Optional archive to extract from
    private Map<String, String> headers; // Optional HTTP headers
    private static final OkHttpClient httpClient = new OkHttpClient();
    
    public HttpDownloader(String url) {
        this(url, null, null, null);
    }
    
    public HttpDownloader(String url, String fileType) {
        this(url, fileType, null, null);
    }
    
    public HttpDownloader(String url, String fileType, String archivePath, Map<String, String> headers) {
        super(url, 3600000); // 1 hour default cache TTL
        this.url = url;
        this.fileType = fileType;
        this.archivePath = archivePath;
        this.headers = headers != null ? headers : new HashMap<>();
    }
    
    @Override
    public Document download() throws Exception {
        Request.Builder requestBuilder = new Request.Builder()
            .url(url);
        
        // Add headers if provided
        for (Map.Entry<String, String> header : headers.entrySet()) {
            requestBuilder.addHeader(header.getKey(), header.getValue());
        }
        
        Request request = requestBuilder.build();
        
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new Exception("HTTP error: " + response.code());
            }
            
            String body = response.body() != null ? response.body().string() : "";
            Document doc = Jsoup.parse(body, url);
            return doc;
        }
    }
    
    public String getUrl() {
        return url;
    }
    
    public String getFileType() {
        return fileType;
    }
    
    public String getArchivePath() {
        return archivePath;
    }
    
    public Map<String, String> getHeaders() {
        return headers;
    }
}
