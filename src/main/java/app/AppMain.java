package app;

import data.DatasetLoader;
import graph.scc.TarjanSCC;
import graph.scc.CondensationGraph;
import graph.topo.TopologicalSort;
import graph.dagsp.DAGShortestLongest;
import graph.util.Graph;
import graph.util.GraphCondensation;
import graph.util.Metrics;

import java.util.List;

public class AppMain {
    public static void main(String[] args) throws Exception {
        // Загрузка данных
        String path = (args.length > 0) ? args[0] : "data/small-1.json";
        System.out.println("--- Smart City Scheduling Analysis ---");
        System.out.println("Loading: " + path);

        Graph g = DatasetLoader.loadFromJson(path);
        System.out.println("Loaded Graph: N=" + g.n());
        System.out.println("Graph (N=" + g.n() + "):");
        System.out.println(g); // Используем toString() неявно


        Metrics m1 = new Metrics();
        m1.start();
        TarjanSCC tarjan = new TarjanSCC(g, m1);
        List<List<Integer>> sccs = tarjan.run();
        m1.stop();

        System.out.println("\n## 1. SCC Analysis (Tarjan)");
        System.out.println("SCC count: " + sccs.size());
        for (int i = 0; i < sccs.size(); i++) {
            List<Integer> comp = sccs.get(i);

            System.out.print("Comp " + i + " (size=" + comp.size() + "): ");
            for (int v : comp) System.out.print(g.id(v) + " ");
            System.out.println();
        }
        System.out.println("Metrics:\n" + m1);


        CondensationGraph builder = new CondensationGraph(g, sccs);
        GraphCondensation dag = builder.build();
        System.out.println("Condensation Graph (DAG) built: N=" + dag.n() + " components.");


        Metrics m2 = new Metrics();
        TopologicalSort topo = new TopologicalSort(dag, m2);
        m2.start();
        List<Integer> order = topo.kahn(); // <-- ПОЛУЧАЕМ ПОРЯДОК ЗДЕСЬ
        m2.stop();

        System.out.println("\n## 2. Topological Sort (Kahn)");
        System.out.println("Order of components: " + order);
        System.out.println("Metrics:\n" + m2);


        Metrics m3 = new Metrics();
        DAGShortestLongest spShortest = new DAGShortestLongest(dag, m3, order);
        m3.start();
        DAGShortestLongest.Result res = spShortest.shortestFrom(0);
        m3.stop();
        System.out.println("\n## 3. Shortest Paths from Comp 0");
        System.out.println("Distances (min task sum):");
        for (int i = 0; i < res.dist.length; i++) {
            String status = (res.dist[i] > Long.MAX_VALUE / 5) ? "UNREACHABLE" : String.valueOf(res.dist[i]);
            System.out.println("  Comp " + i + ": " + status);
        }
        System.out.println("Metrics:\n" + m3);

        Metrics m4 = new Metrics();
        DAGShortestLongest spLongest = new DAGShortestLongest(dag, m4, order);
        m4.start();
        DAGShortestLongest.Result r2 = spLongest.longestOverall();
        m4.stop();

        long bestVal = Long.MIN_VALUE;
        int bestIdx = -1;
        for (int i = 0; i < r2.dist.length; i++) {
            if (r2.dist[i] > bestVal) { bestVal = r2.dist[i]; bestIdx = i; }
        }

        System.out.println("\n## 4. Longest Path (Critical Path)");
        System.out.println("**Critical Path Length (Min Project Time):** " + bestVal);

        List<Integer> criticalCompPath = DAGShortestLongest.reconstructPath(bestIdx, r2.parent);
        System.out.println("Reconstructed Comp-Path: " + criticalCompPath);
        System.out.println("Metrics:\n" + m4);
        System.out.println("------------------------------------------");
    }
}