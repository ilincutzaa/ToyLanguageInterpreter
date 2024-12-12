package interpreter.toylanguageinterpreter.Model.Statement;

import interpreter.toylanguageinterpreter.Model.Expression.Exp;
import interpreter.toylanguageinterpreter.Model.PrgState;
import interpreter.toylanguageinterpreter.Model.Type.RefType;
import interpreter.toylanguageinterpreter.Model.Type.Type;
import interpreter.toylanguageinterpreter.Model.Value.RefValue;
import interpreter.toylanguageinterpreter.Model.Value.Value;
import interpreter.toylanguageinterpreter.Utils.AtomicIntegerKey;
import interpreter.toylanguageinterpreter.Utils.MyException;
import interpreter.toylanguageinterpreter.Utils.MyIDictionary;
import interpreter.toylanguageinterpreter.Utils.MyIHeap;


public class WriteHeap implements IStmt{
    private final String varName;
    private final Exp exp;

    public WriteHeap(String var_name, Exp exp) {
        this.varName = var_name;
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> symtbl = state.getSymTable();
        MyIHeap<Value> heap = state.getHeapTable();
        Value var_value = symtbl.lookUp(varName);
        if(var_value != null) {
            if(var_value.getType() instanceof RefType) {
                RefValue var_ref = (RefValue) var_value;
                AtomicIntegerKey address = new AtomicIntegerKey(var_ref.getAddress());
                Value var_ref_value = heap.lookUp(address);
                if(var_ref_value != null) {
                    Value val = exp.eval(symtbl, heap);
                    if(var_ref_value.getType().equals(val.getType()))
                        heap.update(address, val);
                    else
                        throw new MyException("The location type of the ref is '" + var_ref.getType() + "' but the expression is of type '" + val.getType() + "'");
                }
                else
                    throw new MyException("Could not find reference with address " + var_ref.getAddress());
            }
            else
                throw new MyException("Variable '" + varName + "' is of type '" + var_value.getType() + "'");
        }
        else
            throw new MyException("Variable '" + varName + "' not found");
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new WriteHeap(varName, exp.deepCopy());
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type tvar = typeEnv.lookUp(varName);
        if(tvar == null)
            throw new MyException("variable "+varName+" was not declared before");
        if(!(tvar instanceof RefType))
            throw new MyException("variable "+varName+" is not a RefType");
        RefType rftvar = (RefType) tvar;
        Type texp = exp.typeCheck(typeEnv);
        if(!rftvar.getInner().equals(texp))
            throw new MyException("WriteHeap: LHS's inner type and RHS type do not match (" + rftvar.getInner() + "!=" + texp + ")");
        return typeEnv;
    }

    @Override
    public String toString() {
        return "WriteHeap("+ varName + ", " + exp + ')';
    }
}
