package com.aipp.regions.ls;

import com.aipp.regions.RegionParser;
import com.aipp.downloader.Downloader;
import org.jsoup.nodes.Document;

/**
 * Base parser for LS (Switzerland) region
 */
public abstract class LSParser extends RegionParser {
    
    public LSParser(String scope, String section) {
        super("LS", scope, section);
    }
    
    @Override
    public Downloader getOriginFor(String document) {
        // TODO: Implement LS-specific download logic
        return null;
    }
    
    @Override
    public void parse() throws Exception {
        // TODO: Implement LS parsing logic
    }
}
