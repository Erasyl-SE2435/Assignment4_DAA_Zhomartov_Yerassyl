package data;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
public class DatasetGenerator {
    private static final Random rnd = new Random(12345);

    public static void main(String[] args) throws Exception {
        new DatasetGenerator().generateAll();
    }

    public void generateAll() throws IOException {
        String[] sizes = {"small-1","small-2","small-3","medium-1","medium-2","medium-3","large-1","large-2","large-3"};
        int[] ns = {6,7,9,12,15,18,22,30,45};
        for (int i = 0; i < sizes.length; i++) {
            String name = "data/" + sizes[i] + ".json";
            generateFile(name, ns[i], densityFor(i), includeCyclesFor(i));
            System.out.println("Written " + name + " (N=" + ns[i] + ")");
        }
    }

    private double densityFor(int idx) {
        if (idx % 3 == 0) return 0.15;
        if (idx % 3 == 1) return 0.35;
        return 0.6;
    }

    private boolean includeCyclesFor(int idx) {
        return (idx % 3) != 2;
    }

    public void generateFile(String path, int n, double density, boolean withCycles) throws IOException {
        List<String> ids = new ArrayList<>();
        for (int i = 0; i < n; i++) ids.add("T" + (i+1));

        Map<String, Set<String>> outs = new LinkedHashMap<>();
        Map<String, Integer> durations = new LinkedHashMap<>();

        for (String id: ids) {
            outs.put(id, new LinkedHashSet<>());
            durations.put(id, 1 + rnd.nextInt(10));
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) if (i != j) {
                if (rnd.nextDouble() < density) outs.get(ids.get(i)).add(ids.get(j));
            }
        }
        if (withCycles) {
            for (int k = 0; k < Math.max(1, n/6); k++) {
                int a = rnd.nextInt(n), b = rnd.nextInt(n), c = rnd.nextInt(n);
                // Создание простого цикла A -> B -> C -> A
                if (a==b||b==c||a==c) continue;
                outs.get(ids.get(a)).add(ids.get(b));
                outs.get(ids.get(b)).add(ids.get(c));
                outs.get(ids.get(c)).add(ids.get(a));
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{\n  \"nodes\": [\n");
        boolean first = true;
        for (String id : ids) {
            if (!first) sb.append(",\n");
            first = false;
            sb.append("    { \"id\": \"").append(id).append("\", \"duration\": ").append(durations.get(id)).append(", \"outs\": [");
            boolean f2 = true;
            for (String to : outs.get(id)) {
                if (!f2) sb.append(", ");
                f2 = false;
                sb.append("\"").append(to).append("\"");
            }
            sb.append("] }");
        }
        sb.append("\n  ]\n}\n");
        try (FileWriter fw = new FileWriter(path)) {
            fw.write(sb.toString());
        }
    }
}