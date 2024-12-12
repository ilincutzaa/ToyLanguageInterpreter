package interpreter.toylanguageinterpreter.Model.Statement;

import interpreter.toylanguageinterpreter.Model.Expression.Exp;
import interpreter.toylanguageinterpreter.Model.PrgState;
import interpreter.toylanguageinterpreter.Model.Type.StringType;
import interpreter.toylanguageinterpreter.Model.Type.Type;
import interpreter.toylanguageinterpreter.Model.Value.StringValue;
import interpreter.toylanguageinterpreter.Model.Value.Value;
import interpreter.toylanguageinterpreter.Utils.MyException;
import interpreter.toylanguageinterpreter.Utils.MyIDictionary;
import interpreter.toylanguageinterpreter.Utils.MyIFileTable;
import interpreter.toylanguageinterpreter.Utils.MyIHeap;

import java.io.BufferedReader;
import java.io.IOException;

public class CloseRFile implements IStmt {
    private final Exp exp;

    public CloseRFile(Exp exp) {
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> tbl = state.getSymTable();
        MyIFileTable<String, BufferedReader> files = state.getFileTable();
        MyIHeap<Value> heap = state.getHeapTable();
        if(exp.eval(tbl,heap).getType() instanceof StringType){
            StringValue strval = (StringValue) exp.eval(tbl,heap);
            if(!files.containsKey(strval.toString()))
                throw new MyException("File not found");
            BufferedReader reader = files.lookUp(strval.toString());
            try {
                reader.close();
            } catch (IOException e) {
                throw new MyException(e.getMessage());
            }
            files.remove(strval.toString());
        }
        else
            throw new MyException("Exp is not a string");
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new CloseRFile(exp.deepCopy());
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type texp = exp.typeCheck(typeEnv);
        if(texp instanceof StringType)
            return typeEnv;
        else
            throw new MyException("CloseRFile argument must be a string type");
    }

    @Override
    public String toString() {
        return "CloseRFile(" + exp + ')';
    }
}
