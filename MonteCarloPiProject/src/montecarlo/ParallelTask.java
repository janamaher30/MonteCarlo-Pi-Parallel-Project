package montecarlo;
import java.util.concurrent.Callable;
public class ParallelTask implements Callable<Long> {

    private long points;

    public ParallelTask(long points) {
        this.points = points;
    }

    @Override
    public Long call() {
        // Generate random points
        // Count hits
        return 0L;
    }
}
