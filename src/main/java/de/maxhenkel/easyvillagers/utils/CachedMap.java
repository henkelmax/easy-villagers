package de.maxhenkel.easyvillagers.utils;

import java.util.LinkedHashMap;
import java.util.Map;

public class CachedMap<K, V> extends LinkedHashMap<K, V> {
    private static final long serialVersionUID = 1L;


    private final long maxSize;

    public CachedMap(long maxSize) {
        super(16, 0.75f, true);
        this.maxSize = maxSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxSize;
    }

}
