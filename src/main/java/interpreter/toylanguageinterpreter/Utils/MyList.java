package interpreter.toylanguageinterpreter.Utils;

import java.util.ArrayList;
import java.util.List;

public class MyList<T> implements MyIList<T>{
    private final List<T> list;
    public MyList(){
        list = new ArrayList<>();
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
}
