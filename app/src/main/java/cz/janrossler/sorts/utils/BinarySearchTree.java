package cz.janrossler.sorts.utils;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class BinarySearchTree {
    /**
     * <p>
     *     Třída BinarySearchTree zpracovává uzly čísel.
     * </p>
     */
    public BinarySearchTree(){}

    /**
     * <p>
     *     Přijímá {@link List<Integer>} a ten převádí do {@link Node}, který je pro {@link BinarySearchTree} snadno čitelný.
     * </p>
     * @param list Seznam čísel typu {@link Integer}, které budou převedeny do {@link Node}.
     * @return Vrací {@link Node}.
     */

    public static Node createFromList(@NonNull List<Integer> list){
        Node root = null;

        if(list.size() <= Utilities.MAX_TREE_SIZE){
            BinarySearchTree tree = new BinarySearchTree();
            for(int i = 0; i < list.size(); i++){
                root = tree.insert(root, list.get(i));
            }
        }

        return root;
    }

    public Node insert(Node root, int value){
        if(root == null)
            root = new Node(value);
        else if(root.getValue() > value)
            root.left = insert(root.left, value);
        else if(root.getValue() < value)
            root.right = insert(root.right, value);
        else root.increase();
        return root;
    }

    public SearchResult search(Node root, int value){
        SearchResult result = new SearchResult();

        if(root == null)
            result.found = false;
        else if(root.getValue() > value)
            result = search(root.left, value);
        else if(root.getValue() < value)
            result = search(root.right, value);
        else{
            result.found = true;
            result.value = root.getValue();
            result.amount  = root.getAmount();
        }
        return result;
    }

    public Node remove(Node root, int value){
        if(root == null){
            return null;
        }else if(root.getValue() == value && root.getAmount() > 1)
            root.decrease();
        else if(root.getValue() == value && root.getAmount() <= 1)
            if (root.left != null || root.right != null)
                root = merge(root.left, root.right);
            else root = null;
        else if(root.getValue() > value)
            root.left = remove(root.left, value);
        else if(root.getValue() < value)
            root.right = remove(root.right, value);
        return root;
    }

    public Node merge(Node left, Node right){
        Node node = null;
        List<Integer> list = new ArrayList<>();
        if(left != null && right != null){
            list = left.getValue() > right.getValue()
                    ? right.toList() : left.toList();
            node = left.getValue() > right.getValue() ? left : right;
        }else if(left != null){
            list = left.toList();
        }else if(right != null){
            list = right.toList();
        }

        for(int i = 0; i < list.size(); i++){
            node = insert(node, list.get(i));
        }

        return node;
    }

    /**
     * <p>
     *     Současně obsahuje {@link SearchResult}, který obsahuje podrobnější informace o nalezených číslech v uzlech.
     * </p>
     */

    public static class SearchResult{
        public boolean found = false;
        public int value = 0;
        public int amount = 0;
    }
}
