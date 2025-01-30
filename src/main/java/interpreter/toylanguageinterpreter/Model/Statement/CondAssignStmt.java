package interpreter.toylanguageinterpreter.Model.Statement;

import interpreter.toylanguageinterpreter.Model.Expression.Exp;
import interpreter.toylanguageinterpreter.Model.PrgState;
import interpreter.toylanguageinterpreter.Model.Type.BoolType;
import interpreter.toylanguageinterpreter.Model.Type.Type;
import interpreter.toylanguageinterpreter.Utils.MyException;
import interpreter.toylanguageinterpreter.Utils.MyIDictionary;


public class CondAssignStmt implements IStmt {
    private final String varName;
    private final Exp condExp;
    private final Exp thenExp;
    private final Exp elseExp;

    public CondAssignStmt(String varName, Exp condExp, Exp thenExp, Exp elseExp) {
        this.varName = varName;
        this.condExp = condExp;
        this.thenExp = thenExp;
        this.elseExp = elseExp;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IStmt ifStmt = new IfStmt(condExp, new AssignStmt(varName, thenExp), new AssignStmt(varName, elseExp));
        state.getExeStack().push(ifStmt);
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new CondAssignStmt(varName, condExp, thenExp, elseExp);
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type t1 = condExp.typeCheck(typeEnv);
        if(t1 instanceof BoolType){
            Type t2 = thenExp.typeCheck(typeEnv);
            Type t3 = elseExp.typeCheck(typeEnv);
            Type tv = typeEnv.lookUp(varName);
            if(!(t2.equals(tv) && t3.equals(tv)))
                throw new MyException("Exp2, exp 3 and v in CondAssign do not have the same type");
        }
        else
            throw new MyException("Exp1 in CondAssign is not a BoolType");
        return typeEnv;
    }

    @Override
    public String toString() {
        return varName + "=" + condExp + "?" + thenExp + ":" + elseExp;
    }
}

