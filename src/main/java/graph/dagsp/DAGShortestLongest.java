package graph.dagsp;

import graph.util.GraphCondensation;
import graph.util.Metrics;
import java.util.*;

public class DAGShortestLongest {
    private final GraphCondensation dag;
    private final Metrics metrics;
    private final List<Integer> topoOrder;

    public DAGShortestLongest(GraphCondensation dag, Metrics metrics, List<Integer> topoOrder) {
        this.dag = dag;
        this.metrics = metrics;
        this.topoOrder = topoOrder;
    }

    public static class Result {
        public final long[] dist;
        public final int[] parent;

        public Result(long[] dist, int[] parent) {
            this.dist = dist;
            this.parent = parent;
        }
    }
    public Result shortestFrom(int src) {
        int n = dag.n();
        long INF = Long.MAX_VALUE / 4;
        long[] dist = new long[n];
        int[] parent = new int[n];
        Arrays.fill(dist, INF);
        Arrays.fill(parent, -1);
        List<Integer> order = this.topoOrder;

        dist[src] = dag.duration(src);
        for (int u : order) {
            if (dist[u] == INF) continue;

            for (int v : dag.neighbors(u)) {
                metrics.increment("relaxations");
                long nd = dist[u] + dag.duration(v);

                if (nd < dist[v]) {
                    dist[v] = nd;
                    parent[v] = u;
                }
            }
        }
        return new Result(dist, parent);
    }

    public Result longestOverall() {
        int n = dag.n();
        long NEG_INF = Long.MIN_VALUE / 4;
        long[] dist = new long[n];
        int[] parent = new int[n];
        Arrays.fill(dist, NEG_INF);
        Arrays.fill(parent, -1);
        List<Integer> order = this.topoOrder;
        boolean[] hasIncoming = new boolean[n];
        for (int u = 0; u < n; u++) for (int v : dag.neighbors(u)) hasIncoming[v] = true;

        for (int i = 0; i < n; i++) if (!hasIncoming[i]) dist[i] = dag.duration(i);

        for (int u : order) {
            if (dist[u] == NEG_INF) continue;

            for (int v : dag.neighbors(u)) {
                metrics.increment("relaxations");
                long nd = dist[u] + dag.duration(v);

                if (nd > dist[v]) {
                    dist[v] = nd;
                    parent[v] = u;
                }
            }
        }

        int best = -1;
        long bestVal = NEG_INF;
        for (int i = 0; i < n; i++) if (dist[i] > bestVal) {
            bestVal = dist[i];
            best = i;
        }

        return new Result(dist, parent);
    }

    public static List<Integer> reconstructPath(int target, int[] parent) {
        LinkedList<Integer> path = new LinkedList<>();
        int cur = target;
        while (cur != -1) {
            path.addFirst(cur);
            cur = parent[cur];
        }
        return path;
    }
}