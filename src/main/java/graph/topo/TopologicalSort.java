package graph.topo;

import graph.util.GraphCondensation;
import graph.util.Metrics;

import java.util.*;

public class TopologicalSort {
    private final GraphCondensation dag;
    private final Metrics metrics;

    public TopologicalSort(GraphCondensation dag, Metrics metrics) {
        this.dag = dag;
        this.metrics = metrics;
    }

    public List<Integer> kahn() {
        int n = dag.n();
        int[] indeg = new int[n];

        for (int u = 0; u < n; u++) {
            for (int v : dag.neighbors(u)) {
                indeg[v]++;
            }
        }
        Deque<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (indeg[i] == 0) {
                q.addLast(i);
                metrics.increment("kahnPushes");
            }
        }

        List<Integer> order = new ArrayList<>();
        while (!q.isEmpty()) {
            int u = q.removeFirst();
            metrics.increment("kahnPops");
            order.add(u);
            for (int v : dag.neighbors(u)) {
                indeg[v]--;
                if (indeg[v] == 0) {
                    q.addLast(v);
                    metrics.increment("kahnPushes");
                }
            }
        }
        if (order.size() != n) {
            throw new IllegalStateException("Graph is cyclic, cannot compute topological order.");
        }

        return order;
    }
}