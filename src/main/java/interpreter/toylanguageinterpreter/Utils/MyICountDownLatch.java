package interpreter.toylanguageinterpreter.Utils;

import interpreter.toylanguageinterpreter.Model.Value.IntValue;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface MyICountDownLatch<T> {
    AtomicIntegerKey getIndex();
    IntValue add(T value);
    T lookUp(IntValue index);
    void remove(IntValue index);
    Set<Map.Entry<IntValue,T>> entrySet();
    void update(IntValue index, T value);
    void setContent(Map<IntValue,T> contents);
    Collection<T> values();
    boolean containsKey(IntValue index);
}
