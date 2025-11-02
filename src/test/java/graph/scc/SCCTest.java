package graph.scc;

import graph.Metrics;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class SCCTest {

    @Test
    void testSCCOnSimpleCycle() {
        // Test: 0 -> 1 -> 2 -> 0 (cycle of 3 nodes)
        int n = 3;
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }

        graph.get(0).add(1);
        graph.get(1).add(2);
        graph.get(2).add(0);

        Metrics metrics = new Metrics();
        SCC scc = new SCC(n, graph, metrics);
        List<List<Integer>> components = scc.findSCCs();

        // Should have 1 component with all 3 nodes
        assertEquals(1, components.size());
        assertEquals(3, components.get(0).size());
        assertTrue(components.get(0).containsAll(Arrays.asList(0, 1, 2)));
    }

    @Test
    void testSCCOnDAG() {
        // Test: 0 -> 1 -> 2, 0 -> 3 (DAG - each node is its own component)
        int n = 4;
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }

        graph.get(0).add(1);
        graph.get(0).add(3);
        graph.get(1).add(2);

        Metrics metrics = new Metrics();
        SCC scc = new SCC(n, graph, metrics);
        List<List<Integer>> components = scc.findSCCs();

        // In DAG, each node is its own SCC
        assertEquals(4, components.size());

        // Check that each component has exactly 1 node
        for (List<Integer> component : components) {
            assertEquals(1, component.size());
        }
    }

    @Test
    void testSCCOnTwoCycles() {
        // Test: Two separate cycles: 0<->1 and 2<->3
        int n = 4;
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }

        // First cycle: 0 -> 1 -> 0
        graph.get(0).add(1);
        graph.get(1).add(0);

        // Second cycle: 2 -> 3 -> 2
        graph.get(2).add(3);
        graph.get(3).add(2);

        Metrics metrics = new Metrics();
        SCC scc = new SCC(n, graph, metrics);
        List<List<Integer>> components = scc.findSCCs();

        // Should have 2 components, each of size 2
        assertEquals(2, components.size());

        // Each component should have 2 nodes
        for (List<Integer> component : components) {
            assertEquals(2, component.size());
        }

        // Check components contain correct nodes
        boolean foundFirstCycle = false;
        boolean foundSecondCycle = false;

        for (List<Integer> component : components) {
            if (component.contains(0) && component.contains(1)) {
                foundFirstCycle = true;
            }
            if (component.contains(2) && component.contains(3)) {
                foundSecondCycle = true;
            }
        }

        assertTrue(foundFirstCycle);
        assertTrue(foundSecondCycle);
    }

    @Test
    void testSCCOnSingleNode() {
        // Test: Single node graph
        int n = 1;
        List<List<Integer>> graph = new ArrayList<>();
        graph.add(new ArrayList<>()); // No edges

        Metrics metrics = new Metrics();
        SCC scc = new SCC(n, graph, metrics);
        List<List<Integer>> components = scc.findSCCs();

        // Should have 1 component with the single node
        assertEquals(1, components.size());
        assertEquals(1, components.get(0).size());
        assertEquals(0, components.get(0).get(0));
    }

    @Test
    void testSCCBuildCondensationGraph() {
        // Test condensation graph building
        int n = 5;
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }

        // Cycle: 0 -> 1 -> 2 -> 0
        graph.get(0).add(1);
        graph.get(1).add(2);
        graph.get(2).add(0);

        // Separate nodes: 3 -> 4
        graph.get(3).add(4);

        Metrics metrics = new Metrics();
        SCC scc = new SCC(n, graph, metrics);
        List<List<Integer>> components = scc.findSCCs();
        List<List<Integer>> condensation = scc.buildCondensationGraph();

        // Should have 3 components: [0,1,2], [3], [4]
        assertEquals(3, components.size());

        // Condensation graph should have 3 nodes
        assertEquals(3, condensation.size());
    }

    @Test
    void testSCCPerformanceMetrics() {
        // Test that metrics are being collected
        int n = 3;
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }

        graph.get(0).add(1);
        graph.get(1).add(2);
        graph.get(2).add(0);

        Metrics metrics = new Metrics();
        SCC scc = new SCC(n, graph, metrics);
        scc.findSCCs();

        // Should have recorded some DFS visits and edges
        assertTrue(metrics.dfsVisits > 0);
        assertTrue(metrics.dfsEdges > 0);
        assertTrue(metrics.getElapsedTime() >= 0);
    }
}