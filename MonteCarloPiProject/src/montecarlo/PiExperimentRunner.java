package montecarlo;

import java.util.Locale;
public class PiExperimentRunner {
    
    private static final long[] pointCounts = { 100_000L, 1_000_000L, 10_000_000L };
    private static final int[] threadCounts = {2, 4, 8};
    private static final int taskPerThread = 4;
    private static final int repeatCount = 3;
    private final PiEstimator sequentialEstimator = new SequentialPiEstimator();
    private final PiEstimator parallelEstimator = new ParallelPiEstimator();

    public void runExperiments() {
        
        System.out.println("========== MONTE CARLO PI EXPERIMENTS ==========");
        System.out.println("Actual PI = " + Math.PI);
        System.out.printf(Locale.US,
                "%-12s %-12s %-10s %-16s %-14s %-14s %-10s%n",
                "Points (N)", "Method", "Threads",
                "Estimated PI", "Time (ms)", "Abs. Error", "Speedup");

        System.out.println("--------------------------------------------------------------------------");
        for (long totalPoints : pointCounts) {
            SimulationConfig sequentialConfig = new SimulationConfig(totalPoints, 1, 1);
            ExperimentStats sequentialStats = runRepeatedExperiments(sequentialEstimator, sequentialConfig);
            printResult(totalPoints, "Sequential", 1, sequentialStats, Double.NaN);
            for (int numThreads : threadCounts) {
                int numTasks = numThreads * taskPerThread;
                SimulationConfig parallelConfig = new SimulationConfig(totalPoints, numTasks, numThreads);
                ExperimentStats parallelStats = runRepeatedExperiments(parallelEstimator, parallelConfig);
                double speedup = sequentialStats.averageTime / parallelStats.averageTime;
                printResult(totalPoints, "Parallel", numThreads, parallelStats, speedup);
            }
            System.out.println();
        }
    }
    
    private ExperimentStats runRepeatedExperiments(PiEstimator estimator, SimulationConfig config) {
        
        double totalPi = 0.0;
        double totalTime = 0.0;
        double totalError = 0.0;
        for (int i = 0; i < repeatCount; i++) {
            long startTime = System.nanoTime();
            double estimatedPi = estimator.estimatePi(config);
            long endTime = System.nanoTime();
            double runtimeMs = (endTime - startTime) / 1_000_000.0;
            double absoluteError = Math.abs(estimatedPi - Math.PI);
            totalPi += estimatedPi;
            totalTime += runtimeMs;
            totalError += absoluteError;
        }
        return new ExperimentStats(totalPi / repeatCount, totalTime / repeatCount, totalError / repeatCount);
    }
    
    private void printResult(long points, String method, int threads, ExperimentStats stats, double speedup){
        
        String speedupText = Double.isNaN(speedup) ? "-" : String.format(Locale.US, "%.3f", speedup);
        System.out.printf(Locale.US,
                "%-12d %-12s %-10d %-16.8f %-14.3f %-14.8f %-10s%n",
                points, method, threads,
                stats.averagePi, stats.averageTime,
                stats.averageError, speedupText);
    }

    private static class ExperimentStats {
        
        private final double averagePi;
        private final double averageTime;
        private final double averageError;
        private ExperimentStats(double averagePi, double averageTime, double averageError) {
            this.averagePi = averagePi;
            this.averageTime = averageTime;
            this.averageError = averageError;
        }
    }
    
    public static void main(String[] args) {
        new PiExperimentRunner().runExperiments();
    }
}