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
        Scanner scanner = new Scanner(System.in);
        while (!winnerFlag){
            Player currentPlayer = players.get(playerTurn);
            System.out.print("P"+currentPlayer.number+"'s turn (Press 'Enter' to continue...)");
            scanner.nextLine();
            currentPlayer.displayHand();
            if(eventDeck.cards.isEmpty()){
                System.out.println("Event cards empty! Renewing Deck...");
                eventDeck.cards.addAll(eventDeck.discards);
                eventDeck.discards.clear();
                eventDeck.shuffleDeck();
            }
            Card eventDrawn = eventDeck.cards.removeFirst();
            eventDeck.discards.add(eventDrawn);
            System.out.println("Drawn "+eventDrawn.getType().charAt(0)+eventDrawn.getValue());
            if (eventDrawn.getType().equals("Event")){
                if (eventDrawn.getValue() == 1){
                    System.out.println("Plague! Current player loses 2 shields");
                    if (currentPlayer.shields <= 2){
                        currentPlayer.shields =  0;
                    }
                    else {
                        currentPlayer.shields -= 2;
                    }
                }
                else if (eventDrawn.getValue() == 2){
                    System.out.println("Queen's favor! Current player draws 2 adventure cards");
                    currentPlayer.draw(adventureDeck, scanner);
                    currentPlayer.draw(adventureDeck, scanner);
                }
                else if (eventDrawn.getValue() == 3){
                    System.out.println("Prosperity! All players draw 2 adventure cards");
                    for(Player player : players){
                        player.draw(adventureDeck, scanner);
                        player.draw(adventureDeck, scanner);
                    }
                }
            }
            else{
                Player sponsor = currentPlayer.sponsor(players, scanner);
                List<List<Card>> quest = new ArrayList<>();

                for(int i = 0; i < eventDrawn.getValue(); i++){
                    quest.add(sponsor.buildStage(quest, scanner));
                }
                sponsor.printQuest(quest);

            }
            players.get(playerTurn).shields += 1; //example to finish loop
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