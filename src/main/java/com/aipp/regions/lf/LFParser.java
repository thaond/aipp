package com.aipp.regions.lf;

import com.aipp.regions.RegionParser;
import com.aipp.downloader.Downloader;
import org.jsoup.nodes.Document;

/**
 * Base parser for LF (France) region
 */
public abstract class LFParser extends RegionParser {
    
    public LFParser(String scope, String section) {
        super("LF", scope, section);
    }
    
    @Override
    public Downloader getOriginFor(String document) {
        // TODO: Implement LF-specific download logic
        return null;
    }
    
    @Override
    public void parse() throws Exception {
        // TODO: Implement LF parsing logic
    }
}
