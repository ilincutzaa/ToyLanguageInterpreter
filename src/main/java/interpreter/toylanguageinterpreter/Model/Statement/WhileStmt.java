package interpreter.toylanguageinterpreter.Model.Statement;

import interpreter.toylanguageinterpreter.Model.Expression.Exp;
import interpreter.toylanguageinterpreter.Model.PrgState;
import interpreter.toylanguageinterpreter.Model.Type.BoolType;
import interpreter.toylanguageinterpreter.Model.Type.Type;
import interpreter.toylanguageinterpreter.Model.Value.BoolValue;
import interpreter.toylanguageinterpreter.Model.Value.Value;
import interpreter.toylanguageinterpreter.Utils.MyException;
import interpreter.toylanguageinterpreter.Utils.MyIDictionary;
import interpreter.toylanguageinterpreter.Utils.MyIHeap;
import interpreter.toylanguageinterpreter.Utils.MyIStack;

public class WhileStmt implements IStmt{
    Exp exp;
    IStmt stmt;

    public WhileStmt(Exp exp, IStmt stmt) {
        this.exp = exp;
        this.stmt = stmt;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIStack<IStmt> stk = state.getExeStack();
        MyIDictionary<String, Value> symtbl = state.getSymTable();
        MyIHeap<Value> heap = state.getHeapTable();
        IStmt cpy = this.deepCopy();
        Value val = exp.eval(symtbl,heap);
        if(val.getType() instanceof BoolType){
            if(val.equals(new BoolValue(true)))
                stk.push(new CompStmt(stmt,cpy));
        }
        else
            throw new MyException("exp is not of type bool");
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new WhileStmt(exp.deepCopy(), stmt.deepCopy());
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type texp = exp.typeCheck(typeEnv);
        if(texp instanceof BoolType){
            stmt.typeCheck(typeEnv.deepCopy());
            return typeEnv;
        }
        else
            throw new MyException("The condition of WHILE is not of type bool");
    }

    @Override
    public String toString() {
        return "while(" + exp + ") (" + stmt + ")";
    }
}
