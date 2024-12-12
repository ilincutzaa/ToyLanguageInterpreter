package interpreter.toylanguageinterpreter.TextMenu;

import interpreter.toylanguageinterpreter.TextMenu.Command.Command;
import interpreter.toylanguageinterpreter.Utils.MyDictionary;
import interpreter.toylanguageinterpreter.Utils.MyIDictionary;
import java.util.Scanner;


public class TextMenu {
    private final MyIDictionary<String, Command> commands;

    public TextMenu() {
        commands = new MyDictionary<>();
    }
    public void addCommand(Command c) {
        commands.put(c.getKey(),c);
    }
    private void printMenu(){
        commands.keySet().stream()
                .map(Integer::parseInt)
                .sorted()
                .forEach(key->{
                    Command cmd = commands.lookUp(key.toString());
                    String line = String.format("%4d:\n%s\n",key,cmd.getDescription());
                    System.out.println(line);
                });
    }
    public void show(){
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.print("~~~~MENU~~~~~\n");
            printMenu();
            System.out.print("Input the option: ");
            String key = scanner.nextLine();
            Command cmd = commands.lookUp(key);
            if(cmd == null){
                System.out.println("Invalid option");
                continue;
            }
            System.out.print("~~~~~~~~~~~~");
            System.out.println();
            cmd.execute();
            System.out.println();
        }
    }
}
