package interpreter.toylanguageinterpreter.Model.Expression;

import interpreter.toylanguageinterpreter.Model.Type.BoolType;
import interpreter.toylanguageinterpreter.Model.Type.IntType;
import interpreter.toylanguageinterpreter.Model.Type.Type;
import interpreter.toylanguageinterpreter.Model.Value.BoolValue;
import interpreter.toylanguageinterpreter.Model.Value.IntValue;
import interpreter.toylanguageinterpreter.Model.Value.Value;
import interpreter.toylanguageinterpreter.Utils.MyException;
import interpreter.toylanguageinterpreter.Utils.MyIDictionary;
import interpreter.toylanguageinterpreter.Utils.MyIHeap;

import java.util.ArrayList;
import java.util.List;


public class RelExp implements Exp{
    private final Exp e1;
    private final Exp e2;
    private final String op;

    public RelExp(Exp e1, Exp e2, String op) {
        this.e1 = e1;
        this.e2 = e2;
        this.op = op;
    }

    @Override
    public Value eval(MyIDictionary<String,Value> tbl, MyIHeap<Value> hp) throws MyException {
        Value nr1, nr2;
        nr1=e1.eval(tbl, hp);
        if(nr1.getType() instanceof IntType){
            nr2=e2.eval(tbl, hp);
            if(nr2.getType() instanceof IntType){
                IntValue i1=(IntValue)nr1;
                IntValue i2=(IntValue)nr2;
                int val1=i1.getValue();
                int val2=i2.getValue();
                switch(op){
                    case "<":
                        return new BoolValue(val1<val2);
                    case "<=":
                        return new BoolValue(val1<=val2);
                    case "==":
                        return new BoolValue(val1==val2);
                    case "!=":
                        return new BoolValue(val1!=val2);
                    case ">":
                        return new BoolValue(val1>val2);
                    case ">=":
                        return new BoolValue(val1>=val2);
                    default:
                        throw new MyException("Unexpected operand");
                }
            }
            else throw new MyException("val2 is not int");
        }
        else throw new MyException("val1 is not int");
    }

    @Override
    public Exp deepCopy() {
        return new RelExp(e1.deepCopy(), e2.deepCopy(), op);
    }

    @Override
    public Type typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type t1 = e1.typeCheck(typeEnv);
        Type t2 = e2.typeCheck(typeEnv);
        List<String> operands = new ArrayList<>();
        operands.add("<");
        operands.add("<=");
        operands.add(">");
        operands.add(">=");
        operands.add("==");
        operands.add("!=");
        if(!operands.contains(op))
            throw new MyException("Invalid operator");
        if(t1 instanceof IntType)
            if(t2 instanceof IntType)
                return new BoolType();
            else
                throw new MyException("Operand2 is not an integer");
        else
            throw new MyException("Operand1 is not an integer");
    }

    @Override
    public String toString() {
        return e1.toString() + " " + op + " " + e2.toString();
    }
}
