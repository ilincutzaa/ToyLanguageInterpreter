package interpreter.toylanguageinterpreter.Model.Expression;

import interpreter.toylanguageinterpreter.Model.Type.RefType;
import interpreter.toylanguageinterpreter.Model.Type.Type;
import interpreter.toylanguageinterpreter.Model.Value.RefValue;
import interpreter.toylanguageinterpreter.Model.Value.Value;
import interpreter.toylanguageinterpreter.Utils.AtomicIntegerKey;
import interpreter.toylanguageinterpreter.Utils.MyException;
import interpreter.toylanguageinterpreter.Utils.MyIDictionary;
import interpreter.toylanguageinterpreter.Utils.MyIHeap;

public class ReadHeap implements Exp{
    Exp exp;

    public ReadHeap(Exp exp) {
        this.exp = exp;
    }

    @Override
    public Value eval(MyIDictionary<String,Value> tbl, MyIHeap<Value> hp) throws MyException {
        Value val = exp.eval(tbl, hp);
        if(val instanceof RefValue){
            AtomicIntegerKey address = new AtomicIntegerKey(((RefValue)val).getAddress());
            Value hp_val = hp.lookUp(address);
            if(hp_val != null)
                return hp_val;
            else
                throw new MyException("Address not found");
        }
        else
            throw new MyException("Expression cannot be evaluated to a RefValue");
    }

    @Override
    public Exp deepCopy() {
        return new ReadHeap(exp.deepCopy());
    }

    @Override
    public Type typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type t = exp.typeCheck(typeEnv);
        if(t instanceof RefType){
            RefType rft = (RefType) t;
            return rft.getInner();
        }
        else
            throw new MyException("The rH argument cannot be evaluated to a RefValue");
    }

    @Override
    public String toString() {
        return "rH(" + exp.toString() + ")";
    }
}
