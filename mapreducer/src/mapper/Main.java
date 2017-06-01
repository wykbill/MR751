package mapper;

/**
 * Show how to use BigFileReader, test only, should not show up in your assignment
 * Copy from : https://github.com/yongzhidai/ToolClass/tree/master/src/main/java/cn/dyz/tools/file
 * @author megnji
 */
public class Main {

    public static void main(String[] args) {
        BigFileReader.Builder builder = new BigFileReader.Builder("resources/input", new MapperService());
        builder.withTreahdSize(10).withCharset("gbk").withBufferSize(1024 * 1024);
        BigFileReader bigFileReader = builder.build();
        bigFileReader.start();
        
        int countResult = ResultCollector.getResult();
    }

}
