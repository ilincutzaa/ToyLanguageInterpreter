package interpreter.toylanguageinterpreter.Model;

import interpreter.toylanguageinterpreter.Model.Statement.IStmt;
import interpreter.toylanguageinterpreter.Model.Value.IntValue;
import interpreter.toylanguageinterpreter.Model.Value.Value;
import interpreter.toylanguageinterpreter.Utils.*;

import java.io.BufferedReader;

public class PrgState {
    private final MyIStack<IStmt> exeStack;
    private final MyIDictionary<String, Value> symTable;
    private final MyIList<Value> out;
    private final IStmt originalProgram;
    private final MyIFileTable<String, BufferedReader> fileTable;
    private final MyIHeap<Value> heapTable;
    private final MyICountDownLatch<IntValue> latchTable;
    private final int id;

    private static int currentId = 0;

    public static synchronized void incCurrentId(){
        currentId++;
    }

    public static synchronized void decCurrentId(){
        currentId--;
    }

    public int getId(){
        return id;
    }

    public MyIFileTable<String, BufferedReader> getFileTable() {
        return fileTable;
    }


    public MyIStack<IStmt> getExeStack() {
        return exeStack;
    }

    public MyIDictionary<String, Value> getSymTable() {
        return symTable;
    }

    public MyIList<Value> getOut() {
        return out;
    }

    public IStmt getOriginalProgram() {
        return originalProgram;
    }

    public PrgState(MyIStack<IStmt> exeStack, MyIDictionary<String,Value> symTable, MyIList<Value> out, IStmt originalProgram, MyIFileTable<String, BufferedReader> fileTable, MyIHeap<Value> heapTable, MyICountDownLatch<IntValue> latchTable){
        this.exeStack=exeStack;
        this.symTable=symTable;
        this.out = out;
        this.originalProgram=originalProgram.deepCopy();
        this.fileTable = fileTable;
        this.heapTable = heapTable;
        this.latchTable = latchTable;
        exeStack.push(originalProgram);
        this.id = currentId;
        incCurrentId();
    }

    @Override
    public String toString() {
        return "Program Id: " + id + "Execution stack: "+exeStack.toString()+" Symbol table: "+symTable.toString()+" Out: "+out.toString()+" Original: "+originalProgram.toString()+" File table: "+fileTable.toString();
    }

    public MyIHeap<Value> getHeapTable() {
        return heapTable;
    }

    public boolean isNotCompleted(){
        return !exeStack.isEmpty();
    }

    public MyICountDownLatch<IntValue> getLatchTable() {
        return latchTable;
    }

    public PrgState oneStep() throws MyException {
        if(exeStack.isEmpty()) throw new MyException("prgstate stack is empty");
        IStmt crtStmt = exeStack.pop();
        return crtStmt.execute(this);
    }

}

