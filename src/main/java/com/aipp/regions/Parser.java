package com.aipp.regions;

import com.aipp.parser.Parser;

/**
 * Base class for region-specific parsers
 */
public abstract class RegionParser extends Parser {
    
    protected String regionCode;
    
    public RegionParser(String regionCode, String scope, String section) {
        super(regionCode, scope, section);
        this.regionCode = regionCode;
    }
    
    /**
     * Get the region code (e.g., "LF", "LS")
     */
    public String getRegionCode() {
        return regionCode;
    }
}
