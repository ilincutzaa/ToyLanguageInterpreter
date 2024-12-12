package interpreter.toylanguageinterpreter.Utils;

import java.util.ArrayList;
import java.util.List;

public class MyTree<T> {
    private Node<T> root;

    public MyTree() {
        root = null;
    }

    public Node<T> getRoot() {
        return root;
    }

    public void setRoot(Node<T> root) {
        this.root = root;
    }

    public void inOrderTraversal(List<T> list, Node node){
        if(node == null)
            return;
        inOrderTraversal(list, node.left);
        list.add((T)node.value);
        inOrderTraversal(list, node.right);
    }

    public boolean isEmpty(){
        return root == null;
    }


    @Override
    public String toString() {
        List<T> list = new ArrayList<T>();
        inOrderTraversal(list, root);
        return list.toString();
    }
}
