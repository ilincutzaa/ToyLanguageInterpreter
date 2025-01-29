package interpreter.toylanguageinterpreter.Utils;

import java.util.List;

public interface MyIList<T> {
    void add(T t);
    List<T> getList();
    boolean contains(T t);
    void remove(T t);
    int size();
}
