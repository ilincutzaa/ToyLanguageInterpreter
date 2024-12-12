package interpreter.toylanguageinterpreter.Model.Statement;

import interpreter.toylanguageinterpreter.Model.*;
import interpreter.toylanguageinterpreter.Model.Type.Type;
import interpreter.toylanguageinterpreter.Utils.MyIDictionary;
import interpreter.toylanguageinterpreter.Utils.MyIHeap;
import interpreter.toylanguageinterpreter.Utils.MyIStack;
import interpreter.toylanguageinterpreter.Model.Expression.Exp;
import interpreter.toylanguageinterpreter.Model.Type.BoolType;
import interpreter.toylanguageinterpreter.Model.Value.BoolValue;
import interpreter.toylanguageinterpreter.Model.Value.Value;
import interpreter.toylanguageinterpreter.Utils.MyException;

public class IfStmt implements IStmt{
    private final Exp exp;
    private final IStmt thenS;
    private final IStmt elseS;
    public IfStmt(Exp exp, IStmt thenS, IStmt elseS) {
        this.exp = exp;
        this.thenS = thenS;
        this.elseS = elseS;
    }

    @Override
    public String toString() {
        return "IF(" + exp.toString() + ") THEN(" + thenS.toString() + ") ELSE(" + elseS.toString() + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> tbl = state.getSymTable();
        MyIStack<IStmt> stk = state.getExeStack();
        MyIHeap<Value> heap = state.getHeapTable();

        Value cond = exp.eval(tbl,heap);
        if(!(cond.getType() instanceof BoolType))
            throw new MyException("conditional expression is not a boolean");
        else
            if(cond.equals(new BoolValue(true)))
                stk.push(thenS);
            else 
                stk.push(elseS);
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new IfStmt(exp.deepCopy(), thenS.deepCopy(), elseS.deepCopy());
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type texp = exp.typeCheck(typeEnv);
        if(texp instanceof BoolType){
            thenS.typeCheck(typeEnv.deepCopy());
            elseS.typeCheck(typeEnv.deepCopy());
            return typeEnv;
        }
        else
            throw new MyException("The condition of IF is not a boolean");
    }
}
