package de.maxhenkel.easyvillagers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class CachedMap<K, V> {

    private final Map<K, ValueWrapper> cache;
    private final long lifespan;
    private long lastCheck;

    public CachedMap(long lifespan) {
        this.lifespan = lifespan;
        this.cache = new HashMap<>();
    }

    public V get(K key, Supplier<V> valueSupplier) {
        V value;
        if (cache.containsKey(key)) {
            value = cache.get(key).getValue();
        } else {
            value = valueSupplier.get();
            cache.put(key, new ValueWrapper(value));
        }
        cleanup();
        return value;
    }

    private void cleanup() {
        long time = System.currentTimeMillis();
        if (time - lastCheck > lifespan) {
            Collection<K> collect = cache.entrySet().stream().filter(kValueWrapperEntry -> kValueWrapperEntry.getValue().checkInvalid(time)).map(Map.Entry::getKey).collect(Collectors.toSet());
            cache.keySet().removeAll(collect);
            lastCheck = time;
        }
    }

    private class ValueWrapper {

        private V value;
        private long accessTimestamp;

        public ValueWrapper(V value) {
            this.value = value;
            this.accessTimestamp = System.currentTimeMillis();
        }

        public boolean checkInvalid(long currentTime) {
            return currentTime - accessTimestamp > lifespan;
        }

        public V getValue() {
            accessTimestamp = System.currentTimeMillis();
            return value;
        }

    }

}
