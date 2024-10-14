package org.example;

public class Card {
    private final String type;
    private final int value;
    private String label; //to differentiate from sword and horse

    public Card(String type, int value){
        this.type = type;
        this.value = value;
    }
    //For Weapons
    public Card(String type, int value, String label){
        this.type = type;
        this.value = value;
        this.label = label;
    }
    public String getType(){
        return type;
    }
    public int getValue(){
        return value;
    }
    public String getLabel(){
        return label;
    }
    public String toString() {
        if (label != null) {
            return label + " (Value: " + value + ")";
        } else {
            return type.charAt(0) + String.valueOf(value);
        }
    }
}
