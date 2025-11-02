package graph.scc;

import graph.util.Graph;
import graph.util.GraphCondensation;

import java.util.*;
public class CondensationGraph {
    private final Graph g;
    private final List<List<Integer>> sccs;

    public CondensationGraph(Graph g, List<List<Integer>> sccs) {
        this.g = g;
        this.sccs = sccs;
    }
    public GraphCondensation build() {
        int compCount = sccs.size();
        GraphCondensation cond = new GraphCondensation(compCount);
        int[] compId = new int[g.n()];
        for (int i = 0; i < sccs.size(); i++) {
            for (int v : sccs.get(i)) compId[v] = i;
        }

        for (int u = 0; u < g.n(); u++) {
            int cu = compId[u];
            for (int v : g.neighbors(u)) {
                int cv = compId[v];
                if (cu != cv) {
                    cond.addEdge(cu, cv);
                }
            }
        }
        for (int i = 0; i < sccs.size(); i++) {
            int sum = 0;
            for (int v : sccs.get(i)) sum += g.duration(v);
            cond.setDuration(i, sum);
        }

        return cond;
    }
}