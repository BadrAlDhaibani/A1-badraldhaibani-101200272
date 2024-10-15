package org.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Player {
    public List<Card> hand;
    public int number;
    public int shields;
    public Player(int number){
        hand = new ArrayList<>();
        this.number = number;
    }
    public void draw(AdventureDeck adventureDeck, Scanner scanner){
        if (adventureDeck.getSize() == 0){
            adventureDeck.cards.addAll(adventureDeck.discards);
            adventureDeck.shuffleDeck();
        }
        Card drawnCard = adventureDeck.cards.removeFirst();
        if(this.hand.size() == 12){
            System.out.print("P"+number+" has a full hand... (Press Enter to continue)");
            scanner.nextLine();
            displayHand();
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
                    if (position >= 0 && position < this.hand.size()){
                        Card discardedCard = this.hand.remove(position);
                        adventureDeck.discards.add(discardedCard);
                        System.out.println("Discarded: "+discardedCard.getLabel()+" (Value: "+discardedCard.getValue()+")");
                        this.hand.add(drawnCard);
                        displayHand();
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
            this.hand.add(drawnCard);
        }
    }
    public void sortHand(){
        List<Card> foes = new ArrayList<>();
        List<Card> daggers = new ArrayList<>();
        List<Card> swords = new ArrayList<>();
        List<Card> other = new ArrayList<>();
        for(Card card : hand){
            if (card.getType().equals("Foe")){
                foes.add(card);
            }
            else if (card.getType().equals("Weapon")) {
                if (card.getValue() == 5) {
                    daggers.add(card);
                } else if (card.getLabel().equals("Sword")) {
                    swords.add(card);
                } else {
                    other.add(card);
                }
            }
        }
        foes.sort(Comparator.comparingInt(Card::getValue));
        daggers.sort(Comparator.comparingInt(Card::getValue));
        swords.sort(Comparator.comparingInt(Card::getValue));
        other.sort(Comparator.comparingInt(Card::getValue));
        List<Card> sortedHand = new ArrayList<>();
        sortedHand.addAll(foes);
        sortedHand.addAll(daggers);
        sortedHand.addAll(swords);
        sortedHand.addAll(other);

        hand.clear();
        hand.addAll(sortedHand);
    }
    public void displayHand(){
        sortHand();
        System.out.println("Your current hand:");
        for(int i = 0; i < hand.size(); i++){
            System.out.println((i + 1)+": "+hand.get(i).toString());
        }
    }

    public Player sponsor(List<Player> players, Scanner scanner) {
        for (int i = 0; i < players.size(); i++) {
            Player pask = players.get(((number-1) + i) % players.size());
            System.out.print("P" + pask.number + ", would you like to sponsor the quest? (y/n): ");
            String response = scanner.nextLine();
            if (response.equals("y")) {
                System.out.println("P" + pask.number + " is sponsoring the quest");
                return pask;
            }
        }
        System.out.println("All players declined to sponsor the quest.");
        return null;
    }

}
