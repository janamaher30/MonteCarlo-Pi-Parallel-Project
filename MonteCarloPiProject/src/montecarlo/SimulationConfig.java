package montecarlo;

public class SimulationConfig {

    private long totalPoints;
    private int numTasks;
    private int numThreads;

    public SimulationConfig(long totalPoints, int numTasks, int numThreads) {
        setTotalPoints(totalPoints);
        setNumTasks(numTasks);
        setNumThreads(numThreads);
    }

    public long getTotalPoints() { return totalPoints; }
    public int getNumTasks() { return numTasks; }
    public int getNumThreads() { return numThreads; }

    public void setTotalPoints(long totalPoints) {
        if (totalPoints <= 0)
            throw new IllegalArgumentException("totalPoints must be > 0, got " + totalPoints);
        if (numTasks > 0 && numTasks > totalPoints)
            throw new IllegalArgumentException("totalPoints must be >= numTasks");
        this.totalPoints = totalPoints;
    }

    public void setNumTasks(int numTasks) {
        if (numTasks <= 0)
            throw new IllegalArgumentException("numTasks must be > 0, got " + numTasks);
        if (numTasks > totalPoints)
            throw new IllegalArgumentException("numTasks must be <= totalPoints");
        this.numTasks = numTasks;
    }

    public void setNumThreads(int numThreads) {
        if (numThreads <= 0)
            throw new IllegalArgumentException("numThreads must be > 0, got " + numThreads);
        this.numThreads = numThreads;
    }

    public long pointsForTask(int taskIndex) {
        if (taskIndex < 0 || taskIndex >= numTasks)
            throw new IllegalArgumentException("taskIndex out of range: " + taskIndex);
        long base = totalPoints / numTasks;
        long remainder = totalPoints % numTasks;
        return base + (taskIndex < remainder ? 1 : 0);
    }

    @Override
    public String toString() {
        return "N=" + totalPoints + ", tasks=" + numTasks + ", threads=" + numThreads;
    }
}