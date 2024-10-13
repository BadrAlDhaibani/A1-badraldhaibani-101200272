package org.example;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args){
        AdventureDeck adventureDeck = new AdventureDeck();
        adventureDeck.shuffleDeck();
        EventDeck eventDeck = new EventDeck();
        eventDeck.shuffleDeck();
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < 4; i++){
            players.add(new Player(i+1));
        }
        for (int i = 0; i < 12; i++){
            players.get(0).hand.add(adventureDeck.cards.removeFirst());
            players.get(1).hand.add(adventureDeck.cards.removeFirst());
            players.get(2).hand.add(adventureDeck.cards.removeFirst());
            players.get(3).hand.add(adventureDeck.cards.removeFirst());
        }
        boolean winnerFlag = false;
        int playerTurn = 0;
        while (!winnerFlag){
            players.get(playerTurn).shields += 2; //example to finish loop
            //System.out.println("P"+players.get(playerTurn).number+" "+players.get(playerTurn).shields); //test print
            for(Player player : players){
                if (player.shields >= 7){
                    winnerFlag = true;
                }
            }
            playerTurn++;
            if (playerTurn > 3) {
                playerTurn = 0;
            }
        }
    }
}