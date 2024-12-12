package interpreter.toylanguageinterpreter.TextMenu.Command;

public class ExitCmd extends Command {
    public ExitCmd(String key, String description) {
        super(key, description);
    }

    @Override
    public void execute() {
        System.out.println("Bye!");
        System.exit(0);
    }
}
