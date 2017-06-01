package mapper;

import java.util.Random;
import java.util.concurrent.Callable;

/**
 * Implementation of mapper
 * @author megnji
 *
 */
public class MapperThread implements Callable<Integer>{

    private String str;
    public MapperThread(String string){
        str = string;
    }
    

    @Override
    public Integer call() throws Exception {
        //TODO : do something with the str variable
        return new Random().nextInt(100);
    }

}
