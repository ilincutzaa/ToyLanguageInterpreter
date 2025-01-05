package interpreter.toylanguageinterpreter;

import interpreter.toylanguageinterpreter.Model.Expression.ReadHeap;
import interpreter.toylanguageinterpreter.Model.Expression.ValueExp;
import interpreter.toylanguageinterpreter.Model.Expression.VarExp;
import interpreter.toylanguageinterpreter.Model.PrgState;
import interpreter.toylanguageinterpreter.Model.Statement.*;
import interpreter.toylanguageinterpreter.Model.Type.IntType;
import interpreter.toylanguageinterpreter.Model.Type.RefType;
import interpreter.toylanguageinterpreter.Model.Value.IntValue;
import interpreter.toylanguageinterpreter.Model.Value.Value;
import interpreter.toylanguageinterpreter.Repository.IRepository;
import interpreter.toylanguageinterpreter.Repository.Repository;
import interpreter.toylanguageinterpreter.Service.Service;
import interpreter.toylanguageinterpreter.Utils.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;


public class ProgramStateGuiMain extends Application {
    @Override
    public void start(Stage primaryStage){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(ProgramListGUIMain.class.getResource("ProgramStateGUI.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            //int v; Ref int a; v=10;new(a,22);
            //fork(wH(a,30);v=32;print(rH(a)));
            //print(v)
            IStmt prg = new CompStmt(new VarDeclStmt("v", new IntType()),
                    new CompStmt(new VarDeclStmt("a", new RefType(new IntType())),
                            new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(10))),
                                    new CompStmt(new AllocStmt("a", new ValueExp(new IntValue(22))),
                                            new CompStmt(new ForkStmt(new CompStmt(new WriteHeap("a", new ValueExp(new IntValue(30))),
                                                    new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(32))),
                                                            new PrintStmt(new ReadHeap(new VarExp("a")))))),
                                                    new PrintStmt(new VarExp("v")))))));

            //int v; Ref int a; v=10;new(a,22);
            //fork(wH(a,30);v=32;print(rH(a)));
            //print(v); print(v); print(v)
            IStmt prg1 = new CompStmt(new VarDeclStmt("v", new IntType()),
                    new CompStmt(new VarDeclStmt("a", new RefType(new IntType())),
                            new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(10))),
                                    new CompStmt(new AllocStmt("a", new ValueExp(new IntValue(22))),
                                            new CompStmt(new ForkStmt(new CompStmt(new WriteHeap("a", new ValueExp(new IntValue(30))),
                                                    new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(32))),
                                                            new PrintStmt(new ReadHeap(new VarExp("a")))))),
                                                    new CompStmt(new PrintStmt(new VarExp("v")),
                                                            new CompStmt(new PrintStmt(new VarExp("v")), new PrintStmt(new VarExp("v")))))))));

            IStmt ex1= new CompStmt(new VarDeclStmt("v",new IntType()),
                    new CompStmt(new AssignStmt("v",new ValueExp(new IntValue(2))), new PrintStmt(new VarExp("v"))));

            MyIStack<IStmt> stack = new MyStack<>();
            MyIDictionary<String, Value> symtbl = new MyDictionary<>();
            MyIList<Value> output = new MyList<>();
            MyIFileTable<String, BufferedReader> filetbl = new MyFileTable<>();
            MyIHeap<Value> heap = new MyHeap<>();
            PrgState prgState = new PrgState(stack, symtbl, output, prg, filetbl, heap);
            IRepository<PrgState> repo = new Repository<PrgState>("D:\\Java projects\\ToyLanguageInterpreter\\src\\main\\java\\interpreter\\toylanguageinterpreter\\log.txt");
            repo.add(prgState);
            Service serv = new Service(repo);

            ProgramStateGUIController ctr = fxmlLoader.getController();
            ctr.setService(serv);
            ctr.setCurrentProgramStateID(0);
            ctr.refreshGUI();

            primaryStage.setScene(scene);
            primaryStage.setTitle("Program State");
            primaryStage.show();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) { launch(args); }
}
