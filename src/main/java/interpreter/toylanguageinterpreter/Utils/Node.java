package interpreter.toylanguageinterpreter.Utils;

public class Node<T> {
    T value;
    Node left;
    Node right;
    public Node(T value) {
        this.value = value;
        left = null;
        right = null;
    }

    public T getValue() {
        return value;
    }


    public void setValue(T value) {
        this.value = value;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
