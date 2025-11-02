package graph.scc;

import graph.Metrics;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class SCCTest {
    @Test
    void testSCCOnCycle() {
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

        assertEquals(1, components.size());
        assertEquals(3, components.get(0).size());
        assertTrue(components.get(0).containsAll(Arrays.asList(0, 1, 2)));
    }
}