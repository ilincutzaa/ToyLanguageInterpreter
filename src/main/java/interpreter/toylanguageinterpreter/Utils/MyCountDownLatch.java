package interpreter.toylanguageinterpreter.Utils;

import interpreter.toylanguageinterpreter.Model.Value.IntValue;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MyCountDownLatch<T> implements MyICountDownLatch<T>{
    private final ConcurrentHashMap<IntValue,T> table;
    private final AtomicIntegerKey index;

    public MyCountDownLatch(){
        table = new ConcurrentHashMap<>();
        index = new AtomicIntegerKey(1);
    }

    public AtomicIntegerKey getIndex() {
        return index;
    }

    @Override
    public IntValue add(T value) {
        int oldIndex = index.get();
        table.put(new IntValue(index.getAndIncrement()), value);
        return new IntValue(oldIndex);
    }

    @Override
    public T lookUp(IntValue index) {
        return table.get(index);
    }

    @Override
    public void remove(IntValue index) {
        table.remove(index);
    }

    @Override
    public Set<Map.Entry<IntValue, T>> entrySet() {
        return table.entrySet();
    }

    @Override
    public void update(IntValue index, T value) {
        table.replace(index, value);
    }

    @Override
    public void setContent(Map<IntValue, T> contents) {
        table.clear();
        table.putAll(contents);
        index.set(table.size()+1);
    }

    @Override
    public Collection<T> values() {
        return table.values();
    }

    @Override
    public boolean containsKey(IntValue index) {
        return table.containsKey(index);
    }


    @Override
    public String toString(){
        return table.toString();
    }

}
