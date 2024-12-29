package de.varilx.cache.caches;

import de.varilx.cache.AbstractCache;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.time.Duration;
import java.util.concurrent.*;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TimedCache<K, V> extends AbstractCache<K, V> {

    Duration cacheDuration;
    ScheduledExecutorService executorService;
    ConcurrentMap<K, ScheduledFuture<?>> tasks;

    public TimedCache(Duration cacheDuration) {
        this.cacheDuration = cacheDuration;
        this.tasks = new ConcurrentHashMap<>();
        this.executorService = Executors.newScheduledThreadPool(1);
    }

    @Override
    public void put(K key, V value) {
        if (tasks.containsKey(key)) {
            tasks.get(key).cancel(false);
        }
        super.put(key, value);
        ScheduledFuture<?> scheduledFuture = executorService.schedule(() -> remove(key), cacheDuration.toMillis(), TimeUnit.MILLISECONDS);
        tasks.put(key, scheduledFuture);
    }

    @Override
    public void remove(K key) {
        ScheduledFuture<?> task = tasks.remove(key);
        if(task != null) {
            task.cancel(false);
        }
    }
}
