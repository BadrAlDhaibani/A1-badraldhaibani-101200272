package org.example;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class MainTest {
    @Test
    public void RESP_1_test_1(){
        AdventureDeck adventureDeck = new AdventureDeck();
        assertNotNull(adventureDeck);
        assertEquals(100, adventureDeck.getSize());
        assertEquals(0, adventureDeck.getDiscardSize());
        //Assuming generated adventure deck cards are in order of increasing value, starting with Foes first
        for (int i = 0; i < 50; i++){
            assertEquals("Foe", adventureDeck.cards.get(i).getType());
            if(i < 8){ //checking for 5(8)
                assertEquals(5, adventureDeck.cards.get(i).getValue());
            }
            else if(i < 15){ //checking for 10(7)
                assertEquals(10, adventureDeck.cards.get(i).getValue());
            }
            else if(i < 23){ //checking for 15(8)
                assertEquals(15, adventureDeck.cards.get(i).getValue());
            }
            else if(i < 30){ //checking for 20(7)
                assertEquals(20, adventureDeck.cards.get(i).getValue());
            }
            else if(i < 37){ //checking for 25(7)
                assertEquals(25, adventureDeck.cards.get(i).getValue());
            }
            else if(i < 41){ //checking for 30(4)
                assertEquals(30, adventureDeck.cards.get(i).getValue());
            }
            else if(i < 45){ //checking for 35(4)
                assertEquals(35, adventureDeck.cards.get(i).getValue());
            }
            else if(i < 47){ //checking for 40(2)
                assertEquals(40, adventureDeck.cards.get(i).getValue());
            }
            else if(i < 49){ //checking for 50(2)
                assertEquals(50, adventureDeck.cards.get(i).getValue());
            }
            else if(i < 50){ //checking for 70(1)
                assertEquals(70, adventureDeck.cards.get(i).getValue());
            }
        }
        for (int i = 50; i < 100; i++){
            assertEquals("Weapon", adventureDeck.cards.get(i).getType());
            if(i < 56){ //checking for 6 (D) Daggers (value=5)
                assertEquals(5, adventureDeck.cards.get(i).getValue());
            }
            else if(i < 68){ //checking for 12 (H) Horses (value=10)
                assertEquals(10, adventureDeck.cards.get(i).getValue());
            }
            else if(i < 84){ //checking for 16 (S) Swords (value=10)
                assertEquals(10, adventureDeck.cards.get(i).getValue());
            }
            else if(i < 92){ //checking for 8 (B) Battle-Axes (value=15)
                assertEquals(15, adventureDeck.cards.get(i).getValue());
            }
            else if(i < 98){ //checking for 6 (L) Lances (value=20)
                assertEquals(20, adventureDeck.cards.get(i).getValue());
            }
            else if(i < 100){ //checking for 2 (E) Excaliburs (value=30)
                assertEquals(30, adventureDeck.cards.get(i).getValue());
            }
        }
        EventDeck eventDeck = new EventDeck();
        assertNotNull(eventDeck);
        assertEquals(17, eventDeck.getSize());
        assertEquals(0, eventDeck.getDiscardSize());
        //Assuming generated event deck cards are in order of increasing value, starting with Quest first
        for (int i = 0; i < 12; i++) {
            assertEquals("Quest", eventDeck.cards.get(i).getType());
            if(i < 3){ //checking for 3 Q2
                assertEquals(2, eventDeck.cards.get(i).getValue());
            }
            else if(i < 7){ //checking for 4 Q3
                assertEquals(3, eventDeck.cards.get(i).getValue());
            }
            else if(i < 10){ //checking for 3 Q4
                assertEquals(4, eventDeck.cards.get(i).getValue());
            }
            else if(i < 12){ //checking for 2 Q5
                assertEquals(5, eventDeck.cards.get(i).getValue());
            }
        }
        for (int i = 12; i < 17; i++) {
            assertEquals("Event", eventDeck.cards.get(i).getType());
            if(i < 13){ //checking for  1 “Plague” value=1
                assertEquals(1, eventDeck.cards.get(i).getValue());
            }
            else if(i < 15){ //checking for  2 “Queen’s favor” value=2
                assertEquals(2, eventDeck.cards.get(i).getValue());
            }
            else if(i < 17){ //checking for 2 “Prosperity” value=3
                assertEquals(3, eventDeck.cards.get(i).getValue());
            }
        }
    }
}