package graph.topo;

import graph.Metrics;
import java.util.*;

public class TopologicalSort {
    private Metrics metrics;

    public TopologicalSort(Metrics metrics) {
        this.metrics = metrics;
    }

    public List<Integer> kahnTopoSort(List<List<Integer>> graph) {
        metrics.startTimer();

        int n = graph.size();
        int[] inDegree = new int[n];

        for (int u = 0; u < n; u++) {
            for (int v : graph.get(u)) {
                inDegree[v]++;
            }
        }

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
                metrics.kahnPushes++;
            }
        }

        List<Integer> topoOrder = new ArrayList<>();
        while (!queue.isEmpty()) {
            int u = queue.poll();
            metrics.kahnPops++;
            topoOrder.add(u);

            for (int v : graph.get(u)) {
                inDegree[v]--;
                if (inDegree[v] == 0) {
                    queue.offer(v);
                    metrics.kahnPushes++;
                }
            }
        }

        metrics.stopTimer();
        return topoOrder;
    }

    public void printTopoOrder(List<Integer> order) {
        System.out.println("Topological Order: " + order);
    }

    public List<Integer> getOriginalNodeOrder(List<List<Integer>> components, List<Integer> compOrder) {
        List<Integer> result = new ArrayList<>();
        for (int compId : compOrder) {
            result.addAll(components.get(compId));
        }
        return result;
    }
}