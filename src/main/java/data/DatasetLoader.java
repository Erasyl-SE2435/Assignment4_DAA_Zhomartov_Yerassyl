package data;

import graph.util.Graph;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class DatasetLoader {
    public static Graph loadFromJson(String path) throws IOException {
        String all = new String(Files.readAllBytes(Paths.get(path)));
        Graph g = new Graph();
        Pattern nodePat = Pattern.compile("\\{\\s*\"id\"\\s*:\\s*\"([^\"]+)\"\\s*,\\s*\"duration\"\\s*:\\s*(\\d+)\\s*,\\s*\"outs\"\\s*:\\s*\\[([^\\]]*)\\]\\s*\\}");
        Matcher m = nodePat.matcher(all);

        List<NodeTemp> nodes = new ArrayList<>();

        while (m.find()) {
            String id = m.group(1);
            int dur = Integer.parseInt(m.group(2));
            String outsRaw = m.group(3).trim();
            List<String> outs = new ArrayList<>();

            if (!outsRaw.isEmpty()) {
                Pattern q = Pattern.compile("\"([^\"]+)\"");
                Matcher mm = q.matcher(outsRaw);
                while (mm.find()) outs.add(mm.group(1));
            }
            nodes.add(new NodeTemp(id, dur, outs));
        }

        for (NodeTemp nt : nodes) g.addNode(nt.id, nt.duration);
        for (NodeTemp nt : nodes) {
            for (String to : nt.outs) {
                int fromIdx = g.indexOf(nt.id);
                int toIdx = g.indexOf(to);

                if (fromIdx >= 0 && toIdx >= 0) g.addEdge(fromIdx, toIdx);
            }
        }
        return g;
    }

    private static class NodeTemp {
        String id;
        int duration;
        List<String> outs;

        NodeTemp(String id, int duration, List<String> outs) {
            this.id = id;
            this.duration = duration;
            this.outs = outs;
        }
    }
}