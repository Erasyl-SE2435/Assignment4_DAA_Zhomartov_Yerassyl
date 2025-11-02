package graph.util;

import java.util.*;


public class Graph {
    private final List<List<Integer>> adj = new ArrayList<>();
    private final Map<String, Integer> idToIndex = new LinkedHashMap<>();
    private final List<String> indexToId = new ArrayList<>();
    private final List<Integer> durations = new ArrayList<>();


    public int addNode(String id, int duration) {
        if (idToIndex.containsKey(id)) return idToIndex.get(id);
        int idx = indexToId.size();
        idToIndex.put(id, idx);
        indexToId.add(id);
        durations.add(duration);
        adj.add(new ArrayList<>());
        return idx;
    }

    public void addEdge(String fromId, String toId) {
        Integer u = idToIndex.get(fromId);
        Integer v = idToIndex.get(toId);
        if (u == null || v == null) throw new IllegalArgumentException("Unknown node id in edge: " + fromId + "->" + toId);
        adj.get(u).add(v);
    }

    public void addEdge(int u, int v) {
        adj.get(u).add(v);
    }

    public int n() { return adj.size(); }
    public List<Integer> neighbors(int u) { return Collections.unmodifiableList(adj.get(u)); }
    public String id(int index) { return indexToId.get(index); }
    public int indexOf(String id) { return idToIndex.getOrDefault(id, -1); }
    public int duration(int index) { return durations.get(index); }
    public List<String> ids() { return Collections.unmodifiableList(indexToId); }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Graph (N=" + n() + "):\n");
        for(int i=0; i<n(); i++){
            sb.append(i).append(" (").append(id(i)).append(", dur=").append(duration(i)).append(") -> ");
            for(int v : neighbors(i)) {
                sb.append(id(v)).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}