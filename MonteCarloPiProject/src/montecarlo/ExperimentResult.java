
package montecarlo;
public class ExperimentResult {

    private double estimatedPi;
    private long runtime;
    private double absoluteError;
    private int numThreads;
    private double speedup;

    public ExperimentResult(double estimatedPi, long runtime, double absoluteError, int numThreads, double speedup) {
        this.estimatedPi = estimatedPi;
        this.runtime = runtime;
        this.absoluteError = absoluteError;
        this.numThreads = numThreads;
        this.speedup = speedup;
    }

    public double getEstimatedPi() {
        return estimatedPi;
    }

    public double getRuntime() {
        return runtime;
    }

    public double getAbsoluteError() {
        return absoluteError;
    }

    public int getNumThreads() {
        return numThreads;
    }

    public double getSpeedup() {
        return speedup;
    }

    @Override
    public String toString() {
        return String.format("Threads: %d | Pi: %.6f | Error: %.6f | Runtime: %.3f ms | Speedup: %.2fx",
                numThreads, estimatedPi, absoluteError, runtime, speedup);
    }
}
