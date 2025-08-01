package org.example;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.PrintStream;

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
            System.out.print("P" + players.get(playerTurn).number + "'s turn (Press 'Enter' to continue...)");
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
        Scanner scanner = new Scanner(System.in);
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
                currentPlayer.draw(adventureDeck, scanner);
                currentPlayer.draw(adventureDeck, scanner);
                assertEquals(2, oldSize - adventureDeck.getSize());
            } else if (Objects.equals(card.getType(), "Event") && card.getValue() == 3) {
                int oldSize = adventureDeck.getSize();
                for (Player player : players) {
                    player.draw(adventureDeck, scanner);
                    player.draw(adventureDeck, scanner);
                }
                assertEquals(8, oldSize - adventureDeck.getSize());
            }
        }
    }

    @Test
    public void RESP_6_test_1() {
        AdventureDeck adventureDeck = new AdventureDeck();
        Player player = new Player(1);
        String simulatedInput = "\n1\n";
        Scanner scanner = new Scanner(simulatedInput);
        for(int i = 0; i < 12; i++){
            player.draw(adventureDeck, scanner);
        }
        player.draw(adventureDeck, scanner); //Player hand should be full and trimmed functionality handled in draw function
        assertEquals(12, player.hand.size());
        assertEquals(1, adventureDeck.getDiscardSize());
    }

    @Test
    public void RESP_7_test_1() {
        List<Player> players = new ArrayList<>();
        players.add(new Player(1));
        players.add(new Player(2));
        players.add(new Player(3));
        players.add(new Player(4));

        Player currentPlayer = players.get(2); //player 3's turn

        String simulatedInput = "n\nn\nn\nn\n"; // All players decline (y/n inputs)
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        Scanner scanner = new Scanner(inputStream);

        Player sponsor = currentPlayer.sponsor(players, scanner);
        assertNull(sponsor);
    }

    @Test
    public void RESP_8_test_1() {
        Player sponsor = new Player(1);
        List<List<Card>> quest = new ArrayList<>();
        AdventureDeck adventureDeck = new AdventureDeck();
        String simulatedInput = "q";
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        Scanner scanner = new Scanner(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        for(int i = 0; i < 12; i++){
            sponsor.draw(adventureDeck, scanner);
        }

        Thread buildStageThread = new Thread(()->{
            sponsor.buildStage(quest, scanner);
        });
        buildStageThread.start();
        while(true){
            String gameOutput = outputStream.toString();
            if(gameOutput.contains("Select position of the card to include in stage or type 'q' to quit building: ")) {
                System.setOut(originalOut);
                assertTrue(true);
                return;
            }
        }
    }

    @Test
    public void RESP_9_test_1() {
        Player sponsor = new Player(1);
        AdventureDeck adventureDeck = new AdventureDeck();
        List<List<Card>> quest = new ArrayList<>();

        String simulatedInput = "13\n1\nq\n";
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        Scanner scanner = new Scanner(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        for (int i = 0; i < 12; i++) {
            sponsor.draw(adventureDeck, scanner);
        }
        sponsor.buildStage(quest, scanner);
        System.setOut(originalOut);

        String gameOutput = outputStream.toString();

        assertTrue(gameOutput.contains("Invalid position, please enter a valid card index."));
        assertTrue(gameOutput.contains("Card added to stage:"));
    }

    @Test
    public void RESP_10_test_1() {
        Player sponsor = new Player(1);
        AdventureDeck adventureDeck = new AdventureDeck();
        List<List<Card>> quest = new ArrayList<>();

        String simulatedInput = "1\nq\n";
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        Scanner scanner = new Scanner(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        for (int i = 0; i < 12; i++) {
            sponsor.draw(adventureDeck, scanner);
        }

        List<Card> stageCards = sponsor.buildStage(quest, scanner);

        System.setOut(originalOut);
        String gameOutput = outputStream.toString();

        assertTrue(gameOutput.contains("Card added to stage:"));
        assertEquals(1, stageCards.size());
    }

    @Test
    public void RESP_11_test_1() {
        Player sponsor = new Player(1);
        List<List<Card>> quest = new ArrayList<>();
        AdventureDeck adventureDeck = new AdventureDeck();

        String simulatedInput = "q\n1\nq\n";
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        Scanner scanner = new Scanner(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        for (int i = 0; i < 12; i++) {
            sponsor.draw(adventureDeck, scanner);
        }
        sponsor.buildStage(quest, scanner);

        System.setOut(originalOut);
        String gameOutput = outputStream.toString();

        assertTrue(gameOutput.contains("A stage cannot be empty"));
    }

    @Test
    public void RESP_12_test_1() {
        Player sponsor = new Player(1);

        String simulatedInput = "1\nq\n1\n2\nq";
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        Scanner scanner = new Scanner(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        sponsor.hand.add(new Card("Foe",5));
        sponsor.hand.add(new Card("Weapon",10, "Sword"));
        sponsor.hand.add(new Card("Weapon",30, "Excalibur"));

        List<Card> previousStage = new ArrayList<>();
        previousStage.add(new Card("Foe", 10));
        previousStage.add(new Card("Weapon", 10, "Sword"));
        List<List<Card>> quest = new ArrayList<>();
        quest.add(previousStage);

        List<Card> stageCards = sponsor.buildStage(quest, scanner);

        System.setOut(originalOut);
        String gameOutput = outputStream.toString();
        assertTrue(gameOutput.contains("Insufficient value for this stage"));

    }

    @Test
    public void RESP_13_test_1() {
        Player sponsor = new Player(1);
        String simulatedInput = "1\n2\nq\n1\n1\nq\n";
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        Scanner scanner = new Scanner(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        sponsor.hand.add(new Card("Foe",5));
        sponsor.hand.add(new Card("Foe",10));
        sponsor.hand.add(new Card("Weapon",10, "Sword"));
        sponsor.hand.add(new Card("Weapon",30, "Excalibur"));

        List<List<Card>> quest = new ArrayList<>();
        for(int i = 0; i < 2; i++){ //making two stages
            quest.add(sponsor.buildStage(quest, scanner));
        }
        sponsor.printQuest(quest);
        System.setOut(originalOut);
        String gameOutput = outputStream.toString();
        assertTrue(gameOutput.contains("Stage 1"));
        assertTrue(gameOutput.contains("Stage 2"));
    }

    @Test
    public void RESP_14_test_1() {
        List<Player> players = new ArrayList<>();
        String simulatedInput = "y\nn\ny\n";
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        Scanner scanner = new Scanner(inputStream);
        for (int i = 0; i < 4; i++) {
            players.add(new Player(i + 1));
        }
        Player sponsor = players.get(0);
        List<Player> questParticipants = sponsor.getParticipants(players, scanner);
        assertEquals(2, questParticipants.size());
    }

    @Test
    public void RESP_15_test_1() {
        String simulatedInput = "t\nq\nw\nt\nq\nw\nt\nq\nt\nq\n";
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        Scanner scanner = new Scanner(inputStream);

        AdventureDeck adventureDeck = new AdventureDeck();
        Player sponsor = new Player(1); //assume first player is sponsor
        List<Player> questParticipants = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            questParticipants.add(new Player(i + 1));
        }
        List<List<Card>> quest = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            List<Card> stage = new ArrayList<>();
            quest.add(stage);
        }
        for(List<Card> stage : quest){
            sponsor.stageStart(adventureDeck, stage, questParticipants, scanner);
        }
        assertEquals(1,questParticipants.size());
        assertEquals(4, questParticipants.get(0).number); //Player 4 should be the last remaining player
    }

    @Test
    public void RESP_16_test_1() {
        String simulatedInput = "t\nq\n";
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        Scanner scanner = new Scanner(inputStream);

        AdventureDeck adventureDeck = new AdventureDeck();
        Player sponsor = new Player(1); //assume first player is sponsor
        List<Player> questParticipants = new ArrayList<>();
        questParticipants.add(new Player(2));

        List<Card> stage = new ArrayList<>();
        sponsor.stageStart(adventureDeck, stage, questParticipants, scanner);

        assertEquals(1,questParticipants.getFirst().hand.size()); //card drawn and hand was empty so expecting 1 card
    }

    @Test
    public void RESP_17_test_1() {
        String simulatedInput = "w\n";
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        Scanner scanner = new Scanner(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        AdventureDeck adventureDeck = new AdventureDeck();
        Player sponsor = new Player(1); //assume first player is sponsor
        List<Player> questParticipants = new ArrayList<>();
        questParticipants.add(new Player(2));

        List<Card> stage = new ArrayList<>();
        sponsor.stageStart(adventureDeck, stage, questParticipants, scanner);

        System.setOut(originalOut);
        String gameOutput = outputStream.toString();
        assertTrue(gameOutput.contains("End of Quest"));
    }

    @Test
    public void RESP_18_test_1() {
        String simulatedInput = "1\n" +
                                "2\n" +
                                "1\n" +
                                "3\n" +
                                "q\n";  // Selecting cards for attack
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        Scanner scanner = new Scanner(inputStream);

        List<Card> stage = new ArrayList<>();
        stage.add(new Card("Foe",5));
        stage.add(new Card("Weapon",15, "Battle-Axe"));

        Player player = new Player(1);
        player.hand.add(new Card("Foe",5));
        player.hand.add(new Card("Weapon",5, "Dagger"));
        player.hand.add(new Card("Weapon",30, "Excalibur"));

        List<Card> attack = player.prepareAttack(scanner);

        assertEquals(1,attack.size());
    }

    @Test
    public void RESP_19_test_1() {
        String simulatedInput = "2\n" +
                                "2\n" +
                                "1\n" +
                                "2\n" +
                                "2\n" +
                                "abc\n" +
                                "q\n";
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        Scanner scanner = new Scanner(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        Player player = new Player(1);
        player.hand.add(new Card("Foe", 5));  // Foe card (not selectable)
        player.hand.add(new Card("Weapon", 5, "Dagger"));  // Weapon 1
        player.hand.add(new Card("Weapon", 30, "Excalibur"));  // Weapon 2

        List<Card> stage = new ArrayList<>();

        List<Card> attack = player.prepareAttack(scanner);

        System.setOut(originalOut);

        String gameOutput = outputStream.toString();

        assertEquals(2, attack.size());
        assertEquals("Dagger", attack.get(0).getLabel());
        assertEquals("Excalibur", attack.get(1).getLabel());

        assertTrue(gameOutput.contains("Added Dagger (Value: 5) to your attack."));
        assertTrue(gameOutput.contains("Added Excalibur (Value: 30) to your attack."));
        assertTrue(gameOutput.contains("Invalid choice or repeated weapon. Please select a different weapon."));
        assertTrue(gameOutput.contains("Invalid input. Please enter a number."));
    }

    @Test
    public void RESP_20_test_1() {
        String simulatedInput = "t\n" +
                                "2\n" +
                                "q\n" +
                                "t\n" +
                                "2\n" +
                                "q\n" +
                                "t\n" +
                                "2\n" +
                                "q\n";
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        Scanner scanner = new Scanner(inputStream);

        Player sponsor = new Player(1);
        List<Player> players = new ArrayList<>();
        players.add(new Player(2));
        players.add(new Player(3));
        players.add(new Player(4));

        List<Card> stage = new ArrayList<>();
        stage.add(new Card("Foe", 20));

        players.get(0).hand.add(new Card("Weapon", 15, "Sword"));
        players.get(0).hand.add(new Card("Weapon", 10, "Horse"));
        players.get(1).hand.add(new Card("Weapon", 5, "Dagger"));
        players.get(1).hand.add(new Card("Weapon", 5, "Dagger"));
        players.get(2).hand.add(new Card("Weapon", 30, "Excalibur"));

        AdventureDeck adventureDeck = new AdventureDeck();

        sponsor.stageStart(adventureDeck, stage, players, scanner);

        assertEquals(1, players.size()); // Only 1 Player should remain
        assertEquals(4, players.get(0).number); // Player 4 is still in
    }

    @Test
    public void RESP_21_test_1() {
        String simulatedInput = "t\n" +
                                "2\n" +
                                "q\n" +
                                "t\n" +
                                "2\n" +
                                "q\n" +
                                "t\n" +
                                "2\n" +
                                "q\n";
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        Scanner scanner = new Scanner(inputStream);

        Player sponsor = new Player(1);
        List<Player> players = new ArrayList<>();
        players.add(new Player(2));
        players.add(new Player(3));
        players.add(new Player(4));

        List<Card> stage = new ArrayList<>();
        stage.add(new Card("Foe", 20)); // Stage value is 20

        AdventureDeck adventureDeck = new AdventureDeck();

        players.get(0).hand.add(new Card("Weapon", 15, "Sword"));

        players.get(1).hand.add(new Card("Weapon", 5, "Dagger"));

        players.get(2).hand.add(new Card("Weapon", 30, "Excalibur"));

        List<Player> participants = new ArrayList<>();
        participants.addAll(players);

        sponsor.stageStart(adventureDeck, stage, participants, scanner);

        int discardSizeAfterStage = adventureDeck.getDiscardSize();

        assertFalse(players.get(0).hand.contains(new Card("Weapon", 15, "Sword")));  // Sword should be discarded
        assertFalse(players.get(1).hand.contains(new Card("Weapon", 5, "Dagger")));  // Dagger should be discarded
        assertFalse(players.get(2).hand.contains(new Card("Weapon", 30, "Excalibur")));  // Excalibur should be discarded
    }

    @Test
    public void RESP_22_test_1() {
        String simulatedInput = "t\n" +
                                "2\n" +
                                "q\n" +
                                "t\n" +
                                "2\n" +
                                "q\n" +
                                "t\n" +
                                "2\n" +
                                "q\n";
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        Scanner scanner = new Scanner(inputStream);

        Player sponsor = new Player(1);
        List<Player> players = new ArrayList<>();
        players.add(new Player(2));
        players.add(new Player(3));
        players.add(new Player(4));

        List<Card> stage = new ArrayList<>();
        stage.add(new Card("Foe", 20)); // Stage value is 20

        AdventureDeck adventureDeck = new AdventureDeck();

        players.get(0).hand.add(new Card("Weapon", 15, "Sword"));

        players.get(1).hand.add(new Card("Weapon", 5, "Dagger"));

        players.get(2).hand.add(new Card("Weapon", 30, "Excalibur"));

        List<Player> participants = new ArrayList<>();
        participants.addAll(players);

        sponsor.stageStart(adventureDeck, stage, participants, scanner);

        assertEquals(adventureDeck.getDiscardSize()-3, stage.size());
    }

    @Test
    public void RESP_23_test_1() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        List<Player> players = new ArrayList<>();
        players.add(new Player(1));
        players.add(new Player(2));
        players.add(new Player(3));
        players.add(new Player(4));
        players.get(0).shields = 6;
        players.get(1).shields = 5;
        players.get(2).shields = 4;
        players.get(3).shields = 2;

        List<Player> winners = new ArrayList<>();
        boolean winnerFlag = false;
        while (!winnerFlag){
            //imaginary round where shields get added
            for(Player player : players){
                player.shields++;
                player.checkIfWinner(winners);
            }
            if(!winners.isEmpty()){
                winnerFlag = true;
            }
        }
        System.out.println("Winner(s):");
        for (Player winner : winners) {
            winner.printWinner();
        }
        System.out.println("Game Over.");

        System.setOut(originalOut);

        String gameOutput = outputStream.toString();
        assertTrue(gameOutput.contains("Player 1 with 7 shields."));
    }


}