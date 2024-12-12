package interpreter.toylanguageinterpreter.Model.Statement;

import interpreter.toylanguageinterpreter.Model.Expression.Exp;
import interpreter.toylanguageinterpreter.Model.PrgState;
import interpreter.toylanguageinterpreter.Model.Type.RefType;
import interpreter.toylanguageinterpreter.Model.Type.Type;
import interpreter.toylanguageinterpreter.Model.Value.RefValue;
import interpreter.toylanguageinterpreter.Model.Value.Value;
import interpreter.toylanguageinterpreter.Utils.MyException;
import interpreter.toylanguageinterpreter.Utils.MyIDictionary;
import interpreter.toylanguageinterpreter.Utils.MyIHeap;

public class AllocStmt implements IStmt{
    private final String varName;
    private final Exp exp;

    public AllocStmt(String varName, Exp expression) {
        this.varName = varName;
        this.exp = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> symtbl = state.getSymTable();
        MyIHeap<Value> heap = state.getHeapTable();
        Value var_value = symtbl.lookUp(varName);
        if(var_value != null){
            if(var_value.getType() instanceof RefType){
                Value val = exp.eval(symtbl, heap);
                if(((RefType) var_value.getType()).getInner().equals(val.getType())){
                    heap.add(val);
                    RefValue newVal = new RefValue(heap.getIndex().get()-1, val.getType());
                    symtbl.put(varName, newVal);
                }
                else
                    throw new MyException("Variable '" + varName + "' is of type '" + var_value.getType() + "', not "+ val.getType());
            }
            else
                throw new MyException("Variable '" + varName + "' is not a reference type");
        }
        else
            throw new MyException("Variable '" + varName + "' not found");
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new AllocStmt(varName, exp.deepCopy());
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type tvar = typeEnv.lookUp(varName);
        Type texp = exp.typeCheck(typeEnv);
        if(tvar instanceof RefType){
            RefType rftvar = (RefType) tvar;
            if(rftvar.getInner().equals(texp))
                return typeEnv;
            else
                throw new MyException("Ref inner types do not match (" + rftvar.getInner() + "!=" + texp + ")");
        }
        else
            throw new MyException("Variable '" + varName + "' is not a reference type");
    }

    @Override
    public String toString() {
        return "new(" + varName + ", " + exp + ")";
    }
}
