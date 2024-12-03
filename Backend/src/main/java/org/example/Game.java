package org.example;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {
    AdventureDeck adventureDeck;
    EventDeck eventDeck;
    List<Player> players = new ArrayList<>();
    int playerTurn = 0;
    Player currentPlayer;
    Scanner scanner;
    public void setScannerInput(InputStream inputStream) {
        this.scanner = new Scanner(inputStream);
    }
    public Game(){
        scanner = new Scanner(System.in);
        for (int i = 0; i < 4; i++){
            players.add(new Player(i+1));
        }
        currentPlayer = players.get(playerTurn);
    }
    public void createDecks(){
        adventureDeck = new AdventureDeck();
        adventureDeck.shuffleDeck();
        eventDeck = new EventDeck();
        eventDeck.shuffleDeck();
    }
    public void assignHands(){
        for (int i = 0; i < 12; i++){
            players.get(0).hand.add(adventureDeck.cards.remove(0));
            players.get(1).hand.add(adventureDeck.cards.remove(0));
            players.get(2).hand.add(adventureDeck.cards.remove(0));
            players.get(3).hand.add(adventureDeck.cards.remove(0));
        }
    }
    public List<Player> checkForWinners(){
        List<Player> winners = new ArrayList<>();
        for(Player player : players){
            player.checkIfWinner(winners);
        }
        return winners;
    }
    public void round(Card eventDrawn){
        System.out.println("Drawn "+eventDrawn.getType().charAt(0)+eventDrawn.getValue());
        if (eventDrawn.getType().equals("Event")){
            handleEvent(eventDrawn, currentPlayer);
        }
        else{
            Player sponsor = findSponsor(eventDrawn);
            handleQuest(eventDrawn, sponsor);
        }
        playerTurn++;
        currentPlayer = players.get(playerTurn);
        if (playerTurn > 3) {
            playerTurn = 0;
        }
    }
    public Card eventCardDraw(){
        System.out.print("P"+currentPlayer.number+"'s turn (Press 'Enter' to continue...)");
        scanner.nextLine();
        currentPlayer.displayHand();
        if(eventDeck.cards.isEmpty()){
            System.out.println("Event cards empty! Renewing Deck...");
            eventDeck.cards.addAll(eventDeck.discards);
            eventDeck.discards.clear();
            eventDeck.shuffleDeck();
        }
        Card eventDrawn = eventDeck.cards.remove(0);
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
    public Player findSponsor(Card eventDrawn){
        return currentPlayer.sponsor(eventDrawn, players, scanner);
    }
    public List<List<Card>> questStages(Card eventDrawn, Player sponsor){
        List<List<Card>> quest = new ArrayList<>();
        for(int i = 0; i < eventDrawn.getValue(); i++){
            quest.add(sponsor.buildStage(quest, scanner));
        }
        return quest;
    }
    public boolean askParticipant(Player player){
        System.out.print("P"+player.number+", do you want to participate in the quest? (y/n): ");
        String input = scanner.nextLine();
        if(input.equalsIgnoreCase("y")){
            System.out.println("P"+player.number+" is participating in the quest");
            return true;
        }
        else{
            System.out.println("P"+player.number+" has declined to participate");
            return false;
        }
    }

    public List<Card> prepareAttack(Player player){
        return player.prepareAttack(scanner);
    }
    public List<Integer> checkAttacks(List<List<Card>> Attacks, List<Card> stage){
        List<Integer> goodAttacks = new ArrayList<>();
        for(List<Card> attack : Attacks){
            if(calculateCardsValue(attack) >= calculateCardsValue(stage)){
                goodAttacks.add(Attacks.indexOf(attack));
            }
        }
        return goodAttacks;
    }
    public List<Player> stageWinners(List<Player> Participants, List<Integer> goodAttacks){
        List<Player> stageWinners = new ArrayList<>();
        for(int index : goodAttacks){
            stageWinners.add(Participants.get(index));
            System.out.println("P"+Participants.get(index).number+" will proceed to the next stage");
        }
        return stageWinners;
    }
    public void questWinnerRewarded(Player player, List<List<Card>> Quest){
        for(List<Card> stage : Quest){
            player.shields++;
        }
    }
    public int calculateCardsValue(List<Card> cards){
        int totalValue = 0;
        for(Card card : cards){
            totalValue += card.getValue();
        }
        return totalValue;
    }
    public Card drawAdventureCard(){
        if (adventureDeck.getSize() == 0){
            adventureDeck.cards.addAll(adventureDeck.discards);
            adventureDeck.shuffleDeck();
        }
        return adventureDeck.cards.remove(0);
    }
    public void playerHandlesDrawnCard(Card drawnCard, Player player){
        if(player.hand.size() == 12){
            System.out.print("P"+player.number+" has a full hand... (Press Enter to continue)");
            scanner.nextLine();
            player.displayHand();
            if(drawnCard.getType().equals("Weapon")){
                System.out.println("Drawn card: "+drawnCard.getLabel()+" (Value: "+drawnCard.getValue()+")");
            }
            else{
                System.out.println("Drawn card: "+drawnCard.getType().charAt(0)+drawnCard.getValue());
            }
            boolean validInput = false;
            while(!validInput){
                System.out.print("Please select the position of the card you'd like to discard or type 'q': ");
                String input = scanner.nextLine();
                if(input.equalsIgnoreCase("q")){
                    System.out.println("Canceled, no cards discarded.");
                    adventureDeck.discards.add(drawnCard);
                    return;
                }
                try{
                    int position = Integer.parseInt(input) - 1;
                    if (position >= 0 && position < player.hand.size()){
                        Card discardedCard = player.hand.remove(position);
                        adventureDeck.discards.add(discardedCard);
                        if(discardedCard.getType().equals("Weapon")){
                            System.out.println("Discarded: "+discardedCard.getLabel()+" (Value: "+discardedCard.getValue()+")");
                        }
                        else {
                            System.out.println("Discarded: " + discardedCard.getType().charAt(0)+discardedCard.getValue() + " (Value: " + discardedCard.getValue() + ")");
                        }
                        player.hand.add(drawnCard);
                        player.displayHand();
                        validInput = true;
                    }
                    else {
                        System.out.println("Invalid position, please enter enter a valid card index");
                    }
                } catch (NumberFormatException e){
                    System.out.println("Invalid input, please enter a valid number");
                }
            }
        }
        else{
            player.hand.add(drawnCard);
        }
    }

    public void handleQuest(Card eventDrawn, Player sponsor){
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
