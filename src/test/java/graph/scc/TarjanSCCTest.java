package graph.scc;

import graph.util.Graph;
import graph.util.Metrics;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TarjanSCCTest {

    @Test
    void graphWithOneCycleTest() {
        Graph g = new Graph();
        g.addNode("A", 1);
        g.addNode("B", 1);
        g.addNode("C", 1);
        g.addEdge("A", "B");
        g.addEdge("B", "A");
        g.addEdge("B", "C");

        Metrics m = new Metrics();
        TarjanSCC t = new TarjanSCC(g, m);
        List<List<Integer>> comps = t.run();

        assertEquals(2, comps.size(), "Должно быть ровно 2 SCC.");
        assertTrue(comps.stream().anyMatch(c -> c.size() == 2), "Должен быть один цикл размера 2.");
    }


    @Test
    void pureDAGTest() {
        Graph g = new Graph();
        g.addNode("X", 5); // 0
        g.addNode("Y", 5); // 1
        g.addNode("Z", 5); // 2
        g.addNode("W", 5); // 3

        g.addEdge("X", "Y");
        g.addEdge("X", "Z");
        g.addEdge("Z", "W");

        Metrics m = new Metrics();
        TarjanSCC t = new TarjanSCC(g, m);
        List<List<Integer>> comps = t.run();

        // Проверка: Должно быть N SCC, все размера 1
        assertEquals(4, comps.size(), "В DAG количество SCC должно быть равно N.");
        assertTrue(comps.stream().allMatch(c -> c.size() == 1), "Все SCC должны быть размера 1.");
    }
}