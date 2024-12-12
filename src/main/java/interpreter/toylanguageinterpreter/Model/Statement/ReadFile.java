package interpreter.toylanguageinterpreter.Model.Statement;

import interpreter.toylanguageinterpreter.Model.Expression.Exp;
import interpreter.toylanguageinterpreter.Model.PrgState;
import interpreter.toylanguageinterpreter.Model.Type.IntType;
import interpreter.toylanguageinterpreter.Model.Type.StringType;
import interpreter.toylanguageinterpreter.Model.Type.Type;
import interpreter.toylanguageinterpreter.Model.Value.IntValue;
import interpreter.toylanguageinterpreter.Model.Value.StringValue;
import interpreter.toylanguageinterpreter.Model.Value.Value;
import interpreter.toylanguageinterpreter.Utils.MyException;
import interpreter.toylanguageinterpreter.Utils.MyIDictionary;
import interpreter.toylanguageinterpreter.Utils.MyIFileTable;
import interpreter.toylanguageinterpreter.Utils.MyIHeap;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadFile implements IStmt{
    private final Exp exp;
    private final String varName;

    public ReadFile(Exp exp, String var_name) {
        this.exp = exp;
        this.varName = var_name;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> tbl = state.getSymTable();
        MyIFileTable<String, BufferedReader> files = state.getFileTable();
        MyIHeap<Value> heap = state.getHeapTable();

        if(tbl.lookUp(varName) != null){
            if(tbl.lookUp(varName).getType() instanceof IntType){
                StringValue strval = (StringValue) exp.eval(tbl,heap);
                if(!files.containsKey(strval.toString()))
                    throw new MyException("File not found");
                BufferedReader reader = files.lookUp(strval.toString());
                String line;
                try {
                    line = reader.readLine();
                } catch (IOException e) {
                    throw new MyException(e.getMessage());
                }
                IntValue val;
                if(line == null)
                    val = new IntValue(0);
                else
                    val = new IntValue(Integer.parseInt(line));
                tbl.update(varName, val);
            }
            else
                throw new MyException("Variable '" + varName + "' is not int");
        }
        else
            throw new MyException("Variable '" + varName + "' not found");
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new ReadFile(exp.deepCopy(), varName);
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type texp = exp.typeCheck(typeEnv);
        if(texp instanceof StringType)
            return typeEnv;
        else
            throw new MyException("ReadFile first argument must be a StringType");
    }

    @Override
    public String toString() {
        return "ReadFile(" + exp + "," + varName + ")";
    }
}
