package interpreter.toylanguageinterpreter.Utils;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MyHeap<T> implements MyIHeap<T>{
    private final ConcurrentHashMap<AtomicIntegerKey,T> table;
    private final AtomicIntegerKey index;

    public MyHeap(){
        table = new ConcurrentHashMap<>();
        index = new AtomicIntegerKey(1);
    }

    public AtomicIntegerKey getIndex() {
        return index;
    }

    @Override
    public void add(T value) {
        table.put(new AtomicIntegerKey(index.getAndIncrement()), value);
    }

    @Override
    public T lookUp(AtomicIntegerKey index) {
        return table.get(index);
    }

    @Override
    public void remove(AtomicIntegerKey index) {
        table.remove(index);
    }

    @Override
    public Set<Map.Entry<AtomicIntegerKey, T>> entrySet() {
        return table.entrySet();
    }

    @Override
    public void update(AtomicIntegerKey index, T value) {
        table.replace(index, value);
    }

    @Override
    public void setContent(Map<AtomicIntegerKey, T> contents) {
        table.clear();
        table.putAll(contents);
        index.set(table.size()+1);
    }

    @Override
    public Collection<T> values() {
        return table.values();
    }


    @Override
    public String toString(){
        return table.toString();
    }

}
