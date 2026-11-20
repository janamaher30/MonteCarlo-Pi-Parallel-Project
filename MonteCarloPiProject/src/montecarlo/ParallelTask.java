package montecarlo;

import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

public class ParallelTask implements Callable<Long> {

    private final long points;

    public ParallelTask(long points) {
        if (points < 0) {
            throw new IllegalArgumentException("Points cannot be negative");
        }

        this.points = points;
    }

    @Override
    public Long call() {

        long hits = 0;

        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (long i = 0; i < points; i++) {

            double x = random.nextDouble();
            double y = random.nextDouble();

            if (x * x + y * y <= 1) {
                hits++;
            }
        }

        return hits;
    }
}
