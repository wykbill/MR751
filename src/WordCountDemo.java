import mapreduce.Job;
import mapreduce.Mapper;
import mapreduce.Pair;
import pu.RedLib.IntegerSum;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

/**
 * a wordcount demo for the framework implemented
 */
public class WordCountDemo {

    /**
     * mapper
     */
    static class WordCountMapper implements Mapper<String, Integer>{

        @Override
        public List<Pair<String, Integer>> map(Pair<String, Integer> input) {

            List<Pair<String, Integer>> res = new ArrayList<>();
            for (String word : input.getKey().split(" ")) {

                res.add(new Pair<>(word, 1));
            }

            return res;
        }
    }


    public static void main(String[] args) throws ExecutionException, InterruptedException, FileNotFoundException {

        Scanner in = new Scanner(new FileReader("medium-test.txt"));

        List<Pair<String, Integer>> source = new ArrayList<>();

        while (in.hasNextLine())
        {
            source.add(new Pair<String, Integer>(in.nextLine(), null));
        }

        in.close();

        // create job using mapper defined above and reducer from RedLib
        Job<String, Integer> job = new Job<>(source, new WordCountMapper(), new IntegerSum(), 16);

        long startTime = System.nanoTime();
        job.execute();
        long endTime = System.nanoTime();
        System.out.println((endTime - startTime) * 0.000001);
    }
}
