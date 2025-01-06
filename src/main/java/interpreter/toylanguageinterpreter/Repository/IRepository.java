package interpreter.toylanguageinterpreter.Repository;

import interpreter.toylanguageinterpreter.Model.PrgState;
import interpreter.toylanguageinterpreter.Utils.MyException;

import java.util.List;

public interface IRepository<T> {
    void add(T state);
    List<T> getPrgList();
    void setPrgList(List<T> prgList);
    int getSize();
    void logPrgStateExec(PrgState prgState) throws MyException;
}
