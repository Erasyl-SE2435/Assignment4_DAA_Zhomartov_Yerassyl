package graph.topo;

import graph.util.GraphCondensation;
import graph.util.Metrics;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TopologicalSortTest {

    private GraphCondensation buildSimpleDAG() {
        GraphCondensation dag = new GraphCondensation(4);
        dag.setDuration(0, 0); // A
        dag.setDuration(1, 0); // B
        dag.setDuration(2, 0); // C
        dag.setDuration(3, 0); // D

        dag.addEdge(0, 1); // A -> B
        dag.addEdge(0, 2); // A -> C
        dag.addEdge(1, 3); // B -> D
        dag.addEdge(2, 3); // C -> D
        return dag;
    }

    @Test
    void kahnSort_ShouldProduceValidOrder() {
        GraphCondensation dag = buildSimpleDAG();
        TopologicalSort topo = new TopologicalSort(dag, new Metrics());

        List<Integer> order = topo.kahn();

        assertEquals(4, order.size(), "Должно быть 4 элемента в порядке.");

        // Проверка базовых зависимостей:
        // 1. A (0) должен быть первым: A не зависит ни от кого
        assertEquals(0, order.get(0), "Узел A (0) должен быть первым.");

        // 2. D (3) должен быть последним: D зависит от B и C
        assertEquals(3, order.get(3), "Узел D (3) должен быть последним.");

        // 3. B (1) и C (2) должны идти после A и до D
        int indexB = order.indexOf(1);
        int indexC = order.indexOf(2);

        assertTrue(indexB > order.indexOf(0), "B должен идти после A.");
        assertTrue(indexC > order.indexOf(0), "C должен идти после A.");
        assertTrue(indexB < order.indexOf(3), "B должен идти до D.");
        assertTrue(indexC < order.indexOf(3), "C должен идти до D.");
    }
}