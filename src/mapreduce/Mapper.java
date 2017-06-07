package mapreduce;


import java.util.List;

/**
 * interface for map operation
 * @param <K>
 * @param <V>
 */
public interface Mapper<K, V> {
    /**
     *
     * @param input
     * @return list of pair
     */
    List<Pair<K, V>> map(Pair<K, V> input);
}
