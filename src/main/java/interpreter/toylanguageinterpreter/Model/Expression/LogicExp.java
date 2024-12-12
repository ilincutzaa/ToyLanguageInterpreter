package interpreter.toylanguageinterpreter.Model.Expression;

import interpreter.toylanguageinterpreter.Model.Type.Type;
import interpreter.toylanguageinterpreter.Utils.MyIDictionary;
import interpreter.toylanguageinterpreter.Model.Type.BoolType;
import interpreter.toylanguageinterpreter.Model.Value.BoolValue;
import interpreter.toylanguageinterpreter.Model.Value.Value;
import interpreter.toylanguageinterpreter.Utils.MyException;
import interpreter.toylanguageinterpreter.Utils.MyIHeap;

import java.util.ArrayList;
import java.util.List;

public class LogicExp implements Exp{
    private final Exp e1;
    private final Exp e2;
    private final String op;
    
    public LogicExp(String op, Exp e1, Exp e2) {
        this.e1 = e1;
        this.e2 = e2;
        this.op = op;
    }
    
    public Value eval(MyIDictionary<String,Value> tbl, MyIHeap<Value> hp) throws MyException {
        Value nr1, nr2;
        nr1 = e1.eval(tbl, hp);
        if(nr1.getType() instanceof BoolType){
            nr2 = e2.eval(tbl, hp);
            if(nr2.getType() instanceof BoolType){
                BoolValue b1 = (BoolValue)nr1;
                BoolValue b2 = (BoolValue)nr2;
                boolean val1 = b1.getValue();
                boolean val2 = b2.getValue();
                if(op.equals("and") || op.equals("&&"))
                    return new BoolValue(val1 && val2);
                else if(op.equals("or") || op.equals("||"))
                    return new BoolValue(val1 || val2);
                else
                    throw new MyException("Wrong operator");
            }
            else 
                throw new MyException("operand2 is not boolean");
        }
        else 
            throw new MyException("operand1 is not boolean");
    }

    @Override
    public Exp deepCopy() {
        return new LogicExp(op, e1.deepCopy(), e2.deepCopy());
    }

    @Override
    public Type typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type t1 = e1.typeCheck(typeEnv);
        Type t2 = e2.typeCheck(typeEnv);
        List<String> operands = new ArrayList<>();
        operands.add("&&");
        operands.add("and");
        operands.add("||");
        operands.add("or");
        if(!operands.contains(op))
            throw new MyException("Wrong operator");
        if(t1 instanceof BoolType)
            if(t2 instanceof BoolType)
                return new BoolType();
            else
                throw new MyException("Operand2 is not boolean");
        else
            throw new MyException("Operand1 is not boolean");
    }

    @Override
    public String toString() {
        return e1.toString() + " " + op + " " + e2.toString();
    }
}
