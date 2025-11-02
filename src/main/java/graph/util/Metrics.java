package graph.util;

import java.util.HashMap;
import java.util.Map;

public class Metrics {
    private final Map<String, Long> counters = new HashMap<>();
    private long startTime = 0;
    private long endTime = 0;

    public void start() {
        startTime = System.nanoTime();
    }
    public void stop() {
        endTime = System.nanoTime();
    }
    public long elapsedNanos() {
        if (endTime == 0) return System.nanoTime() - startTime;
        return endTime - startTime;
    }
    public void increment(String key) {
        counters.put(key, counters.getOrDefault(key, 0L) + 1L);
    }

    public long get(String key) {
        return counters.getOrDefault(key, 0L);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  Time (ns): ").append(elapsedNanos()).append("\n");
        for (Map.Entry<String, Long> e : counters.entrySet()) {
            sb.append("  ").append(e.getKey()).append(": ").append(e.getValue()).append("\n");
        }
        return sb.toString();
    }
}