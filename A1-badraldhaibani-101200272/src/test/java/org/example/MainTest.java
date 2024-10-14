package org.example;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.io.InputStream;
import java.io.ByteArrayInputStream;

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

    @Test
    public void RESP_2_test_1(){
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < 4; i++){
            players.add(new Player(i+1));
        }
        AdventureDeck adventureDeck = new AdventureDeck();
        adventureDeck.shuffleDeck();
        for (int i = 0; i < 12; i++){
            players.get(0).hand.add(adventureDeck.cards.removeFirst());
            players.get(1).hand.add(adventureDeck.cards.removeFirst());
            players.get(2).hand.add(adventureDeck.cards.removeFirst());
            players.get(3).hand.add(adventureDeck.cards.removeFirst());
        }
        for (Player player : players){
            assertEquals(12, player.hand.size());
        }
        assertEquals(52, adventureDeck.getSize());
    }

    @Test
    public void RESP_3_test_1() {
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            players.add(new Player(i + 1));
        }
        boolean winnerFlag = false;
        int playerTurn = 0; //following List index, so 0 == P1, 1 == P2 etc...
        while (!winnerFlag) {
            String sampleInput = "\n";
            InputStream inputStream = new ByteArrayInputStream(sampleInput.getBytes());
            System.setIn(inputStream);
            Scanner scanner = new Scanner(System.in);
            System.out.println("P" + players.get(playerTurn).number + "'s turn (Press 'Enter' to continue...)");
            scanner.nextLine();
            //Rest of the round
            players.get(playerTurn).shields += 2; //example to finish loop
            for (Player player : players) {
                if (player.shields >= 7) {
                    winnerFlag = true;
                }
            }
            playerTurn++;
            if (playerTurn > 3) {
                playerTurn = 0;
            }
        }
    }

    @Test
    public void RESP_4_test_1(){
        EventDeck eventDeck = new EventDeck();
        eventDeck.shuffleDeck();
        for (int i = 0; i < 20; i++){
            if(eventDeck.cards.isEmpty()){
                System.out.println("cards empty! discards full!");
                eventDeck.cards.addAll(eventDeck.discards);
                eventDeck.discards.clear();
                eventDeck.shuffleDeck();
            }
            int oldCardsSize = eventDeck.cards.size();
            int oldDiscardSize = eventDeck.discards.size();
            Card eventDrawn = eventDeck.cards.removeFirst();
            eventDeck.discards.add(eventDrawn);
            System.out.println("Drawn "+eventDrawn.getType().charAt(0)+eventDrawn.getValue());
            assertEquals(1, oldCardsSize - eventDeck.cards.size()); //card drawn from event deck
            assertEquals(1, eventDeck.discards.size() - oldDiscardSize); //card added to discard
        }
    }

    @Test
    public void RESP_5_test_1() {
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            players.add(new Player(i + 1));
        }
        List<Card> cards = new ArrayList<>();
        cards.add(new Card("Event", 1)); //Plague
        cards.add(new Card("Event", 2)); //Queen's favor
        cards.add(new Card("Event", 3)); //Prosperity
        Player currentPlayer = players.getFirst();
        AdventureDeck adventureDeck = new AdventureDeck();
        for (Card card : cards) {
            if (Objects.equals(card.getType(), "Event") && card.getValue() == 1) {
                if (currentPlayer.shields <= 2) {
                    currentPlayer.shields = 0;
                    assertEquals(0, currentPlayer.shields);
                }
                currentPlayer.shields = 5; //test input
                if (currentPlayer.shields > 2) {
                    currentPlayer.shields -= 2;
                    assertEquals(3, currentPlayer.shields);
                }
            } else if (Objects.equals(card.getType(), "Event") && card.getValue() == 2) {
                int oldSize = adventureDeck.getSize();
                currentPlayer.draw(adventureDeck);
                currentPlayer.draw(adventureDeck);
                assertEquals(2, oldSize - adventureDeck.getSize());
            } else if (Objects.equals(card.getType(), "Event") && card.getValue() == 3) {
                int oldSize = adventureDeck.getSize();
                for (Player player : players) {
                    player.draw(adventureDeck);
                    player.draw(adventureDeck);
                }
                assertEquals(8, oldSize - adventureDeck.getSize());
            }
        }
    }

    @Test
    public void RESP_6_test_1() {
        AdventureDeck adventureDeck = new AdventureDeck();
        Player player = new Player(1);
        for(int i = 0; i < 12; i++){
            player.draw(adventureDeck);
        }
        player.draw(adventureDeck); //Player hand should be full and trimmed functionality handled in draw function
        assertEquals(12, player.hand.size());
        assertEquals(1, adventureDeck.getDiscardSize());
    }
}