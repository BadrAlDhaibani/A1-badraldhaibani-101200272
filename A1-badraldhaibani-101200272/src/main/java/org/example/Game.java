package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {
    AdventureDeck adventureDeck;
    EventDeck eventDeck;
    List<Player> players = new ArrayList<>();
    int playerTurn = 0;
    Scanner scanner;
    public Game(){
        adventureDeck = new AdventureDeck();
        adventureDeck.shuffleDeck();
        eventDeck = new EventDeck();
        eventDeck.shuffleDeck();
        scanner = new Scanner(System.in);
        for (int i = 0; i < 4; i++){
            players.add(new Player(i+1));
        }
        for (int i = 0; i < 12; i++){
            players.get(0).hand.add(adventureDeck.cards.removeFirst());
            players.get(1).hand.add(adventureDeck.cards.removeFirst());
            players.get(2).hand.add(adventureDeck.cards.removeFirst());
            players.get(3).hand.add(adventureDeck.cards.removeFirst());
        }
    }
    public List<Player> checkForWinners(){
        List<Player> winners = new ArrayList<>();
        for(Player player : players){
            player.checkIfWinner(winners);
        }
        return winners;
    }
    public void round(){
        Player currentPlayer = players.get(playerTurn);
        System.out.print("P"+currentPlayer.number+"'s turn (Press 'Enter' to continue...)");
        scanner.nextLine();
        currentPlayer.displayHand();
        Card eventDrawn = eventCardDraw();
        System.out.println("Drawn "+eventDrawn.getType().charAt(0)+eventDrawn.getValue());
        if (eventDrawn.getType().equals("Event")){
            handleEvent(eventDrawn, currentPlayer);
        }
        else{
            handleQuest(eventDrawn, currentPlayer);
        }
        playerTurn++;
        if (playerTurn > 3) {
            playerTurn = 0;
        }
    }
    public Card eventCardDraw(){
        if(eventDeck.cards.isEmpty()){
            System.out.println("Event cards empty! Renewing Deck...");
            eventDeck.cards.addAll(eventDeck.discards);
            eventDeck.discards.clear();
            eventDeck.shuffleDeck();
        }
        Card eventDrawn = eventDeck.cards.removeFirst();
        eventDeck.discards.add(eventDrawn);
        return eventDrawn;
    }
    public void handleEvent(Card eventDrawn, Player currentPlayer){
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
    public void handleQuest(Card eventDrawn, Player currentPlayer){
        Player sponsor = currentPlayer.sponsor(players, scanner);
        List<List<Card>> quest = new ArrayList<>();
        if(sponsor !=  null){
            for(int i = 0; i < eventDrawn.getValue(); i++){
                quest.add(sponsor.buildStage(quest, scanner));
            }
            sponsor.printQuest(quest);
            System.out.println("---------------Quest Build Finished---------------");
            List<Player> participants = sponsor.getParticipants(players, scanner);
            if(!participants.isEmpty()){
                int stageNum = 1;
                for(List<Card> stage : quest){
                    if(!participants.isEmpty()){
                        System.out.println("---------------Stage "+stageNum+"---------------");
                        sponsor.stageStart(adventureDeck, stage, participants, scanner);
                        stageNum++;
                    }
                    else{
                        break;
                    }
                }
                if (!participants.isEmpty()) {
                    for (Player player : participants) {
                        player.shields += stageNum;
                        System.out.println("Player " + player.number + " wins and earns "+stageNum+" shields,  making that  "+player.shields+" total");
                    }
                }
            }
            else{
                System.out.println("No participants found... end of quest");
            }
        }
    }
}
