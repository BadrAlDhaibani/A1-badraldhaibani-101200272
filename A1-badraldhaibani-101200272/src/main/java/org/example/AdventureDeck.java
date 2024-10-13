package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class AdventureDeck {
    public List<Card> cards;
    public List<Card> discards;

    public AdventureDeck(){
        cards = new ArrayList<>();
        discards = new ArrayList<>();
        for (int i = 0; i < 50; i++){
            if(i < 8){ //for 5(8)
                cards.add(new Card("Foe",5));
            }
            else if(i < 15){ //for 10(7)
                cards.add(new Card("Foe",10));
            }
            else if(i < 23){ //for 15(8)
                cards.add(new Card("Foe",15));
            }
            else if(i < 30){ //for 20(7)
                cards.add(new Card("Foe",20));
            }
            else if(i < 37){ //for 25(7)
                cards.add(new Card("Foe",25));
            }
            else if(i < 41){ //for 30(4)
                cards.add(new Card("Foe",30));
            }
            else if(i < 45){ //for 35(4)
                cards.add(new Card("Foe",35));
            }
            else if(i < 47){ //for 40(2)
                cards.add(new Card("Foe",40));
            }
            else if(i < 49){ //for 50(2)
                cards.add(new Card("Foe",50));
            }
            else if(i < 50){ //for 70(1)
                cards.add(new Card("Foe",70));
            }
        }
        for (int i = 50; i < 100; i++){
            if(i < 56){ //for 6 (D) Daggers (value=5)
                cards.add(new Card("Weapon",5));
            }
            else if(i < 68){ //for 12 (H) Horses (value=10)
                cards.add(new Card("Weapon",10));
            }
            else if(i < 84){ //for 16 (S) Swords (value=10)
                cards.add(new Card("Weapon",10));
            }
            else if(i < 92){ //for 8 (B) Battle-Axes (value=15)
                cards.add(new Card("Weapon",15));
            }
            else if(i < 98){ //for 6 (L) Lances (value=20)
                cards.add(new Card("Weapon",20));
            }
            else if(i < 100){ //for 2 (E) Excaliburs (value=30)
                cards.add(new Card("Weapon",30));
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
