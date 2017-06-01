package mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Finalize the result and call Redlib apis for generating the result.
 * @author megnji
 *
 */
public class ResultCollector {

    private static List<Callable<Integer>> threadList  = new ArrayList<>();
    private static List<FutureTask<Integer>> futures;
    private static Integer sum;
    public static void regesterNstart(Callable<Integer> thread){
        threadList.add(thread);
        FutureTask<Integer> future = new FutureTask<Integer>(thread);
        futures.add(future);
        new Thread(future).start();
    }
    
    public static int getResult(){
        for (FutureTask<Integer> f : futures){
            try {
                sum += f.get();
            } catch (InterruptedException | ExecutionException e) {
                
                e.printStackTrace();
                return -1;
            }
        }
        //Call redlib api here
        return sum;
        
    }
    
}
