package interpreter.toylanguageinterpreter.Model.Statement;

import interpreter.toylanguageinterpreter.Model.PrgState;
import interpreter.toylanguageinterpreter.Model.Type.IntType;
import interpreter.toylanguageinterpreter.Model.Type.Type;
import interpreter.toylanguageinterpreter.Model.Value.IntValue;
import interpreter.toylanguageinterpreter.Model.Value.Value;
import interpreter.toylanguageinterpreter.Utils.MyException;
import interpreter.toylanguageinterpreter.Utils.MyIDictionary;

public class AwaitStmt implements IStmt{
    private final String varName;

    public AwaitStmt(String varName) {
        this.varName = varName;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        var stk = state.getExeStack();
        var symTbl = state.getSymTable();
        var latchTbl = state.getLatchTable();
        Value foundIndex = symTbl.lookUp(varName);
        if(foundIndex == null)
            throw new MyException("Variable '" + varName + "' not found");
        if(!(foundIndex.getType() instanceof IntType))
            throw new MyException("Variable '" + varName + "' is not an int");
        IntValue index = (IntValue) foundIndex;
        if(!(latchTbl.containsKey(index)))
            throw new MyException("Index " + index + " not found in latch table");
        IntValue foundVal = latchTbl.lookUp(index);
        if(!foundVal.equals(new IntValue(0)))
            stk.push(this);
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new AwaitStmt(varName);
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type t1 = typeEnv.lookUp(varName);
        if(!(t1 instanceof IntType))
            throw new MyException("Variable '" + varName + "' is not an int");
        return typeEnv;
    }

    @Override
    public String toString() {
        return "await(" + varName + ")";
    }
}
