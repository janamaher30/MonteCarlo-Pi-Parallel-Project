package montecarlo;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
public class ParallelPiEstimator implements PiEstimator {

    @Override
    public double estimatePi(SimulationConfig config) {
       long totalpoints = config.getTotalPoints();
       int numTasks = config.getNumTasks();
       int numThreads = config.getNumThreads();
       
       if(totalpoints<=0 || numTasks<=0 || numThreads<=0){
           throw new IllegalArgumentException(
                   "Configuration values must be positive"
           );
       }
       ExecutorService executor= Executors.newFixedThreadPool(numThreads);
       try{
           List <Future<Long>> futures=new ArrayList<>();
           long pointsperTask= totalpoints/numTasks;
           long reminder= totalpoints%numTasks;
           for(int i=0;i<numTasks;i++){
               long taskPoints=pointsperTask;
               if(i<reminder){
                   taskPoints++;
               }
                ParallelTask task=new ParallelTask(taskPoints);
                Future<Long> future =executor.submit(task);
                futures.add(future);
            }
               long totalHits=0;
               for(Future<Long> future: futures){
                   totalHits += future.get();
               }
                   return 4.0 * totalHits / totalpoints;

        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
            throw new RuntimeException("Parallel execution interrupted", e);
        }catch (ExecutionException e) {
            throw new RuntimeException("A task failed", e);
        }finally {
            executor.shutdown();
        }
    }
}
    

