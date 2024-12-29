package de.varilx.cache.caches;

import de.varilx.cache.AbstractCache;

public class Cache<K, V> extends AbstractCache<K, V> {

    @Override
    public void remove(K key) {
        cache.remove(key);
    }

}
