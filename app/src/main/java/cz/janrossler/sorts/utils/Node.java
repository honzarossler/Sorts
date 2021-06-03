package cz.janrossler.sorts.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Node {
    public Node left;
    public Node right;

    private final int value;
    private int amount = 0;

    public Node(int value){
        this.value = value;
        amount++;
    }

    public int getValue(){
        return value;
    }

    public int getAmount(){
        return amount;
    }

    public void increase(){
        amount++;
    }

    public void decrease(){
        if(amount > 0) amount--;
        else throw new RuntimeException(new IOException("Node is empty."));
    }

    public List<Integer> toList(){
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < amount; i++)
            list.add(getValue());

        if(left != null) list.addAll(left.toList());
        if(right != null) list.addAll(right.toList());

        return list;
    }

    public int size(){
        int size = amount;

        if(left != null) size += left.size();
        if(right != null) size += right.size();

        return size;
    }
}
