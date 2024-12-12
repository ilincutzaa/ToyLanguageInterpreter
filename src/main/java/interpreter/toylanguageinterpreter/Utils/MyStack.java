package interpreter.toylanguageinterpreter.Utils;

import interpreter.toylanguageinterpreter.Model.Statement.CompStmt;
import interpreter.toylanguageinterpreter.Model.Statement.IStmt;
import interpreter.toylanguageinterpreter.Model.Statement.NopStmt;

import java.util.*;
import java.util.stream.Collectors;

public class MyStack<T> implements MyIStack<T>{
    private final Stack<T> stack;
    private final MyTree<T> tree;

    public MyStack() {
        stack = new Stack<>();
        tree = new MyTree<>();
    }

    private Node<T> toTree(IStmt stmt){
        Node<IStmt> node;
        if(stmt instanceof CompStmt){
            CompStmt compStmt = (CompStmt) stmt;
            node = new Node<>(new NopStmt());
            node.setLeft(toTree(compStmt.getFirst()));
            node.setRight(toTree(compStmt.getSecond()));
        }
        else
            node = new Node<>(stmt);
        return (Node<T>) node;
    }

    private void removeTree(){
        if(stack.isEmpty()){
            tree.setRoot(null);
            return;
        }
        IStmt stmt = (IStmt) stack.pop();
        IStmt compStmt = stmt;
        while(!stack.isEmpty()){
            compStmt = new CompStmt(compStmt, (IStmt) stack.pop());
        }
        stack.push((T) compStmt);
        tree.setRoot(toTree((IStmt) stack.getFirst()));
    }

    public List<T> getInOrderStmts(){
        List<T> inOrderList = new LinkedList<>();
        tree.inOrderTraversal(inOrderList, tree.getRoot());
        return inOrderList.stream().filter(v -> !(v instanceof NopStmt)).collect(Collectors.toList());
    }

    private void addTree(T t){
        Node<T> newRoot = (Node<T>) new Node<>(new NopStmt());
        newRoot.setLeft(toTree((IStmt) t));
        newRoot.setRight(tree.getRoot());
        tree.setRoot(newRoot);
    }

    @Override
    public void push(T t) {
        stack.push(t);
        addTree(t);
    }

    @Override
    public T pop() throws MyException {
        if(stack.isEmpty())
            throw new MyException("Stack is empty");
        T top = stack.pop();
        removeTree();
        return top;
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public List<T> toList() {
        return (List<T>)Arrays.asList(stack.toArray());
    }

    @Override
    public String toString() {
        return stack.toString();
    }
}
