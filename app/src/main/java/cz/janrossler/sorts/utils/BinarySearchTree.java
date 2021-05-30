package cz.janrossler.sorts.utils;

import androidx.annotation.NonNull;

import java.util.List;

public class BinarySearchTree {
    /**
     * <p>
     *     Třída BinarySearchTree zpracovává uzly čísel. Tato třída však obsahuje 2 vnořené třídy {@link Recursive} a {@link NonRecursive},
     *     které se liší pouze způsobem zpracovávání podle rekurze a nerekurze.
     * </p>
     */
    public BinarySearchTree(){

    }

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
            BinarySearchTree.Recursive tree = new BinarySearchTree.Recursive();
            for(int i = 0; i < list.size(); i++){
                root = tree.insert(root, list.get(i));
            }
        }

        return root;
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

    public static class Recursive {
        public Node insert(Node root, int value){
            if(root == null)
                root = new Node(value);
            else if(root.getValue() > value)
                root.left = insert(root.left, value);
            else if(root.getValue() < value)
                root.right = insert(root.right, value);
            else root.add();

            return root;
        }

        public SearchResult search(Node root, int value){
            SearchResult result = new SearchResult();

            if(root == null){
                result.found = false;
            }else if(root.getValue() > value){
                result = search(root.left, value);
            }else if(root.getValue() < value){
                result = search(root.right, value);
            }else{
                result.found = true;
                result.value = root.getValue();
                result.amount  = root.getAmount();
            }
            return result;
        }
    }

    public static class NonRecursive {
        public void insert(Node root, int value){
            boolean end = false;
            while (!end) {
                if (root == null) {
                    root = new Node(value);
                    end = true;
                } else if (root.getValue() > value)
                    root = root.left;
                else if (root.getValue() < value)
                    root = root.right;
                else {
                    root.add();
                    end = true;
                }
            }
        }

        public SearchResult search(Node root, int value){
            SearchResult result = new SearchResult();

            boolean end = false;
            while (!end){
                if(root == null){
                    result.found = false;
                    end = true;
                }else if(root.getValue() > value){
                    root = root.left;
                }else if(root.getValue() < value){
                    root = root.right;
                }else{
                    result.found = true;
                    result.value = root.getValue();
                    result.amount  = root.getAmount();
                    end = true;
                }
            }

            return result;
        }
    }
}
