package interpreter.toylanguageinterpreter.Model.Statement;

import interpreter.toylanguageinterpreter.Model.*;
import interpreter.toylanguageinterpreter.Utils.MyIDictionary;
import interpreter.toylanguageinterpreter.Utils.MyIHeap;
import interpreter.toylanguageinterpreter.Model.Expression.Exp;
import interpreter.toylanguageinterpreter.Model.Type.Type;
import interpreter.toylanguageinterpreter.Model.Value.Value;
import interpreter.toylanguageinterpreter.Utils.MyException;

public class AssignStmt implements IStmt{
    private final String id;
    private final Exp exp;
    public AssignStmt(String id, Exp exp) {
        this.id = id;
        this.exp = exp;
    }

    @Override
    public String toString() {
        return id + " = " + exp.toString();
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> symTbl = state.getSymTable();
        MyIHeap<Value> heap = state.getHeapTable();

        if (symTbl.containsKey(id)){
            Value val = exp.eval(symTbl, heap);
            Type typeId = symTbl.lookUp(id).getType();
            if(val.getType().equals(typeId))
                symTbl.update(id, val);
            else 
                throw new MyException("declared type of variable "+id+" and type of the assigned expression do not match");
        }
        else 
            throw new MyException("the used variable "+id+" was not declared before");
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new AssignStmt(id, exp.deepCopy());
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type tvar = typeEnv.lookUp(id);
        if(tvar == null)
            throw new MyException("variable "+id+" was not declared before");
        Type texp = exp.typeCheck(typeEnv);
        if(tvar.equals(texp))
            return typeEnv;
        else
            throw new MyException("Assignment: LHS and RHS have different types (" + tvar + "!=" + texp + ")");
    }

}
