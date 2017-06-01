package mapper;

import java.util.concurrent.Callable;

public class MapperService implements IHandle{

    @Override
    public void handle(String line) {
     // TODO Implements the line of data here, register to ResultCollector, and finish analysis
        Callable<Integer> thread = new MapperThread(line);
        ResultCollector.regesterNstart(thread);
    }

}
