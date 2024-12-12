package interpreter.toylanguageinterpreter.Model.Expression;

import interpreter.toylanguageinterpreter.Model.Type.Type;
import interpreter.toylanguageinterpreter.Utils.MyException;
import interpreter.toylanguageinterpreter.Utils.MyIDictionary;
import interpreter.toylanguageinterpreter.Model.Value.Value;
import interpreter.toylanguageinterpreter.Utils.MyIHeap;

public interface Exp {
    Value eval(MyIDictionary<String,Value> tbl, MyIHeap<Value> hp) throws MyException;
    Exp deepCopy();
    Type typeCheck(MyIDictionary<String,Type> typeEnv) throws MyException;
}


