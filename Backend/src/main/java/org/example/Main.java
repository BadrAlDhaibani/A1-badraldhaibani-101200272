package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Main {
    /*public static void main(String[] args){
        Game currentGame = new Game();
        currentGame.createDecks();
        currentGame.assignHands();
        List<Player> winners = new ArrayList<>();
        boolean winnerFlag = false;
        while (!winnerFlag){
            Card eventDrawn = currentGame.eventCardDraw();
            if(eventDrawn.getType().equals("Event")){
                //REPLACE
                currentGame.handleEvent(eventDrawn, currentGame.currentPlayer);
            }
            else{
                Player sponsor = currentGame.findSponsor(eventDrawn);
                List<List<Card>> quest = currentGame.questStages(eventDrawn, sponsor);
                List<Player> Participants = currentGame.players;
                Participants.remove(sponsor);
                for(List<Card> stage : quest){
                    List<List<Card>> Attacks = new ArrayList<>();
                    for(Player player : Participants){
                        if(currentGame.askParticipant(player)){
                            Card drawnCard = currentGame.drawAdventureCard();
                            currentGame.playerHandlesDrawnCard(drawnCard, player);
                        }
                        else{
                            Participants.remove(player);
                        }
                    }
                    for(Player player : Participants){
                        Attacks.add(currentGame.prepareAttack(player));
                    }
                    List<Integer> goodAttacks = currentGame.checkAttacks(Attacks, stage);
                    Participants = currentGame.stageWinners(Participants, goodAttacks);
                }
                for(Player player : Participants){
                    currentGame.questWinnerRewarded(player, quest);
                }
            }
            winners = currentGame.checkForWinners();
            if (!winners.isEmpty()) {
                winnerFlag = true;
            }
        }
        System.out.println("Winner(s):");
        for (Player winner : winners) {
            winner.printWinner();
        }
        System.out.println("Game Over.");
    }*/
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}