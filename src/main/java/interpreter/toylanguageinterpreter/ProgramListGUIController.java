package interpreter.toylanguageinterpreter;

import interpreter.toylanguageinterpreter.Model.Expression.*;
import interpreter.toylanguageinterpreter.Model.PrgState;
import interpreter.toylanguageinterpreter.Model.Statement.*;
import interpreter.toylanguageinterpreter.Model.Type.*;
import interpreter.toylanguageinterpreter.Model.Value.BoolValue;
import interpreter.toylanguageinterpreter.Model.Value.IntValue;
import interpreter.toylanguageinterpreter.Model.Value.StringValue;
import interpreter.toylanguageinterpreter.Model.Value.Value;
import interpreter.toylanguageinterpreter.Repository.IRepository;
import interpreter.toylanguageinterpreter.Repository.Repository;
import interpreter.toylanguageinterpreter.Service.Service;
import interpreter.toylanguageinterpreter.Utils.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.IOException;


public class ProgramListGUIController {
    @FXML
    private ListView<IStmt> programListView;

    public void setLogFilePath(String logFilePath) {
        this.logFilePath = logFilePath;
    }

    private String logFilePath;


    @FXML
    protected void onLoadButtonClicked(){
        IStmt prg = programListView.getSelectionModel().getSelectedItem();
        MyIDictionary<String, Type> typeEnv = new MyDictionary<>();
        try {
            prg.typeCheck(typeEnv);
            MyIStack<IStmt> stack = new MyStack<>();
            MyIDictionary<String, Value> symtbl = new MyDictionary<>();
            MyIList<Value> output = new MyList<>();
            MyIFileTable<String, BufferedReader> filetbl = new MyFileTable<>();
            MyIHeap<Value> heap = new MyHeap<>();
            PrgState prgState = new PrgState(stack, symtbl, output, prg, filetbl, heap);
            IRepository<PrgState> repo = new Repository<>(logFilePath);
            repo.add(prgState);
            Service serv = new Service(repo);

            FXMLLoader fxmlLoader = new FXMLLoader(ProgramListGUIMain.class.getResource("ProgramStateGUI.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            ProgramStateGUIController ctr = fxmlLoader.getController();

            ctr.setService(serv);
            ctr.setCurrentProgramStateID(serv.getPrgIds().getFirst());
            ctr.refreshGUI();

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Program State");
            stage.show();


        } catch (MyException e) {
            errorAlert("Type Checker returned an error: " + e.getMessage());
        } catch (IOException e) {
            errorAlert(e.getMessage());
        }
    }

    private void errorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void initialize() {
        programListView.setItems(getProgramList());
        programListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        programListView.getSelectionModel().selectIndices(0);
        programListView.getFocusModel().focus(0);
    }



    private ObservableList<IStmt> getProgramList() {
        //int v; v=2;Print(v) is represented as:
        IStmt ex1= new CompStmt(new VarDeclStmt("v",new IntType()),
                new CompStmt(new AssignStmt("v",new ValueExp(new IntValue(2))), new PrintStmt(new VarExp("v"))));

        //int a;int b; a=2+3*5;b=a+1;Print(b) is represented as:
        IStmt ex2 = new CompStmt( new VarDeclStmt("a",new IntType()),
                new CompStmt(new VarDeclStmt("b",new IntType()),
                        new CompStmt(new AssignStmt("a", new ArithExp('+',new ValueExp(new IntValue(2)),new ArithExp('*',new ValueExp(new IntValue(3)), new ValueExp(new IntValue(5))))),
                                new CompStmt(new AssignStmt("b",new ArithExp('+',new VarExp("a"), new ValueExp(new IntValue(1)))), new PrintStmt(new VarExp("b"))))));


        //bool a; int v; a=true;(If a Then v=2 Else v=3);Print(v) is represented as
        IStmt ex3 = new CompStmt(new VarDeclStmt("a",new BoolType()),
                new CompStmt(new VarDeclStmt("v", new IntType()),
                        new CompStmt(new AssignStmt("a", new ValueExp(new BoolValue(true))),
                                new CompStmt(new IfStmt(new VarExp("a"),new AssignStmt("v",new ValueExp(new IntValue(2))), new AssignStmt("v", new ValueExp(new IntValue(3)))), new PrintStmt(new VarExp("v"))))));

        //string varf;
        //varf="test.in";
        //openRFile(varf);
        //int varc;
        //readFile(varf,varc);print(varc);
        //readFile(varf,varc);print(varc)
        //closeRFile(varf)
        IStmt ex4 = new CompStmt(new VarDeclStmt("varf", new StringType()),
                new CompStmt(new AssignStmt("varf", new ValueExp(new StringValue("src/main/java/interpreter/toylanguageinterpreter/test.in"))),
                        new CompStmt(new OpenRFile(new VarExp("varf")),
                                new CompStmt(new VarDeclStmt("varc", new IntType()),
                                        new CompStmt(new ReadFile(new VarExp("varf"), "varc"),
                                                new CompStmt(new PrintStmt(new VarExp("varc")),
                                                        new CompStmt(new ReadFile(new VarExp("varf"), "varc"),
                                                                new CompStmt(new PrintStmt(new VarExp("varc")),
                                                                        new CloseRFile(new VarExp("varf"))))))))));

        //Ref int v;new(v,20);Ref Ref int a; new(a,v);print(v);print(a)
        IStmt ex5 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(new AllocStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompStmt(new AllocStmt("a", new VarExp("v")),
                                        new CompStmt(new PrintStmt(new VarExp("v")),
                                                new PrintStmt(new VarExp("a")))))));

        //Ref int v;new(v,20);Ref Ref int a; new(a,v);print(rH(v));print(rH(rH(a))+5)
        IStmt ex6 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(new AllocStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompStmt(new AllocStmt("a", new VarExp("v")),
                                        new CompStmt(new PrintStmt(new ReadHeap(new VarExp("v"))),
                                                new PrintStmt(new ArithExp('+', new ReadHeap(new ReadHeap(new VarExp("a"))), new ValueExp(new IntValue(5)))))))));

        //Ref int v;new(v,20);print(rH(v)); wH(v,30);print(rH(v)+5);
        IStmt ex7 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(new AllocStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(new PrintStmt(new ReadHeap(new VarExp("v"))),
                                new CompStmt(new WriteHeap("v", new ValueExp(new IntValue(30))),
                                        new PrintStmt(new ArithExp('+', new ReadHeap(new VarExp("v")), new ValueExp(new IntValue(5))))))));

        //Ref int v;new(v,20);Ref Ref int a; new(a,v); new(v,30);print(rH(rH(a)))
        IStmt ex8 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(new AllocStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompStmt(new AllocStmt("a", new VarExp("v")),
                                        new CompStmt(new AllocStmt("v", new ValueExp(new IntValue(30))),
                                                new PrintStmt(new ReadHeap(new ReadHeap(new VarExp("a")))))))));

        //Ref int v;new(v,20);Ref Ref int a; new(a,v); Ref Ref Ref int b; new(b,a);
        IStmt ex9 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(new AllocStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompStmt(new AllocStmt("a", new VarExp("v")),
                                        new CompStmt(new VarDeclStmt("b",new RefType(new RefType(new RefType(new IntType())))),
                                                new AllocStmt("b", new VarExp("a")))))));

        //int v; v=4; (while (v>0) print(v);v=v-1);print(v)
        IStmt ex10 = new CompStmt(new VarDeclStmt("v", new IntType()),
                new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(4))),
                        new CompStmt(new WhileStmt(new RelExp(new VarExp("v"), new ValueExp(new IntValue(0)),">"),
                                new CompStmt(new PrintStmt(new VarExp("v")),new AssignStmt("v", new ArithExp('-', new VarExp("v"),new ValueExp(new IntValue(1)))))),
                                new PrintStmt(new VarExp("v")))));

        //Ref int v;new(v,20);Ref Ref int a; new(a,v); new(v,30); new(a,v);
        IStmt ex11 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(new AllocStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompStmt(new AllocStmt("a", new VarExp("v")),
                                        new CompStmt(new AllocStmt("v", new ValueExp(new IntValue(30))),
                                                new AllocStmt("a", new VarExp("v")))))));

        //int v; Ref int a; v=10;new(a,22);
        //fork(wH(a,30);v=32;print(v);print(rH(a)));
        //print(v);print(rH(a))
        IStmt ex12 = new CompStmt(new VarDeclStmt("v", new IntType()),
                new CompStmt(new VarDeclStmt("a", new RefType(new IntType())),
                        new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(10))),
                                new CompStmt(new AllocStmt("a", new ValueExp(new IntValue(22))),
                                        new CompStmt(new ForkStmt(new CompStmt(new WriteHeap("a", new ValueExp(new IntValue(30))),
                                                new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(32))),
                                                        new CompStmt(new PrintStmt(new VarExp("v")), new PrintStmt(new ReadHeap(new VarExp("a"))))))),
                                                new CompStmt(new PrintStmt(new VarExp("v")), new PrintStmt(new ReadHeap(new VarExp("a")))))))));

        //Ref int v;new(v,20);Ref Ref int a; new(a,v); new(v,30); new(a,v);
        IStmt ex13 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(new AllocStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompStmt(new AllocStmt("a", new VarExp("v")),
                                        new CompStmt(new AllocStmt("v", new ValueExp(new IntValue(30))),
                                                new CompStmt(new AllocStmt("a", new VarExp("v")),
                                                        new PrintStmt(new ReadHeap(new VarExp("v")))))))));
        //int v; v="five";Print(v) SHOULD NOT BE ABLE TO RUN
        IStmt ex14= new CompStmt(new VarDeclStmt("v",new IntType()),
                new CompStmt(new AssignStmt("v",new ValueExp(new StringValue("five"))), new PrintStmt(new VarExp("v"))));

        return FXCollections.observableArrayList(ex1, ex2, ex3, ex4, ex5, ex6, ex7, ex8, ex9, ex10, ex11, ex12, ex13, ex14);
    }

}
