package knemognition.heartauth.orchestrator.interfaces.internal.app.impl;

import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A generic registry for temporarily awaiting async replies (e.g. Kafka responses).
 *
 * @param <K> the key type (e.g. UUID, String)
 * @param <V> the reply type
 */
@Component
public class WaitRegistry<K, V> {

    private final ConcurrentMap<K, CompletableFuture<V>> waits = new ConcurrentHashMap<>();

    /**
     * Registers a new future for a given key (idempotent).
     * If one already exists, returns the existing one.
     */
    public CompletableFuture<V> register(K key) {
        var future = new CompletableFuture<V>();
        var existing = waits.putIfAbsent(key, future);
        return existing != null ? existing : future;
    }

    /**
     * Completes and removes the waiting future.
     */
    public void complete(K key, V value) {
        var f = waits.remove(key);
        if (f != null) f.complete(value);
    }

    /**
     * Completes exceptionally and removes the waiting future.
     */
    public void completeExceptionally(K key, Throwable t) {
        var f = waits.remove(key);
        if (f != null) f.completeExceptionally(t);
    }

    /**
     * Optionally: remove a stale entry manually (e.g. on timeout cleanup).
     */
    public void remove(K key) {
        waits.remove(key);
    }

    /**
     * Optionally: check if a key is currently waiting.
     */
    public boolean isWaiting(K key) {
        return waits.containsKey(key);
    }
}
