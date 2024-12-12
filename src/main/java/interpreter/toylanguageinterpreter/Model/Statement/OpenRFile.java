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
import java.io.FileReader;
import java.io.IOException;


public class OpenRFile implements IStmt{
    private final Exp exp;

    public OpenRFile(Exp exp) {
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> tbl = state.getSymTable();
        MyIFileTable<String, BufferedReader> files = state.getFileTable();
        MyIHeap<Value> heap = state.getHeapTable();

        Value val = exp.eval(tbl,heap);
        if(val.getType() instanceof StringType){
            StringValue strval = (StringValue)val;
            if(!files.containsKey(strval.toString())){
                BufferedReader reader;
                try{
                    reader = new BufferedReader(new FileReader(strval.toString()));
                } catch (IOException e){
                    throw new MyException(e.getMessage());
                }
                files.put(strval.toString(),reader);
            }
            else
                throw new MyException("file with the same name already opened");
        }
        else
            throw new MyException("exp is not stringType");
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new OpenRFile(exp.deepCopy());
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type texp = exp.typeCheck(typeEnv);
        if(texp instanceof StringType)
            return typeEnv;
        else
            throw new MyException("OpenRFile argument must be a string type");
    }

    @Override
    public String toString() {
        return "OpenRFile(" + exp + ")";
    }
}
