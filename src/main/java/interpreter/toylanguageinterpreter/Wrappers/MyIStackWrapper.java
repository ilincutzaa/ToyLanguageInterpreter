package interpreter.toylanguageinterpreter.Wrappers;
import interpreter.toylanguageinterpreter.Model.Statement.IStmt;
import interpreter.toylanguageinterpreter.Utils.MyException;
import interpreter.toylanguageinterpreter.Utils.MyIStack;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class MyIStackWrapper<T> {
    private final MyIStack<T> stack;
    private final ObservableList<T> observableStack;

    public MyIStackWrapper(MyIStack<T> stack) {
        this.stack = stack;
        this.observableStack = FXCollections.observableArrayList(stack.getInOrderStmts());
    }

    public void push(T stmt) {
        stack.push(stmt);
        refreshObservableStack();
    }

    public T pop() throws MyException {
        T stmt = stack.pop();
        refreshObservableStack();
        return stmt;
    }

    public ObservableList<T> getObservableStack() {
        return observableStack;
    }

    private void refreshObservableStack() {
        observableStack.setAll(stack.getInOrderStmts());
    }
}
