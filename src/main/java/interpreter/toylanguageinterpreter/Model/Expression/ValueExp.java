package interpreter.toylanguageinterpreter.Model.Expression;

import interpreter.toylanguageinterpreter.Model.Type.Type;
import interpreter.toylanguageinterpreter.Utils.MyException;
import interpreter.toylanguageinterpreter.Utils.MyIDictionary;
import interpreter.toylanguageinterpreter.Model.Value.Value;
import interpreter.toylanguageinterpreter.Utils.MyIHeap;

public class ValueExp implements Exp{
    private final Value val;
    public ValueExp(Value val) {
        this.val = val;
    }
    public Value eval(MyIDictionary<String,Value> tbl, MyIHeap<Value> hp) throws MyException {
        return val;
    }

    @Override
    public Exp deepCopy() {
        return new ValueExp(val.deepCopy());
    }

    @Override
    public Type typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        return val.getType();
    }

    public String getType(){
        return val.getType().toString();
    }

    @Override
    public String toString() {
        return val.toString();
    }
}
