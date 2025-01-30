package interpreter.toylanguageinterpreter.Repository;

import interpreter.toylanguageinterpreter.Model.PrgState;
import interpreter.toylanguageinterpreter.Model.Statement.IStmt;
import interpreter.toylanguageinterpreter.Model.Value.IntValue;
import interpreter.toylanguageinterpreter.Model.Value.Value;
import interpreter.toylanguageinterpreter.Utils.AtomicIntegerKey;
import interpreter.toylanguageinterpreter.Utils.MyException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class Repository<T> implements IRepository<T> {
    private final List<T> repo;
    String logFilePath;
    private static int maxIndex = 0;
    public Repository(String logFilePath) {
        repo = new ArrayList<>();
        this.logFilePath = logFilePath;
    }

    @Override
    public void add(T state) {
        repo.add(state);
        maxIndex++;
    }


    @Override
    public List<T> getPrgList() {
        return repo;
    }

    @Override
    public void setPrgList(List<T> prgList) {
        repo.clear();
        repo.addAll(prgList);
    }

    @Override
    public int getSize() {
        return repo.size();
    }

    @Override
    public void logPrgStateExec(PrgState prg) throws MyException {
        PrintWriter logFile;
        try {
            logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)));
        } catch(IOException e) {
            throw new MyException(e.getMessage());
        }
        logFile.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        logFile.println("Program Id: " + prg.getId());
        logFile.println("ExeStack:");
        List<IStmt> stmts = prg.getExeStack().getInOrderStmts();
        for (IStmt stmt : stmts) {
            if(stmt!=null)
                logFile.println(stmt);
        }
        logFile.println("\nSymTable:");
        Set<Entry<String,Value>> symbols = prg.getSymTable().entrySet();
        for (Entry<String, Value> entry : symbols) {
            logFile.println(entry.getKey() + " --> " + entry.getValue());
        }
        logFile.println("\nOut:");
        List<Value> output = prg.getOut().getList();
        for(Value v : output) {
            logFile.println(v.toString());
        }
        logFile.println("\nFileTable:");
        Set<Entry<String,BufferedReader>> files = prg.getFileTable().entrySet();
        for (Entry<String, BufferedReader> entry : files) {
            logFile.println(entry.getKey() + " --> " + entry.getValue());
        }
        logFile.println("\nHeapTable:");
        Set<Entry<AtomicIntegerKey,Value>> heap = prg.getHeapTable().entrySet();
        for (Entry<AtomicIntegerKey, Value> entry : heap) {
            logFile.println(entry.getKey() + " --> " + entry.getValue());
        }
        logFile.println("\nLatchTable:");
        Set<Entry<IntValue,IntValue>> latchtbl = prg.getLatchTable().entrySet();
        for (Entry<IntValue,IntValue> entry : latchtbl) {
            logFile.println(entry.getKey() + " --> " + entry.getValue());
        }
        logFile.println("~~~~~~~~~~~~~~~~~~~~~\n");
        logFile.close();
    }

    @Override
    public String getLogFilePath() {
        return logFilePath;
    }


}
