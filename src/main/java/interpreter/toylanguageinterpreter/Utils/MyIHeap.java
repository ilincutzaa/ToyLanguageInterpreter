package interpreter.toylanguageinterpreter.Utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public interface MyIHeap<T> {
    AtomicIntegerKey getIndex();
    void add(T value);
    T lookUp(AtomicIntegerKey index);
    void remove(AtomicIntegerKey index);
    Set<Map.Entry<AtomicIntegerKey,T>> entrySet();
    void update(AtomicIntegerKey index, T value);
    void setContent(Map<AtomicIntegerKey,T> contents);
    Collection<T> values();
}
