package mapreduce;

import pu.RedLib.Reduction;

import java.util.*;
import java.util.concurrent.*;


public class Job<K, V> {

    /**
     * inputs to mapper
     */
    List<Pair<K, V>> source;


    Mapper<K, V> mapper;

    Reduction<V> reducer;

    // max number of threads to be used
    int maxNumberOfThread;

    /**
     *
     * @param source
     * @param mapper
     * @param reducer
     * @param maxNumberOfThread
     */
    public Job(List<Pair<K, V>> source, Mapper<K, V> mapper,
               Reduction<V> reducer, int maxNumberOfThread) {
        this.source = source;
        this.mapper = mapper;
        this.reducer = reducer;
        this.maxNumberOfThread = maxNumberOfThread;
//        this.numReducerThread = numReducerThread;
    }

    /**
     * mapper callable
     */
    class MapperCallable implements Callable<List<Pair<K, V>>>{

        // inputs for this mapper
        List<Pair<K, V>> inputs;

        public MapperCallable(List<Pair<K, V>> inputs) {
            this.inputs = inputs;
        }

        @Override
        public List<Pair<K, V>> call() throws Exception {

            List<Pair<K, V>> result = new ArrayList<>();

            for (Pair<K, V> input: inputs )
            {
                result.addAll(mapper.map(input));
            }

            return result;
        }
    }

    /**
     * reducer callable
     */
    class ReducerrCallable implements Callable<List<Pair<K, V>>>{

        // inputs for this reducer
        List<Map.Entry<K, List<V>>> inputs;

        public ReducerrCallable(List<Map.Entry<K, List<V>>> inputs) {
            this.inputs = inputs;
        }

        @Override
        public List<Pair<K, V>> call() throws Exception {

            List<Pair<K, V>> result = new ArrayList<>();


            for (Map.Entry<K, List<V>> input: inputs )
            {
                List<V> vs = input.getValue();

                if (vs.isEmpty()) {

                    continue;
                }

                V res = vs.get(0);

                for (V v : vs.subList(1, vs.size())) {

                    res = reducer.reduce(res, v);
                }

                result.add(new Pair<K, V>(input.getKey(), res));
            }

            return result;
        }
    }

    /**
     * execute the job
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void execute() throws ExecutionException, InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(maxNumberOfThread);

        List<Future<List<Pair<K, V>>>> mapResults = new ArrayList<>();

        // each mapper has about len inputs
        int len = source.size() / maxNumberOfThread;

        if (len < 1)
        {
            len = 1;
        }

        int start = 0;

        int end;

        // submit mapper tasks
        for (int i = 0; i< maxNumberOfThread && start < source.size(); i++) {

            if (i == maxNumberOfThread - 1)
            {
                end = source.size();
            }else{

                end = start + len;
            }

            MapperCallable callable = new MapperCallable(source.subList(start, end));

            // run the mapper thread
            Future<List<Pair<K, V>>> future = executorService.submit(callable);

            mapResults.add(future);

            start += len;

        }

        Map<K, List<V>> map = new HashMap<K, List<V>>();

        // collect mapper results
        for (Future<List<Pair<K, V>>> future: mapResults)
        {
            List<Pair<K, V>> result = future.get();

            for (Pair<K, V> p: result)
            {
                List<V> t = map.get(p.getKey());

                if (t == null)
                {
                    t = new ArrayList<V>();

                    map.put(p.getKey(), t);
                }

                t.add(p.getValue());

            }
        }

        Set<Map.Entry<K, List<V>>> entrySet =  map.entrySet();

        List<Map.Entry<K, List<V>>> entryList = new ArrayList<>(entrySet);

        start = 0;

        // each reducer has about len inputs
        len = entryList.size() / maxNumberOfThread;

        List<Future<List<Pair<K, V>>>> reduceResults = new ArrayList<>();

        // submit reducer tasks
        for (int i = 0; i< maxNumberOfThread && start < entryList.size(); i++)
        {
            if (i == maxNumberOfThread - 1)
            {
                end = entryList.size();

            }else{

                end = start + len;
            }

            ReducerrCallable reducerrCallable = new ReducerrCallable(entryList.subList(start, end));

            Future<List<Pair<K, V>>> future = executorService.submit(reducerrCallable);

            reduceResults.add(future);

            start += len;

        }

        // print out reducer result
        for (Future<List<Pair<K, V>>> reduceResult : reduceResults) {

            List<Pair<K, V>> res = reduceResult.get();

            for (Pair<K, V> p:res)
            {
                System.out.println(p);
            }
        }

        executorService.shutdown();

    }
}
