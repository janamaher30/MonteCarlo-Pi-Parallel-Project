package montecarlo;
public class SimulationConfig {
    private long totalPoints;
    private int numTasks;
    private int numThreads;

    public SimulationConfig(long totalPoints, int numTasks, int numThreads) {
        // Constructor
    }

    public long getTotalPoints() { return totalPoints; }
    public int getNumTasks() { return numTasks; }
    public int getNumThreads() { return numThreads; }
}
