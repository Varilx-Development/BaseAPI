package de.varilx.cache;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public abstract class AbstractCache<K, V> {

    @Getter
    protected ConcurrentMap<K, V> cache;

    public AbstractCache() {
        this.cache = new ConcurrentHashMap<>();
    }

    public void put(K key, V value) {
        cache.put(key, value);
    }

    public V get(K key) {
        return cache.get(key);
    }

    public boolean contains(K key) {
        return cache.containsKey(key);
    }

    public boolean exists(K key) {
        return cache.containsKey(key);
    }

    public V replace(K key, V value) {
        remove(key);
        put(key, value);
        return cache.get(key);
    }

    public abstract void remove(K key);

}
