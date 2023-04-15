package ru.otus.cachehw;


import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

@Slf4j
public class MyCache<K, V> implements HwCache<K, V> {
//Надо реализовать эти методы
    private final List<HwListener<K, V>> listeners = new ArrayList<>();

    private final Map<K, V> cache = new WeakHashMap<>();

    @Override
    public void put(K key, V value) {
        log.info("put in cache: {}", value);
        cache.put(key, value);
    }

    @Override
    public void remove(K key) {
        var removed = cache.remove(key);
        if (removed != null) {
            log.info("removed from cache: {}", key);
        }
    }

    @Override
    public V get(K key) {
        var cached = cache.get(key);
        if (cached != null) {
            log.info("got from cache: {}", key);
        }
        return cached;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }
}
