package interpreter.toylanguageinterpreter.Model.Expression;

import interpreter.toylanguageinterpreter.Model.Type.IntType;
import interpreter.toylanguageinterpreter.Model.Type.Type;
import interpreter.toylanguageinterpreter.Model.Value.IntValue;
import interpreter.toylanguageinterpreter.Model.Value.Value;
import interpreter.toylanguageinterpreter.Utils.MyException;
import interpreter.toylanguageinterpreter.Utils.MyIDictionary;
import interpreter.toylanguageinterpreter.Utils.MyIHeap;

public class ArithExp implements Exp{
    private final Exp e1;
    private final Exp e2;
    private final char op;

    public ArithExp(char op, Exp e1, Exp e2) {
        this.e1 = e1;
        this.e2 = e2;
        this.op = op;
    }

    public Value eval(MyIDictionary<String,Value> tbl, MyIHeap<Value> hp) throws MyException {
        Value v1, v2;
        v1 = e1.eval(tbl, hp);
        if(v1.getType() instanceof IntType) {
            v2 = e2.eval(tbl, hp);
            if (v2.getType() instanceof IntType) {
                IntValue i1 = (IntValue) v1;
                IntValue i2 = (IntValue) v2;
                int n1, n2;
                n1 = i1.getValue();
                n2 = i2.getValue();
                return switch (op) {
                    case '+' -> new IntValue(n1 + n2);
                    case '-' -> new IntValue(n1 - n2);
                    case '*' -> new IntValue(n1 * n2);
                    case '/' -> {
                        if (n2 == 0) throw new MyException("division by zero");
                        yield new IntValue(n1 / n2);
                    }
                    default -> throw new MyException("Unexpected operand: " + op);
                };
            }
            else 
                throw new MyException("second operand is not an integer, but a" + v2.getType());
        }
        else 
            throw new MyException("first operand is not an integer, but a" + v1.getType());
    }

    @Override
    public Exp deepCopy() {
        return new ArithExp(op, e1.deepCopy(), e2.deepCopy());
    }

    @Override
    public Type typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type t1,t2;
        t1 = e1.typeCheck(typeEnv);
        t2 = e2.typeCheck(typeEnv);
        if(t1 instanceof IntType)
            if(t2 instanceof IntType)
                return new IntType();
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
