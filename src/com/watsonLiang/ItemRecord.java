package com.watsonLiang;

import java.util.HashMap;

class ItemRecord {
    HashMap<String, String> attrs;
    public ItemRecord(String[] dataAttrs, String[] dataVals){
        attrs = new HashMap<>();
        for(int i = 0; i < dataAttrs.length; i++){
            attrs.put(dataAttrs[i], dataVals[i]);
        }
    }
};