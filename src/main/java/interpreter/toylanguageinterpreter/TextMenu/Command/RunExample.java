package interpreter.toylanguageinterpreter.TextMenu.Command;

import interpreter.toylanguageinterpreter.Service.Service;
import interpreter.toylanguageinterpreter.Model.PrgState;
import interpreter.toylanguageinterpreter.Model.Statement.IStmt;
import interpreter.toylanguageinterpreter.Model.Type.Type;
import interpreter.toylanguageinterpreter.Model.Value.Value;
import interpreter.toylanguageinterpreter.Repository.IRepository;
import interpreter.toylanguageinterpreter.Repository.Repository;
import interpreter.toylanguageinterpreter.Utils.*;

import java.io.BufferedReader;

public class RunExample extends Command {
    private final Service ctr;

    public RunExample(String key, String description, IStmt prg,String logFilePath) {
        super(key, description);
        MyIDictionary<String, Type> typeEnv = new MyDictionary<>();
        try {
            prg.typeCheck(typeEnv);
            MyIStack<IStmt> stack = new MyStack<>();
            MyIDictionary<String, Value> symtbl = new MyDictionary<>();
            MyIList<Value> output = new MyList<>();
            MyIFileTable<String, BufferedReader> filetbl = new MyFileTable<>();
            MyIHeap<Value> heap = new MyHeap<>();
            PrgState prgState = new PrgState(stack, symtbl, output, prg, filetbl, heap);
            IRepository<PrgState> repo = new Repository<PrgState>(logFilePath);
            repo.add(prgState);
            this.ctr = new Service(repo);
        } catch (MyException e) {
            throw new MyException("TypeCheck Error in Program " + key + " " +e.getMessage() + " -> The program will not load");
        }
    }

    @Override
    public void execute() {
        try{
            ctr.allStep();
        } catch (MyException e){
            System.out.println("An exception has occurred: " + e.getMessage());
        }
    }
}
