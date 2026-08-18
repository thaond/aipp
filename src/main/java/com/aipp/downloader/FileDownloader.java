package com.aipp.downloader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * Downloader for local files
 */
public class FileDownloader extends Downloader {
    
    private String filePath;
    private String fileType; // e.g., "pdf", "xlsx", "html"
    private String archivePath; // Optional archive to extract from
    
    public FileDownloader(String filePath) {
        this(filePath, null, null);
    }
    
    public FileDownloader(String filePath, String fileType) {
        this(filePath, fileType, null);
    }
    
    public FileDownloader(String filePath, String fileType, String archivePath) {
        super(filePath, 0); // No cache TTL for files
        this.filePath = filePath;
        this.fileType = fileType;
        this.archivePath = archivePath;
    }
    
    @Override
    public Document download() throws Exception {
        File file = new File(filePath);
        
        if (!file.exists()) {
            throw new Exception("File not found: " + filePath);
        }
        
        // Parse as HTML/XML
        Document doc = Jsoup.parse(file, StandardCharsets.UTF_8.name());
        return doc;
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public String getFileType() {
        return fileType;
    }
    
    public String getArchivePath() {
        return archivePath;
    }
}
