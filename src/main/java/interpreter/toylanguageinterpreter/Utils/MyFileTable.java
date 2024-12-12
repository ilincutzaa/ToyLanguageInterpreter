package interpreter.toylanguageinterpreter.Utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MyFileTable<K,V> implements MyIFileTable<K,V>{

    private final Map<K,V> table;

    public MyFileTable(){
        table = new HashMap<>();
    }

    @Override
    public void put(K key, V value) {
        table.put(key, value);
    }

    @Override
    public V lookUp(K key) {
        if(!this.containsKey(key)) {
            return null;
        }
        return table.get(key);
    }

    @Override
    public boolean containsKey(K key) {
        return table.containsKey(key);
    }

    @Override
    public void update(K key, V value) {
        table.replace(key, value);
    }

    @Override
    public String toString(){
        return table.toString();
    }

    public Set<Map.Entry<K,V>> entrySet(){
        return table.entrySet();
    }

    @Override
    public void remove(K key) {
        table.remove(key);
    }

    @Override
    public Collection<V> values(){
        return table.values();
    }
}
