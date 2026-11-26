package montecarlo;
import java.util.Locale;
public class PiExperimentRunner {
    private static final long[] pointCounts = {
        100_000L, 1_000_000L, 10_000_000L
    };
    private static final int[] threadCounts = {2, 4, 8};
    private static final int taskPerThread = 4;
    private static final int repeatCount = 3;
    private final PiEstimator sequentialEstimator =
            new SequentialPiEstimator();
    private final PiEstimator parallelEstimator =
            new ParallelPiEstimator();
    public void runExperiments() {
        System.out.println("========== MONTE CARLO PI EXPERIMENTS ==========");
        System.out.println("Actual PI = " + Math.PI);
        System.out.printf(Locale.US,
                "%-12s %-12s %-10s %-16s %-14s %-14s %-10s%n",
                "Points (N)", "Method", "Threads",
                "Estimated PI", "Time (ms)", "Abs. Error", "Speedup");
        System.out.println("--------------------------------------------------------------------------");
        for (long totalPoints : pointCounts) {
            SimulationConfig sequentialConfig =
                    new SimulationConfig(totalPoints, 1, 1);
            ExperimentResult sequentialResult =
                    runRepeatedExperiments(
                            sequentialEstimator, sequentialConfig, 1
                    );
            printResult(totalPoints, "Sequential", sequentialResult);
            for (int numThreads : threadCounts) {
                int numTasks = numThreads * taskPerThread;
                SimulationConfig parallelConfig =
                        new SimulationConfig(
                                totalPoints, numTasks, numThreads
                        );
                ExperimentResult parallelStats =
                        runRepeatedExperiments(
                                parallelEstimator, parallelConfig, numThreads
                        );
                double speedup =
                        sequentialResult.getRuntime()
                        / parallelStats.getRuntime();
                ExperimentResult parallelResult =
                        new ExperimentResult(
                                parallelStats.getEstimatedPi(),
                                parallelStats.getRuntime(),
                                parallelStats.getAbsoluteError(),
                                parallelStats.getNumThreads(),
                                speedup
                        );
                printResult(totalPoints, "Parallel", parallelResult);
            }
            System.out.println();
        }
    }
    private ExperimentResult runRepeatedExperiments(
            PiEstimator estimator,
            SimulationConfig config,
            int numThreads) {
        double totalPi = 0.0;
        double totalTime = 0.0;
        double totalError = 0.0;
        for (int i = 0; i < repeatCount; i++) {
            long startTime = System.nanoTime();
            double estimatedPi = estimator.estimatePi(config);
            long endTime = System.nanoTime();
            double runtimeMs =
                    (endTime - startTime) / 1_000_000.0;
            double absoluteError =
                    Math.abs(estimatedPi - Math.PI);
            totalPi += estimatedPi;
            totalTime += runtimeMs;
            totalError += absoluteError;
        }
        double averagePi = totalPi / repeatCount;
        double averageTime = totalTime / repeatCount;
        double averageError = totalError / repeatCount;
        return new ExperimentResult(
                averagePi,
                averageTime,
                averageError,
                numThreads,
                Double.NaN
        );
    }
    private void printResult(
            long points,
            String method,
            ExperimentResult result) {
        String speedupText =
                Double.isNaN(result.getSpeedup())
                ? "-"
                : String.format(Locale.US, "%.3f",
                        result.getSpeedup());
        System.out.printf(Locale.US,
                "%-12d %-12s %-10d %-16.8f %-14.3f %-14.8f %-10s%n",
                points,
                method,
                result.getNumThreads(),
                result.getEstimatedPi(),
                result.getRuntime(),
                result.getAbsoluteError(),
                speedupText
        );
    }
}
