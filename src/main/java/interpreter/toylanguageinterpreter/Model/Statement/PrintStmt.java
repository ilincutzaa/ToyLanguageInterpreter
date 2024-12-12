package interpreter.toylanguageinterpreter.Model.Statement;

import interpreter.toylanguageinterpreter.Model.Expression.Exp;
import interpreter.toylanguageinterpreter.Model.Type.Type;
import interpreter.toylanguageinterpreter.Model.Value.Value;
import interpreter.toylanguageinterpreter.Utils.MyException;
import interpreter.toylanguageinterpreter.Utils.MyIDictionary;
import interpreter.toylanguageinterpreter.Utils.MyIHeap;
import interpreter.toylanguageinterpreter.Utils.MyIList;
import interpreter.toylanguageinterpreter.Model.PrgState;

public class PrintStmt implements IStmt{
    private final Exp exp;
    public PrintStmt(Exp exp) {
        this.exp = exp;
    }

    @Override
    public String toString() {
        return "Print(" + exp.toString() + ")";
    }
    
    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIList<Value> out = state.getOut();
        MyIDictionary<String, Value> tbl = state.getSymTable();
        MyIHeap<Value> heap = state.getHeapTable();

        Value value = exp.eval(tbl, heap);
        if(value == null)
            throw new MyException("Value was not declared");
        out.add(value);
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new PrintStmt(exp.deepCopy());
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        exp.typeCheck(typeEnv);
        return typeEnv;
    }
}
