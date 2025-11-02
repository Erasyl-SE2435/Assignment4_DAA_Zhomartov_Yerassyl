package graph.scc;

import graph.util.Graph;
import graph.util.Metrics;

import java.util.*;

public class TarjanSCC {
    private final Graph g;
    private final Metrics metrics;
    private int[] index;
    private int[] low;
    private boolean[] onStack;
    private Deque<Integer> stack;
    private List<List<Integer>> sccs;
    private int currentIndex;

    public TarjanSCC(Graph g, Metrics metrics) {
        this.g = g;
        this.metrics = metrics;
    }

    public List<List<Integer>> run() {
        int n = g.n();
        index = new int[n];
        Arrays.fill(index, -1);
        low = new int[n];
        onStack = new boolean[n];
        stack = new ArrayDeque<>();
        sccs = new ArrayList<>();
        currentIndex = 0;

        for (int v = 0; v < n; v++) {
            if (index[v] == -1) {
                dfs(v);
            }
        }
        return sccs;
    }

    private void dfs(int v) {
        index[v] = currentIndex;
        low[v] = currentIndex;
        currentIndex++;
        stack.push(v);
        onStack[v] = true;
        metrics.increment("dfsVisits");

        for (int to : g.neighbors(v)) {
            metrics.increment("dfsEdges");
            if (index[to] == -1) {
                dfs(to);
                low[v] = Math.min(low[v], low[to]);
            } else if (onStack[to]) {
                low[v] = Math.min(low[v], index[to]);
            }
        }
        if (low[v] == index[v]) {
            List<Integer> comp = new ArrayList<>();
            while (true) {
                int w = stack.pop();
                onStack[w] = false;
                comp.add(w);
                if (w == v) break;
            }
            sccs.add(comp);
        }
    }
}