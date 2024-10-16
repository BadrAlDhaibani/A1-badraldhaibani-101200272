package org.example;

import java.util.*;

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

    public List<Card> buildStage( List<List<Card>> quest, Scanner scanner){
        List<Card> stageCards = new ArrayList<>();
        boolean buildingStage = true;
        while(buildingStage){
            displayHand();
            printQuest(quest);
            System.out.println("---------------Current Stage---------------");
            for(Card card : stageCards){
                System.out.println(card.toString());
            }
            System.out.print("Select position of the card to include in stage or type 'q' to quit building: ");
            String input = scanner.nextLine();
            if(input.equals("q")){
                if (stageCards.isEmpty()){
                    System.out.println("A stage cannot be empty");
                }
                else if(!isValidStage(stageCards)){
                    System.out.println("Invalid stage, must have exactly one Foe card and no repeated Weapon cards.");
                    hand.addAll(stageCards);
                    stageCards.clear();
                }
                else {
                    int previousStageValue = quest.isEmpty() ? 0 : calculateStageValue(quest.getLast());
                    int currentStageValue = calculateStageValue(stageCards);
                    if(currentStageValue <= previousStageValue){
                        System.out.println("Insufficient value for this stage");
                        hand.addAll(stageCards);
                        stageCards.clear();
                    }
                    else{
                        System.out.println("Stage building ended.");
                        buildingStage = false;
                    }
                }
            }
            else{
                try {
                    int position = Integer.parseInt(input) - 1;
                    if (position >= 0 && position < hand.size()){
                        Card cardToAdd = hand.remove(position);
                        stageCards.add(cardToAdd);
                        System.out.println("Card added to stage:");
                    }
                    else {
                        System.out.println("Invalid position, please enter a valid card index.");
                    }
                }
                catch (NumberFormatException e){
                    System.out.println("Invalid position, please enter a valid card index.");
                }
            }
        }
        return stageCards;
    }

    public void printQuest(List<List<Card>> quest){
        int stageNum = 1;
        for(List<Card> stage : quest){
            System.out.println("---------------Stage "+stageNum+"---------------");
            for(Card card : stage){
                System.out.println(card.toString());
            }
            stageNum++;
        }
    }

    public int calculateStageValue(List<Card> stageCards){
        int totalValue = 0;
        for(Card card : stageCards){
            totalValue += card.getValue();
        }
        return totalValue;
    }

    public boolean isValidStage(List<Card> stageCards){
        int foeCount = 0;
        Set<String> weaponsUsed = new HashSet<>();
        for(Card card : stageCards){
            if (card.getType().equals("Foe")){
                foeCount++;
            }
            else if (card.getType().equals("Weapon")){
                if (weaponsUsed.contains(card.getLabel())){
                    return false;
                }
                weaponsUsed.add(card.getLabel());
            }
        }
        return foeCount == 1;
    }

    public List<Player> getParticipants(List<Player> players, Scanner scanner){
        List<Player> participants = new ArrayList<>();
        for(Player player : players){
            if(player != this){
                System.out.print("P"+player.number+", do you want to participate in the quest? (y/n): ");
                String input = scanner.nextLine();
                if(input.equalsIgnoreCase("y")){
                    participants.add(player);
                    System.out.println("P"+player.number+" is participating in the quest");
                }
                else{
                    System.out.println("P"+player.number+" has declined to participate");
                }
            }
        }
        return participants;
    }

    public void stageStart(AdventureDeck adventureDeck, List<Card> stage, List<Player> questParticipants, Scanner scanner){
        List<Player> playersToRemove = new ArrayList<>();
        int stageValue = calculateStageValue(stage);
        for(Player player : questParticipants){
            System.out.print("P"+player.number+", would you like to withdraw (w) from the quest or tackle (t) the current stage? (w/t): ");
            String input = scanner.nextLine();
            if(input.equalsIgnoreCase("w")){
                playersToRemove.add(player);
            }
            else{
                player.draw(adventureDeck, scanner);
                List<Card> attack = player.prepareAttack(scanner);
                int attackValue = calculateStageValue(attack);
                System.out.println("Player "+player.number+" has an attack value of "+attackValue);
                if(attackValue < stageValue){
                    playersToRemove.add(player);
                }
                adventureDeck.discards.addAll(attack);
            }
        }
        for(Player lostPlayer : playersToRemove){
            System.out.println("Player "+lostPlayer.number+"'s attack is too weak and they are eliminated.");
        }
        questParticipants.removeAll(playersToRemove);
        adventureDeck.discards.addAll(stage);
        if(questParticipants.isEmpty()){
            System.out.println("End of Quest");
        }
        else{
            System.out.println("Players proceeding to the next stage: " + questParticipants.size());
        }
    }

    public List<Card> prepareAttack(Scanner scanner){
        List<Card> attack = new ArrayList<>();
        Set<String> usedWeapons = new HashSet<>();
        boolean preparing = true;
        while(preparing){
            displayHand();
            System.out.println("---------------Current Attack---------------");
            for(Card card : attack){
                System.out.println(card.toString());
            }
            System.out.print("Prepare your attack. Select weapon cards (enter the position of the card) or type 'q' to finish: ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("q")){
                preparing = false;
            }
            else{
                try{
                    int position = Integer.parseInt(input) - 1;
                    if(position >= 0  && position < hand.size()) {
                        Card selectedCard = hand.remove(position);
                        if(selectedCard.getType().equals("Weapon") && !usedWeapons.contains(selectedCard.getLabel())){
                            attack.add(selectedCard);
                            usedWeapons.add(selectedCard.getLabel());
                            System.out.println("Added "+selectedCard.getLabel()+" (Value: "+selectedCard.getValue()+") to your attack.");
                        }
                        else{
                            System.out.println("Invalid choice or repeated weapon. Please select a different weapon.");
                            hand.add(selectedCard);
                            hand.addAll(attack);
                            usedWeapons.clear();
                            attack.clear();
                        }
                    }
                    else{
                        System.out.println("Invalid position. Please select a valid card index.");
                    }
                }
                catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
            }
        }
        return attack;
    }
}
