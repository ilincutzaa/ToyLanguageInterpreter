package interpreter.toylanguageinterpreter.Model.Statement;

import interpreter.toylanguageinterpreter.Model.Expression.Exp;
import interpreter.toylanguageinterpreter.Model.PrgState;
import interpreter.toylanguageinterpreter.Model.Type.IntType;
import interpreter.toylanguageinterpreter.Model.Type.Type;
import interpreter.toylanguageinterpreter.Model.Value.IntValue;
import interpreter.toylanguageinterpreter.Model.Value.Value;
import interpreter.toylanguageinterpreter.Utils.MyException;
import interpreter.toylanguageinterpreter.Utils.MyIDictionary;


public class CreateLatchStmt implements IStmt{
    private final String varName;
    private final Exp exp;

    public CreateLatchStmt(String varName, Exp exp) {
        this.varName = varName;
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        var heap = state.getHeapTable();
        var symTbl = state.getSymTable();
        var latchTbl = state.getLatchTable();
        Value num1 = exp.eval(symTbl,heap);
        if(!(num1.getType() instanceof IntType))
            throw new MyException("Exp is not an integer type");
        int i = latchTbl.getIndex().get();
        latchTbl.add((IntValue) num1);
        if(symTbl.containsKey(varName)){
            if(symTbl.lookUp(varName).getType() instanceof IntType)
                symTbl.put(varName, new IntValue(i));
            else throw new MyException("Variable" +varName+ " is not an integer type");
        }
        else throw new MyException("Variable" +varName+ " not found");
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new CreateLatchStmt(varName, exp.deepCopy());
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type t1 = typeEnv.lookUp(varName);
        if(t1 instanceof IntType){
            Type t2 = exp.typeCheck(typeEnv);
            if(!(t2 instanceof IntType))
                throw new MyException("Exp " +exp+" is not an integer type");
        }
        else throw new MyException("Var " +varName+ " is not an integer type");
        return typeEnv;
    }

    @Override
    public String toString() {
        return "newLatch(" + varName + "," + exp + ")";
    }
}
