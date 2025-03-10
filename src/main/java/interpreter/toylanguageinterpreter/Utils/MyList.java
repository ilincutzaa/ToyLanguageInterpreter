package interpreter.toylanguageinterpreter.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MyList<T> implements MyIList<T>{
    private final List<T> list;
    public MyList(){
        list = Collections.synchronizedList(new ArrayList<>());
    }
    @Override
    public void add(T t) {
        list.add(t);
    }
    @Override
    public String toString(){
        return list.toString();
    }
    @Override
    public List<T> getList(){
        return list;
    }

    @Override
    public boolean contains(T t) {
        return list.contains(t);
    }

    @Override
    public void remove(T t) {
        list.remove(t);
    }

    @Override
    public int size() {
        return list.size();
    }
}
