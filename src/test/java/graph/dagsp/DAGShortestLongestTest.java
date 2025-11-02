package graph.dagsp;

import graph.util.GraphCondensation;
import graph.util.Metrics;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class DAGShortestLongestTest {

    private GraphCondensation buildSimpleDAG() {
        GraphCondensation dag = new GraphCondensation(4);
        dag.setDuration(0, 10); // A (Source)
        dag.setDuration(1, 2);  // B
        dag.setDuration(2, 3);  // C
        dag.setDuration(3, 1);  // D (Sink)

        dag.addEdge(0, 1); // A -> B
        dag.addEdge(0, 2); // A -> C
        dag.addEdge(1, 3); // B -> D
        dag.addEdge(2, 3); // C -> D
        return dag;
    }

    private List<Integer> createMockOrder(int n) {
        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            order.add(i);
        }
        return order;
    }

    @Test
    void longestPathTest() {
        GraphCondensation dag = buildSimpleDAG();
        List<Integer> mockOrder = createMockOrder(dag.n());

        DAGShortestLongest sp = new DAGShortestLongest(dag, new Metrics(), mockOrder);

        DAGShortestLongest.Result r = sp.longestOverall();
        long maxDist = Arrays.stream(r.dist).max().getAsLong();

        assertEquals(14, maxDist, "Длиннейший путь должен быть 14 (A->C->D).");

        List<Integer> path = DAGShortestLongest.reconstructPath(3, r.parent);
        assertEquals(Arrays.asList(0, 2, 3), path, "Критический путь должен быть A->C->D.");
    }

    @Test
    void shortestPathTest() {
        GraphCondensation dag = buildSimpleDAG();
        List<Integer> mockOrder = createMockOrder(dag.n());
        DAGShortestLongest sp = new DAGShortestLongest(dag, new Metrics(), mockOrder);
        DAGShortestLongest.Result r = sp.shortestFrom(0);
        assertEquals(13, r.dist[3], "Кратчайшее расстояние до D должно быть 13 (A->B->D).");
        List<Integer> path = DAGShortestLongest.reconstructPath(3, r.parent);
        assertEquals(Arrays.asList(0, 1, 3), path, "Кратчайший путь должен быть A->B->D.");
    }
}