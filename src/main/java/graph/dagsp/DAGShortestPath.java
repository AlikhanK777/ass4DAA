package graph.dagsp;

import graph.Metrics;
import java.util.*;

public class DAGShortestPath {
    private Metrics metrics;

    public DAGShortestPath(Metrics metrics) {
        this.metrics = metrics;
    }

    public static class Graph {
        public int n;
        public List<List<Edge>> adjList;
        public int source;

        public Graph(int n, int source) {
            this.n = n;
            this.source = source;
            this.adjList = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                adjList.add(new ArrayList<>());
            }
        }

        public void addEdge(int u, int v, int weight) {
            adjList.get(u).add(new Edge(v, weight));
        }

        public void printGraph() {
            System.out.println("Graph (n=" + n + ", source=" + source + "):");
            for (int i = 0; i < n; i++) {
                System.out.println("  Node " + i + " -> " + adjList.get(i));
            }
        }
    }

    public static class Edge {
        public int v;
        public int weight;

        public Edge(int v, int weight) {
            this.v = v;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return String.format("(->%d, w:%d)", v, weight);
        }
    }

    public int[] shortestPath(Graph graph, List<Integer> topoOrder) {
        metrics.startTimer();

        int[] dist = new int[graph.n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[graph.source] = 0;

        for (int u : topoOrder) {
            if (dist[u] != Integer.MAX_VALUE) {
                for (Edge edge : graph.adjList.get(u)) {
                    metrics.relaxations++;
                    int newDist = dist[u] + edge.weight;
                    if (newDist < dist[edge.v]) {
                        dist[edge.v] = newDist;
                    }
                }
            }
        }

        metrics.stopTimer();
        return dist;
    }

    public int[] longestPath(Graph graph, List<Integer> topoOrder) {
        metrics.startTimer();

        int[] dist = new int[graph.n];
        Arrays.fill(dist, Integer.MIN_VALUE);
        dist[graph.source] = 0;

        for (int u : topoOrder) {
            if (dist[u] != Integer.MIN_VALUE) {
                for (Edge edge : graph.adjList.get(u)) {
                    metrics.relaxations++;
                    int newDist = dist[u] + edge.weight;
                    if (newDist > dist[edge.v]) {
                        dist[edge.v] = newDist;
                    }
                }
            }
        }

        metrics.stopTimer();
        return dist;
    }

    public List<Integer> reconstructPath(int[] dist, Graph graph, int target, List<Integer> topoOrder) {
        if (dist[target] == Integer.MAX_VALUE || dist[target] == Integer.MIN_VALUE) {
            return new ArrayList<>();
        }

        List<Integer> path = new ArrayList<>();
        path.add(target);
        int current = target;

        Map<Integer, Integer> position = new HashMap<>();
        for (int i = 0; i < topoOrder.size(); i++) {
            position.put(topoOrder.get(i), i);
        }

        while (current != graph.source) {
            boolean found = false;
            for (int u : topoOrder) {
                if (position.get(u) < position.get(current)) {
                    for (Edge edge : graph.adjList.get(u)) {
                        if (edge.v == current && dist[u] + edge.weight == dist[current]) {
                            path.add(0, u);
                            current = u;
                            found = true;
                            break;
                        }
                    }
                    if (found) break;
                }
            }
            if (!found) break;
        }

        return path;
    }

    public void printDistances(int[] dist, String type) {
        System.out.println(type + " distances from source:");
        for (int i = 0; i < dist.length; i++) {
            String value;
            if (dist[i] == Integer.MAX_VALUE) {
                value = "∞";
            } else if (dist[i] == Integer.MIN_VALUE) {
                value = "-∞";
            } else {
                value = String.valueOf(dist[i]);
            }
            System.out.println("  Node " + i + ": " + value);
        }
    }
}