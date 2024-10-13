package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventDeck {
    public List<Card> cards;
    public List<Card> discards;

    public EventDeck(){
        cards = new ArrayList<>();
        discards = new ArrayList<>();
        for (int i = 0; i < 12; i++){
            if(i < 3){
                cards.add(new Card("Quest",2));
            }
            else if(i < 7){
                cards.add(new Card("Quest",3));
            }
            else if(i < 10){
                cards.add(new Card("Quest",4));
            }
            else if(i < 12){
                cards.add(new Card("Quest",5));
            }
        }
        for (int i = 12; i < 17; i++){
            if(i < 13){ //value 1 for Plague
                cards.add(new Card("Event",1));
            }
            else if(i < 15){ //value 1 for Plague
                cards.add(new Card("Event",2));
            }
            else if(i < 17){ //value 1 for Plague
                cards.add(new Card("Event",3));
            }
        }
    }
    public void shuffleDeck(){
        Collections.shuffle(cards);
        Collections.shuffle(discards);
    }
    public int getSize(){
        return cards.size();
    }
    public int getDiscardSize(){
        return discards.size();
    }
}
