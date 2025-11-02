package graph.model;

public class GraphData {
    public static class WeightedEdge {
        public int u, v, w;
        public WeightedEdge(int u, int v, int w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }
    }
}