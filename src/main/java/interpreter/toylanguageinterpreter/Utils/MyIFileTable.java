package interpreter.toylanguageinterpreter.Utils;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface MyIFileTable<K,V> {
    void put(K key, V value);
    V lookUp(K key);
    boolean containsKey(K key);
    void update(K key, V value);
    Set<Map.Entry<K,V>> entrySet();
    void remove(K key);
    Collection<V> values();
    Set<K> keySet();
}
