package graph;

public class Metrics {
    private long startTime;
    private long endTime;

    public int dfsVisits = 0;
    public int dfsEdges = 0;
    public int kahnPushes = 0;
    public int kahnPops = 0;
    public int relaxations = 0;

    public void startTimer() {
        startTime = System.nanoTime();
    }

    public void stopTimer() {
        endTime = System.nanoTime();
    }

    public long getElapsedTime() {
        return endTime - startTime;
    }

    public void reset() {
        dfsVisits = 0;
        dfsEdges = 0;
        kahnPushes = 0;
        kahnPops = 0;
        relaxations = 0;
    }

    @Override
    public String toString() {
        return String.format(
                "Metrics[DFS Visits: %d, DFS Edges: %d, Kahn Pushes: %d, Kahn Pops: %d, Relaxations: %d, Time: %d ns]",
                dfsVisits, dfsEdges, kahnPushes, kahnPops, relaxations, getElapsedTime()
        );
    }
}