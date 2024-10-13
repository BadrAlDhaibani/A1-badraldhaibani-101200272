package org.example;

import java.util.ArrayList;
import java.util.List;

public class Player {
    public List<Card> hand;
    public int number;
    public Player(int number){
        hand = new ArrayList<>();
        this.number = number;
    }
}
