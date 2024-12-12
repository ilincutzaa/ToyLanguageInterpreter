package interpreter.toylanguageinterpreter.Model.Statement;

import interpreter.toylanguageinterpreter.Model.PrgState;
import interpreter.toylanguageinterpreter.Model.Type.Type;
import interpreter.toylanguageinterpreter.Utils.MyException;
import interpreter.toylanguageinterpreter.Utils.MyIDictionary;
import interpreter.toylanguageinterpreter.Utils.MyStack;

public class ForkStmt implements IStmt{
    private final IStmt stmt;

    public ForkStmt(IStmt stmt) {
        this.stmt = stmt;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        return new PrgState(new MyStack<>(), state.getSymTable().deepCopy(), state.getOut(), stmt, state.getFileTable(), state.getHeapTable());
    }

    @Override
    public IStmt deepCopy() {
        return new ForkStmt(stmt.deepCopy());
    }

    @Override
    public String toString() {
        return "fork(" + stmt.toString() + ")";
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        stmt.typeCheck(typeEnv.deepCopy());
        return typeEnv;
    }
}
