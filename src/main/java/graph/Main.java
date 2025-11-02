package graph;

import graph.scc.SCC;
import graph.topo.TopologicalSort;
import graph.dagsp.DAGShortestPath;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Smart City Scheduling - Graph Algorithms ===\n");

        TestGraphData data = createTestData();
        System.out.println("Test Graph: " + data);

        Metrics metrics = new Metrics();

        // 1. Strongly Connected Components
        System.out.println("\n--- 1. Strongly Connected Components ---");
        SCC sccFinder = new SCC(data.n, data.graph, metrics);
        List<List<Integer>> components = sccFinder.findSCCs();
        sccFinder.printComponents();
        System.out.println("SCC " + metrics);
        metrics.reset();

        // 2. Condensation graph and topological sort
        System.out.println("\n--- 2. Condensation Graph & Topological Sort ---");
        List<List<Integer>> condensation = sccFinder.buildCondensationGraph();
        System.out.println("Condensation graph has " + condensation.size() + " nodes");

        TopologicalSort topo = new TopologicalSort(metrics);
        List<Integer> compTopoOrder = topo.kahnTopoSort(condensation);
        topo.printTopoOrder(compTopoOrder);
        System.out.println("Topo Sort " + metrics);
        metrics.reset();

        List<Integer> originalTopoOrder = topo.getOriginalNodeOrder(components, compTopoOrder);
        System.out.println("Original nodes order: " + originalTopoOrder);

        // 3. Shortest and Longest Paths in DAG
        System.out.println("\n--- 3. Shortest/Longest Paths in DAG ---");
        DAGShortestPath.Graph weightedGraph = buildWeightedGraph(data);
        weightedGraph.printGraph();

        DAGShortestPath sp = new DAGShortestPath(metrics);

        int[] shortestDist = sp.shortestPath(weightedGraph, originalTopoOrder);
        sp.printDistances(shortestDist, "Shortest");

        metrics.reset();
        int[] longestDist = sp.longestPath(weightedGraph, originalTopoOrder);
        sp.printDistances(longestDist, "Longest");

        findCriticalPath(longestDist, weightedGraph, originalTopoOrder, sp);

        System.out.println("Shortest Path " + metrics);
    }

    private static TestGraphData createTestData() {
        TestGraphData data = new TestGraphData();
        data.n = 8;
        data.source = 4;

        data.graph = new ArrayList<>();
        for (int i = 0; i < data.n; i++) {
            data.graph.add(new ArrayList<>());
        }

        data.addEdge(0, 1);
        data.addEdge(1, 2);
        data.addEdge(2, 3);
        data.addEdge(3, 1);
        data.addEdge(4, 5);
        data.addEdge(5, 6);
        data.addEdge(6, 7);

        data.weightedEdges = new ArrayList<>();
        data.addWeightedEdge(0, 1, 3);
        data.addWeightedEdge(1, 2, 2);
        data.addWeightedEdge(2, 3, 4);
        data.addWeightedEdge(3, 1, 1);
        data.addWeightedEdge(4, 5, 2);
        data.addWeightedEdge(5, 6, 5);
        data.addWeightedEdge(6, 7, 1);

        return data;
    }

    private static DAGShortestPath.Graph buildWeightedGraph(TestGraphData data) {
        DAGShortestPath.Graph graph = new DAGShortestPath.Graph(data.n, data.source);
        for (TestGraphData.WeightedEdge edge : data.weightedEdges) {
            graph.addEdge(edge.u, edge.v, edge.w);
        }
        return graph;
    }

    private static void findCriticalPath(int[] longestDist, DAGShortestPath.Graph graph,
                                         List<Integer> topoOrder, DAGShortestPath sp) {
        int maxDist = Integer.MIN_VALUE;
        int criticalNode = -1;

        for (int i = 0; i < graph.n; i++) {
            if (longestDist[i] > maxDist && longestDist[i] != Integer.MIN_VALUE) {
                maxDist = longestDist[i];
                criticalNode = i;
            }
        }

        if (criticalNode != -1) {
            List<Integer> criticalPath = sp.reconstructPath(longestDist, graph, criticalNode, topoOrder);
            System.out.println("\nCritical Path (Longest): " + criticalPath + " (length: " + maxDist + ")");
        } else {
            System.out.println("\nNo critical path found from source " + graph.source);
        }
    }

    static class TestGraphData {
        public int n;
        public int source;
        public List<List<Integer>> graph;
        public List<WeightedEdge> weightedEdges;

        public void addEdge(int u, int v) {
            graph.get(u).add(v);
        }

        public void addWeightedEdge(int u, int v, int w) {
            weightedEdges.add(new WeightedEdge(u, v, w));
        }

        @Override
        public String toString() {
            return String.format("Graph[n=%d, edges=%d, source=%d]", n, graph.stream().mapToInt(List::size).sum(), source);
        }

        static class WeightedEdge {
            public int u, v, w;
            public WeightedEdge(int u, int v, int w) {
                this.u = u;
                this.v = v;
                this.w = w;
            }
        }
    }
}