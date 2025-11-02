package graph.scc;

import graph.Metrics;
import java.util.*;

public class SCC {
    private int n;
    private List<List<Integer>> graph;
    private int index;
    private int[] indices;
    private int[] lowlinks;
    private boolean[] onStack;
    private Stack<Integer> stack;
    private List<List<Integer>> components;
    private Metrics metrics;

    public SCC(int n, List<List<Integer>> graph, Metrics metrics) {
        this.n = n;
        this.graph = graph;
        this.metrics = metrics;
        this.indices = new int[n];
        this.lowlinks = new int[n];
        this.onStack = new boolean[n];
        Arrays.fill(indices, -1);
        this.stack = new Stack<>();
        this.components = new ArrayList<>();
        this.index = 0;
    }

    public List<List<Integer>> findSCCs() {
        metrics.startTimer();
        for (int i = 0; i < n; i++) {
            if (indices[i] == -1) {
                strongConnect(i);
            }
        }
        metrics.stopTimer();
        return components;
    }

    private void strongConnect(int v) {
        metrics.dfsVisits++;
        indices[v] = index;
        lowlinks[v] = index;
        index++;
        stack.push(v);
        onStack[v] = true;

        for (int w : graph.get(v)) {
            metrics.dfsEdges++;
            if (indices[w] == -1) {
                strongConnect(w);
                lowlinks[v] = Math.min(lowlinks[v], lowlinks[w]);
            } else if (onStack[w]) {
                lowlinks[v] = Math.min(lowlinks[v], indices[w]);
            }
        }

        if (lowlinks[v] == indices[v]) {
            List<Integer> component = new ArrayList<>();
            int w;
            do {
                w = stack.pop();
                onStack[w] = false;
                component.add(w);
            } while (w != v);
            components.add(component);
        }
    }

    public List<List<Integer>> buildCondensationGraph() {
        List<List<Integer>> condensation = new ArrayList<>();
        for (int i = 0; i < components.size(); i++) {
            condensation.add(new ArrayList<>());
        }

        int[] componentId = new int[n];
        for (int i = 0; i < components.size(); i++) {
            for (int node : components.get(i)) {
                componentId[node] = i;
            }
        }

        for (int u = 0; u < n; u++) {
            for (int v : graph.get(u)) {
                int compU = componentId[u];
                int compV = componentId[v];
                if (compU != compV && !condensation.get(compU).contains(compV)) {
                    condensation.get(compU).add(compV);
                }
            }
        }

        return condensation;
    }

    public void printComponents() {
        System.out.println("Strongly Connected Components (" + components.size() + " components):");
        for (int i = 0; i < components.size(); i++) {
            Collections.sort(components.get(i));
            System.out.println("  Component " + i + " (size " + components.get(i).size() + "): " + components.get(i));
        }
    }
}