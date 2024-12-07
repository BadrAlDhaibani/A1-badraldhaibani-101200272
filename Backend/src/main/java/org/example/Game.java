package org.example;

import java.util.*;

public class Game {
    AdventureDeck adventureDeck;
    EventDeck eventDeck;
    List<Player> players = new ArrayList<>();
    int playerTurn = 0;
    Player currentPlayer;
    String gameState;
    String nextGameState;
    int stateRepeater = 0;
    Card adventureCardDrawn;
    Player sponsor;

    Card currentQuest;
    public List<List<Card>> questStages;  // List to hold all stages
    public List<Card> currentStage;       // The stage currently being built
    public int currentStageNumber;
    public List<Player> participants;
    public List<List<Card>> attacks;
    public List<Card> currentAttack;

    public Game(){
        //Adding 4 players
        for (int i = 0; i < 4; i++){
            players.add(new Player(i+1));
        }
        //Setting current player to P1
        currentPlayer = players.get(playerTurn);
        //Init decks and shuffling
        adventureDeck = new AdventureDeck();
        adventureDeck.shuffleDeck();
        eventDeck = new EventDeck();
        eventDeck.shuffleDeck();
        //Init players hands
        for (int i = 0; i < 12; i++){
            players.get(0).hand.add(adventureDeck.cards.remove(0));
            players.get(1).hand.add(adventureDeck.cards.remove(0));
            players.get(2).hand.add(adventureDeck.cards.remove(0));
            players.get(3).hand.add(adventureDeck.cards.remove(0));
        }
        gameState = "DRAW_EVENT_CARD";
    }
    public String handleInput(String input){
        if(input.equals("start")){
            switch(gameState){
                case "DRAW_EVENT_CARD":
                    Card eventCard = eventCardDraw();
                    eventCard = new Card("Quest",2); //TEST
                    if(eventCard.getType().equals("Event")){
                        String eventMessage = handleEvent(eventCard);
                        return "P"+(playerTurn+1)+"'s turn\n"+"Event card drawn: " + eventCard.toString() + "\n" + eventMessage;
                    }
                    else{
                        currentQuest = eventCard;
                        gameState = "FIND_SPONSOR";
                        return "P"+(playerTurn+1)+"'s turn\n"+"Quest card drawn: " + eventCard.toString() + "\n" + "Determining sponsor... (type 'start' to continue)";
                    }
                case "DRAW":
                    if(stateRepeater > 0){
                        stateRepeater -= 1;
                        adventureCardDrawn = adventureCardDraw();
                        if(currentPlayer.hand.size() == 12){
                            gameState = "TRIM_HAND";
                            return "Drawn card: "+adventureCardDrawn.toString()+"\nP"+currentPlayer.number+" has a full hand... (type 'start' to continue)";
                        }
                        else{
                            currentPlayer.hand.add(adventureCardDrawn);
                            return "Drawn card: "+adventureCardDrawn.toString()+"\nType 'start' to continue";
                        }
                    }
                    else{
                        gameState = nextGameState;
                        return "Done drawing P"+currentPlayer.number+" cards (type 'start' to continue)";
                    }
                case "TRIM_HAND":
                    return trimHand();
                case "PROSPERITY_NEXT_PLAYER":
                    int nextPlayerIndex = players.indexOf(currentPlayer)+1;
                    if(nextPlayerIndex < players.size()){
                        currentPlayer = players.get(nextPlayerIndex);
                        gameState = "DRAW";
                        nextGameState = "PROSPERITY_NEXT_PLAYER";
                        stateRepeater = 2;
                        return "P"+currentPlayer.number+"'s turn... (type 'start' to continue)";
                    }
                    else{
                        gameState = "DRAW_EVENT_CARD";
                        goNextPlayer(players);
                        return "Prosperity finished. (type 'start' to continue)";
                    }
                case "FIND_SPONSOR":
                    return findSponsor(input);
                case "SPONSOR_ACCEPTED":
                    questStages = new ArrayList<>();
                    currentStage = new ArrayList<>();
                    currentStageNumber = 1;
                    gameState = "BUILD_QUEST_STAGE";
                    return sponsor.displayHand()+"P" + sponsor.number + " is building stage " + currentStageNumber + " of the quest. " +
                            "Type the position of the card to add to the stage or 'q' to finish this stage."+displayQuestStages();
                case "BUILD_QUEST_STAGE":
                    return buildQuestStage(input);
                case "GET_PARTICIPANTS":
                    participants = new ArrayList<>(players);
                    participants.remove(sponsor);
                    return getParticipants(input);
                case "PARTICIPANTS_DRAW":
                    currentPlayer = participants.get(0);
                    gameState = "DRAW";
                    nextGameState = "PARTICIPANTS_DRAW_NEXT";
                    stateRepeater = 1;
                    return "Participants are drawing one card each. Starting with P" + currentPlayer.number + " (type 'start' to continue)";
                case "PARTICIPANTS_DRAW_NEXT":
                    int nextParticipantIndex = participants.indexOf(currentPlayer) + 1;
                    if (nextParticipantIndex < participants.size()) {
                        currentPlayer = participants.get(nextParticipantIndex);
                        gameState = "DRAW";
                        nextGameState = "PARTICIPANTS_DRAW_NEXT";
                        stateRepeater = 1;
                        return "P" + currentPlayer.number + "'s turn to draw a card. (type 'start' to continue)";
                    } else {
                        gameState = "PROCESS_ATTACKS";
                        currentPlayer = participants.get(0);
                        currentStageNumber = 1;
                        attacks = new ArrayList<>();
                        currentAttack = new ArrayList<>();
                        return "All participants have drawn their card. Starting the stages with P"+currentPlayer.number+"... (type 'start' to continue)";
                    }
                case "PROCESS_ATTACKS":
                    StringBuilder attackPrompt = new StringBuilder();
                    attackPrompt.append("P").append(currentPlayer.number).append(", build your attack for Stage ")
                            .append(currentStageNumber)
                            .append(". Type the position of the card to add, or 'q' to finish your attack.");
                    return currentPlayer.displayHand() + attackPrompt.toString();
                case "EVALUATE_ATTACKS":
                    StringBuilder evaluationResult = new StringBuilder();
                    int stageValue = calculateStageValue(currentStage);
                    List<Player> survivingParticipants = new ArrayList<>();
                    evaluationResult.append("Evaluating attacks for Stage ").append(currentStageNumber).append("...\n");
                    for (int i = 0; i < participants.size(); i++) {
                        Player participant = participants.get(i);
                        List<Card> attack = attacks.get(i);
                        int attackValue = calculateStageValue(attack);
                        evaluationResult.append("P").append(participant.number).append(" Attack Value: ").append(attackValue).append("\n");
                        if (attackValue >= stageValue) {
                            survivingParticipants.add(participant);
                            evaluationResult.append("P").append(participant.number).append(" successfully passed this stage!\n");
                        } else {
                            evaluationResult.append("P").append(participant.number).append(" failed this stage and is out of the quest.\n");
                        }
                    }
                    participants = survivingParticipants;
                    if (participants.isEmpty()) {
                        gameState = "DRAW_EVENT_CARD";
                        evaluationResult.append("No participants remain. The quest ends with no winners. (type 'start' to continue)");
                    } else if (currentStageNumber == questStages.size()) {
                        int shieldsEarned = questStages.size();
                        evaluationResult.append("The quest is complete! Winners:\n");
                        for (Player winner : participants) {
                            winner.shields += shieldsEarned;
                            evaluationResult.append("P").append(winner.number).append(" earns ").append(shieldsEarned).append(" shields.\n");
                        }
                        gameState = "DRAW_EVENT_CARD";
                        evaluationResult.append("(type 'start' to continue)");
                    } else {
                        currentStageNumber++;
                        currentStage = questStages.get(currentStageNumber - 1);
                        attacks = new ArrayList<>();
                        currentAttack = new ArrayList<>();
                        currentPlayer = participants.get(0);
                        gameState = "GET_PARTICIPANTS";
                        evaluationResult.append("Proceeding to Stage ").append(currentStageNumber).append("... (type 'start' to continue)");
                    }
                    return evaluationResult.toString();
            }
        }
        if (gameState.equals("TRIM_HAND")) {
            return discardCard(input);
        }
        if (gameState.equals("FIND_SPONSOR")) {
            return findSponsor(input);
        }
        if (gameState.equals("BUILD_QUEST_STAGE")) {
            return buildQuestStage(input);
        }
        if (gameState.equals("GET_PARTICIPANTS")){
            return getParticipants(input);
        }
        if (gameState.equals("PROCESS_ATTACKS")){
            return buildAttack(input);
        }
        return "Invalid input. (type 'start' to continue)";
    }
    public void goNextPlayer(List<Player> players){
        playerTurn = (playerTurn + 1) % players.size();
        currentPlayer = players.get(playerTurn);
    }
    public Card eventCardDraw(){
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
    public Card adventureCardDraw(){
        if (adventureDeck.getSize() == 0){
            adventureDeck.cards.addAll(adventureDeck.discards);
            adventureDeck.shuffleDeck();
        }
        Card drawnCard = adventureDeck.cards.remove(0);
        return drawnCard;
    }
    public String handleEvent(Card eventDrawn){
        if (eventDrawn.getValue() == 1){
            if (currentPlayer.shields <= 2){
                currentPlayer.shields =  0;
            }
            else {
                currentPlayer.shields -= 2;
            }
            gameState = "DRAW_EVENT_CARD";
            goNextPlayer(players);
            return "Plague! Current player loses 2 shields";
        }
        else if (eventDrawn.getValue() == 2){
            gameState = "DRAW";
            nextGameState = "DRAW_EVENT_CARD";
            stateRepeater = 2;
            return "Queen's favor! Current player draws 2 adventure cards (type 'start' to continue)";
        }
        else if (eventDrawn.getValue() == 3){
            currentPlayer = players.get(0);
            gameState = "DRAW";
            nextGameState = "PROSPERITY_NEXT_PLAYER";
            stateRepeater = 2;
            return "Prosperity! All players draw 2 adventure cards. P1's turn... (type 'start' to continue)";
        }
        return "";
    }
    public String discardCard(String input) {
        try {
            int cardIndex = Integer.parseInt(input) - 1;
            if (cardIndex < 0 || cardIndex >= currentPlayer.hand.size()) {
                return "Invalid card number. Please select a valid position.";
            }
            Card discardedCard = currentPlayer.hand.remove(cardIndex);
            adventureDeck.discards.add(discardedCard);
            currentPlayer.hand.add(adventureCardDrawn);
            gameState = "DRAW";
            return currentPlayer.displayHand()+"Hand trimmed successfully (type 'start' to continue)";
        } catch (NumberFormatException e) {
            return "Invalid input. Please enter a valid number.";
        }
    }
    public String trimHand() {
        StringBuilder response = new StringBuilder("P"+currentPlayer.number+" current hand:\n");
        response.append(currentPlayer.displayHand());
        response.append("Type the number of the card to discard:");
        return response.toString();
    }
    public String findSponsor(String input) {
        if (input.equals("start")) {
            return "P" + currentPlayer.number + ", would you like to sponsor the quest? (y/n)";
        } else if (input.equalsIgnoreCase("y")) {
            gameState = "SPONSOR_ACCEPTED";
            sponsor = currentPlayer;
            return "P" + currentPlayer.number + " has agreed to sponsor the quest! (type 'start' to continue)";
        } else if (input.equalsIgnoreCase("n")) {
            int nextPlayerIndex = (players.indexOf(currentPlayer) + 1) % players.size();
            if (nextPlayerIndex == playerTurn) {
                gameState = "DRAW_EVENT_CARD";
                goNextPlayer(players);
                return "All players declined to sponsor the quest. (type 'start' to continue)";
            }
            currentPlayer = players.get(nextPlayerIndex);
            return "P" + currentPlayer.number + ", would you like to sponsor the quest? (y/n)";
        }
        return "Invalid input. (type 'start' to continue or y/n)";
    }
    private String buildQuestStage(String input) {
        if (input.equals("q")) {
            if (currentStage.isEmpty()) {
                return "A stage cannot be empty. Please add at least one card.";
            } else if (!isValidStage(currentStage)) {
                sponsor.hand.addAll(currentStage);
                currentStage.clear();
                return sponsor.displayHand()
                        +"Invalid stage. Must have exactly one Foe card and no repeated Weapon cards.\n"
                        +"Type the position of the card to add to the stage or 'q' to finish this stage."+displayQuestStages();
            } else {
                if (!questStages.isEmpty() && calculateStageValue(currentStage) <= calculateStageValue(questStages.get(questStages.size() - 1))) {
                    sponsor.hand.addAll(currentStage);
                    currentStage.clear();
                    return sponsor.displayHand()
                            +"Insufficient value for this stage. Each stage must be stronger than the previous one.\n"
                            +"Type the position of the card to add to the stage or 'q' to finish this stage."+displayQuestStages();
                }
                questStages.add(new ArrayList<>(currentStage));
                currentStage.clear();
                currentStageNumber++;
                if (questStages.size() == currentQuest.getValue()) {
                    gameState = "GET_PARTICIPANTS";
                    participants = new ArrayList<>(players);
                    return displayQuestStages() + "Quest stages are complete. Determining participants... (type 'start' to continue)";
                }

                return sponsor.displayHand()
                        +"Stage "+(currentStageNumber - 1)+" completed successfully.\n"
                        +"Now building Stage "+currentStageNumber+"\n"
                        +"Type the position of the card to add to the stage or 'q' to finish this stage."+displayQuestStages();
            }
        } else {
            try {
                int position = Integer.parseInt(input) - 1;
                if (position < 0 || position >= sponsor.hand.size()) {
                    return "Invalid card position. Please select a valid card from your hand.";
                }
                Card selectedCard = sponsor.hand.remove(position);
                currentStage.add(selectedCard);
                return sponsor.displayHand()
                        +"Card added to stage: " + selectedCard.toString() + "\n"
                        +"Type the position of another card to add, or 'q' to finish this stage."+displayQuestStages();
            } catch (NumberFormatException e) {
                return "Invalid input. Please enter a card position or 'q' to finish the stage.";
            }
        }
    }
    public int calculateStageValue(List<Card> stageCards){
        int totalValue = 0;
        for(Card card : stageCards){
            totalValue += card.getValue();
        }
        return totalValue;
    }
    public boolean isValidStage(List<Card> stage) {
        int foeCount = 0;
        Set<String> weaponNames = new HashSet<>();
        for (Card card : stage) {
            if (card.getType().equals("Foe")) {
                foeCount++;
            } else if (card.getType().equals("Weapon")) {
                if (weaponNames.contains(card.getName())) {
                    return false; // Duplicate weapon found
                }
                weaponNames.add(card.getName());
            }
        }
        return foeCount == 1; // Must have exactly one Foe card
    }
    public String displayQuestStages() {
        StringBuilder stagesInfo = new StringBuilder("\n--- Quest Stages ---\n");
        for (int i = 0; i < questStages.size(); i++) {
            //stagesInfo.append("Stage ").append(i + 1).append(": ");
            stagesInfo.append("--- Stage ").append(i+1).append(" ---\n");
            for (Card card : questStages.get(i)) {
                stagesInfo.append(card.toString()).append("\n");
            }
        }
        if (!currentStage.isEmpty()) {
            stagesInfo.append("--- Building Stage ---\n");
            for (Card card : currentStage) {
                stagesInfo.append(card.toString()).append("\n");
            }
        }
        stagesInfo.append("---------------------\n");
        return stagesInfo.toString();
    }
    public String getParticipants(String input) {
        if (input.equals("start")) {
            currentPlayer = participants.get(0);
            currentStage = questStages.get(0);
            return "P" + currentPlayer.number + ", do you want to participate? (y/n)";
        } else if (input.equalsIgnoreCase("y")) {
            goNextPlayer(participants);
            if (currentPlayer == participants.get(0)) {
                gameState = "PARTICIPANTS_DRAW";
                return "Participants finalized: " + displayPlayers(participants) + " (type 'start' to continue)";
            }
            return "P" + currentPlayer.number + ", do you want to participate? (y/n)";
        } else if (input.equalsIgnoreCase("n")) {
            int currentIndex = participants.indexOf(currentPlayer);
            participants.remove(currentPlayer);
            if (participants.isEmpty()) {
                gameState = "DRAW_EVENT_CARD";
                return "No players are participating. Quest is finished. (type 'start' to continue)";
            }
            if (currentIndex == participants.size()) {
                gameState = "PARTICIPANTS_DRAW";
                return "Participants finalized: " + displayPlayers(participants) + " (type 'start' to continue)";
            }
            currentPlayer = participants.get(currentIndex);
            return "P" + currentPlayer.number + ", do you want to participate? (y/n)";
        }
        return "Invalid input. (type 'start' to continue or y/n)";
    }
    public String displayPlayers(List<Player> playersList) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < playersList.size(); i++) {
            Player p = playersList.get(i);
            sb.append("P").append(p.number);
            if (i < playersList.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
    public String buildAttack(String input) {
        if (input.equals("q")) {
            if (currentAttack.isEmpty()) {
                return "An attack cannot be empty. Please add at least one card.";
            } else {
                attacks.add(new ArrayList<>(currentAttack));
                currentAttack.clear();
                for(List<Card> attack : attacks){
                    for(Card card : attack){
                        System.out.println(card.getValue());
                    }
                }
                if(participants.size() == attacks.size()){
                    gameState = "EVALUATE_ATTACKS";
                    return "Finished building attacks (type 'start' to continue)";
                }
                goNextPlayer(participants);
                StringBuilder attackPrompt = new StringBuilder();
                attackPrompt.append("P").append(currentPlayer.number).append(", build your attack for Stage ")
                        .append(currentStageNumber)
                        .append(". Type the position of the card to add, or 'q' to finish your attack.");
                return currentPlayer.displayHand() + attackPrompt.toString();
            }
        } else {
            try {
                int position = Integer.parseInt(input) - 1;
                if (position < 0 || position >= currentPlayer.hand.size()) {
                    return "Invalid card position. Please select a valid card from your hand.";
                }
                Card selectedCard = currentPlayer.hand.remove(position);
                currentAttack.add(selectedCard);
                return currentPlayer.displayHand() + "\nCard added to attack: " + selectedCard.toString()
                        + "\nType the position of another card to add, or 'q' to finish your attack.\n"
                        + displayCurrentAttack();
            } catch (NumberFormatException e) {
                return "Invalid input. Please enter a card position or 'q' to finish your attack.";
            }
        }
    }
    public String displayCurrentAttack() {
        StringBuilder attackInfo = new StringBuilder("\n--- Current Attack ---\n");
        if (!currentAttack.isEmpty()) {
            for (Card card : currentAttack) {
                attackInfo.append(card.toString()).append("\n");
            }
        } else {
            attackInfo.append("(No cards added yet)\n");
        }
        attackInfo.append("----------------------\n");
        return attackInfo.toString();
    }


    /*
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

     */
}
