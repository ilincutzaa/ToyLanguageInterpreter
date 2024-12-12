package interpreter.toylanguageinterpreter.View;

import interpreter.toylanguageinterpreter.Model.Expression.*;
import interpreter.toylanguageinterpreter.Model.Statement.*;
import interpreter.toylanguageinterpreter.Model.Type.BoolType;
import interpreter.toylanguageinterpreter.Model.Type.IntType;
import interpreter.toylanguageinterpreter.Model.Type.RefType;
import interpreter.toylanguageinterpreter.Model.Type.StringType;
import interpreter.toylanguageinterpreter.Model.Value.BoolValue;
import interpreter.toylanguageinterpreter.Model.Value.IntValue;
import interpreter.toylanguageinterpreter.Model.Value.StringValue;
import interpreter.toylanguageinterpreter.TextMenu.Command.ExitCmd;
import interpreter.toylanguageinterpreter.TextMenu.Command.RunExample;
import interpreter.toylanguageinterpreter.TextMenu.TextMenu;
import interpreter.toylanguageinterpreter.Utils.MyException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Interpreter {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Input log file path:");
        String logFilePath = scanner.nextLine();


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
                new CompStmt(new AssignStmt("varf", new ValueExp(new StringValue("D:\\Java projects\\ToyLanguageCompilerPRO\\src\\test.in"))),
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
        ////int v; v="five";Print(v) SHOULD NOT LOAD
        IStmt ex14= new CompStmt(new VarDeclStmt("v",new IntType()),
                new CompStmt(new AssignStmt("v",new ValueExp(new StringValue("five"))), new PrintStmt(new VarExp("v"))));

        TextMenu menu = new TextMenu();
        try {
            FileWriter fileWriter = new FileWriter(logFilePath, false);
            fileWriter.close();
        } catch (IOException e) {
            throw new MyException(e.getMessage());
        }

        try {
            menu.addCommand(new ExitCmd("0", "exit"));
            menu.addCommand(new RunExample("1", ex1.toString(), ex1, logFilePath));
            menu.addCommand(new RunExample("2", ex2.toString(), ex2, logFilePath));
            menu.addCommand(new RunExample("3", ex3.toString(), ex3, logFilePath));
            menu.addCommand(new RunExample("4", ex4.toString(), ex4, logFilePath));
            menu.addCommand(new RunExample("5", ex5.toString(), ex5, logFilePath));
            menu.addCommand(new RunExample("6", ex6.toString(), ex6, logFilePath));
            menu.addCommand(new RunExample("7", ex7.toString(), ex7, logFilePath));
            menu.addCommand(new RunExample("8", ex8.toString(), ex8, logFilePath));
            menu.addCommand(new RunExample("9", ex9.toString(), ex9, logFilePath));
            menu.addCommand(new RunExample("10", ex10.toString(), ex10, logFilePath));
            menu.addCommand(new RunExample("11", ex11.toString(), ex11, logFilePath));
            menu.addCommand(new RunExample("12", ex12.toString(), ex12, logFilePath));
            menu.addCommand(new RunExample("13", ex13.toString(), ex13, logFilePath));
            menu.addCommand(new RunExample("14", ex14.toString(), ex14, logFilePath));

        } catch (MyException e) {
            System.out.println(e.getMessage());
        }

        menu.show();
    }
}
