package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
            Scanner scanner = new Scanner(System.in);
            System.out.println("P"+players.get(playerTurn).number+"'s turn (Press 'Enter' to continue...)");
            scanner.nextLine();
            if(eventDeck.cards.isEmpty()){
                System.out.println("cards empty!");
                eventDeck.cards.addAll(eventDeck.discards);
                eventDeck.discards.clear();
                eventDeck.shuffleDeck();
            }
            Card eventDrawn = eventDeck.cards.removeFirst();
            eventDeck.discards.add(eventDrawn);
            System.out.println("Drawn "+eventDrawn.getType().charAt(0)+eventDrawn.getValue());
            players.get(playerTurn).shields += 1; //example to finish loop
            //System.out.println("P"+players.get(playerTurn).number+" "+players.get(playerTurn).shields); //test print
            //Check for winner
            for(Player player : players){
                if (player.shields >= 7){
                    winnerFlag = true;
                }
            }
            //Rotate players
            playerTurn++;
            if (playerTurn > 3) {
                playerTurn = 0;
            }
        }
    }
}