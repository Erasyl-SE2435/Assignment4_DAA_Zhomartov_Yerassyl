package graph.util;

import java.util.*;
public class GraphCondensation {
    private final List<Set<Integer>> adjSet;
    private final int n;
    private final int[] durations;
    public GraphCondensation(int n) {
        this.n = n;
        this.adjSet = new ArrayList<>();
        this.durations = new int[n];
        for (int i = 0; i < n; i++) {
            adjSet.add(new LinkedHashSet<>());
        }
    }

    public void addEdge(int u, int v) {
        if (u == v) return;
        adjSet.get(u).add(v);
    }
    public void setDuration(int node, int d) {
        durations[node] = d;
    }
    public List<Integer> neighbors(int u) {

        return new ArrayList<>(adjSet.get(u));
    }

    public int n() {
        return n;
    }

    public int duration(int u) {
        return durations[u];
    }
}