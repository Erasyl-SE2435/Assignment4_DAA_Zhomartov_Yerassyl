package data;

import graph.util.Graph;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.FileWriter;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

public class DatasetLoaderTest {
    private static final String TEMP_JSON = "data/temp_test.json";

    @BeforeAll
    static void setup() throws IOException {
        String jsonContent = "{\n" +
                "  \"nodes\": [\n" +
                "    { \"id\": \"T1\", \"duration\": 10, \"outs\": [\"T2\", \"T3\"] },\n" +
                "    { \"id\": \"T2\", \"duration\": 5, \"outs\": [\"T4\"] },\n" +
                "    { \"id\": \"T3\", \"duration\": 2, \"outs\": [\"T4\"] },\n" +
                "    { \"id\": \"T4\", \"duration\": 1, \"outs\": [] }\n" +
                "  ]\n" +
                "}\n";

        try (FileWriter fw = new FileWriter(TEMP_JSON)) {
            fw.write(jsonContent);
        }
    }

    @Test
    void loadFromJson_ShouldParseGraphCorrectly() throws IOException {
        Graph g = DatasetLoader.loadFromJson(TEMP_JSON);

        assertEquals(4, g.n(), "Граф должен содержать 4 узла.");

        assertEquals(10, g.duration(g.indexOf("T1")), "Длительность T1 должна быть 10.");
        assertEquals(1, g.duration(g.indexOf("T4")), "Длительность T4 должна быть 1.");
        assertEquals(2, g.neighbors(g.indexOf("T1")).size(), "T1 должен иметь 2 исходящих ребра.");
        assertEquals(0, g.neighbors(g.indexOf("T4")).size(), "T4 не должен иметь исходящих ребер.");
        assertTrue(g.neighbors(g.indexOf("T1")).contains(g.indexOf("T2")), "Ребро T1->T2 должно существовать.");
    }
}