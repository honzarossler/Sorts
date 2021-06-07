package cz.janrossler.sorts.utils;

import androidx.annotation.NonNull;

import org.json.JSONArray;

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

    @SuppressWarnings("rawtypes")
    public static <T> Node createFromList(@NonNull T list){
        Node root = null;

        try{
            BinarySearchTree tree = new BinarySearchTree();
            if(list instanceof List){
                List l = (List) list;
                for(int i = 0; i < l.size(); i++){
                    root = tree.insert(root, (Integer) l.get(i));
                }
            }else if(list instanceof JSONArray){
                JSONArray a = (JSONArray) list;
                for(int i = 0; i < a.length(); i++){
                    root = tree.insert(root, a.getInt(i));
                }
            }else if(list instanceof Integer[]){
                Integer[] num = (Integer[]) list;
                for (Integer i : num) {
                    root = tree.insert(root, i);
                }
            }else throw new Exception("Imcompatible type.");
        } catch (OutOfMemoryError | Exception e){
            e.printStackTrace();
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
        result.value = value;

        if(root == null)
            result.found = false;
        else if(root.getValue() > value)
            result = search(root.left, value);
        else if(root.getValue() < value)
            result = search(root.right, value);
        else{
            result.found = true;
            result.amount = root.getAmount();
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
