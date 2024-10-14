package org.example;

import java.util.ArrayList;
import java.util.List;

public class Player {
    public List<Card> hand;
    public int number;
    public int shields;
    public Player(int number){
        hand = new ArrayList<>();
        this.number = number;
    }
    public void draw(AdventureDeck adventureDeck){
        if (adventureDeck.getSize() == 0){
            adventureDeck.cards.addAll(adventureDeck.discards);
            adventureDeck.shuffleDeck();
        }
        Card drawnCard = adventureDeck.cards.removeFirst();
        this.hand.add(drawnCard);
    }
}
