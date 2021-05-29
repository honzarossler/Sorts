package cz.janrossler.sorts.utils;

import java.io.IOException;
import java.io.UncheckedIOException;

public class Node {
    public Node left;
    public Node right;

    private int value;
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

    public boolean isEmpty(){
        return amount > 0;
    }

    public void add(){
        amount++;
    }

    public void remove(){
        if(amount > 0)
            amount--;
        else
            throw new UncheckedIOException(new IOException("Node is empty."));
    }
}
