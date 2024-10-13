package org.example;

public class Card {
    private String type;
    private int value;

    public Card(String type, int value){
        this.type = type;
        this.value = value;
    }
    public String getType(){
        return type;
    }
    public int getValue(){
        return value;
    }
}
