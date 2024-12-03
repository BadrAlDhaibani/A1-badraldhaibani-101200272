package org.example;

import io.cucumber.java.en.*;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class StepDefinitions {
    private Game game;
    //for simplicity of rigging
    Player P1;
    Player P2;
    Player P3;
    Player P4;
    Card eventDrawn;
    Player sponsor;
    List<List<Card>> quest = new ArrayList<>();
    List<Player> participants = new ArrayList<>();
    List<List<Card>> Attacks = new ArrayList<>();

    @Given("A new game is started")
    public void A_new_game_is_started(){
        game = new Game();
        P1 = game.players.get(0);
        P2 = game.players.get(1);
        P3 = game.players.get(2);
        P4 = game.players.get(3);
    }
    @And("the decks are created")
    public void the_decks_are_created(){
        game.createDecks();
    }
    @Then("the hands of the 4 players are set up with random cards")
    public void the_hands_of_the_4_players_are_set_up_with_random_cards(){
        game.assignHands();
    }
    @Then("the 4 hands are rigged to hold the specific initial cards")
    public void the_4_hands_are_rigged_to_hold_the_specific_initial_cards(){
        //P1 Hand
        P1.hand.clear();
        P1.hand.add(new Card("Foe",5));
        P1.hand.add(new Card("Foe",5));
        P1.hand.add(new Card("Foe",15));
        P1.hand.add(new Card("Foe",15));
        P1.hand.add(new Card("Weapon",5,"Dagger"));
        P1.hand.add(new Card("Weapon",10,"Sword"));
        P1.hand.add(new Card("Weapon",10,"Sword"));
        P1.hand.add(new Card("Weapon",10,"Horse"));
        P1.hand.add(new Card("Weapon",10,"Horse"));
        P1.hand.add(new Card("Weapon",15,"Battle-Axe"));
        P1.hand.add(new Card("Weapon",15,"Battle-Axe"));
        P1.hand.add(new Card("Weapon",20,"Lance"));
        //P2 Hand
        P2.hand.clear();
        P2.hand.add(new Card("Foe",5));
        P2.hand.add(new Card("Foe",5));
        P2.hand.add(new Card("Foe",15));
        P2.hand.add(new Card("Foe",15));
        P2.hand.add(new Card("Foe",40));
        P2.hand.add(new Card("Weapon",5,"Dagger"));
        P2.hand.add(new Card("Weapon",10,"Sword"));
        P2.hand.add(new Card("Weapon",10,"Horse"));
        P2.hand.add(new Card("Weapon",10,"Horse"));
        P2.hand.add(new Card("Weapon",15,"Battle-Axe"));
        P2.hand.add(new Card("Weapon",15,"Battle-Axe"));
        P2.hand.add(new Card("Weapon",30,"Excalibur"));
        //P3 Hand
        P3.hand.clear();
        P3.hand.add(new Card("Foe",5));
        P3.hand.add(new Card("Foe",5));
        P3.hand.add(new Card("Foe",5));
        P3.hand.add(new Card("Foe",15));
        P3.hand.add(new Card("Weapon",5,"Dagger"));
        P3.hand.add(new Card("Weapon",10,"Sword"));
        P3.hand.add(new Card("Weapon",10,"Sword"));
        P3.hand.add(new Card("Weapon",10,"Sword"));
        P3.hand.add(new Card("Weapon",10,"Horse"));
        P3.hand.add(new Card("Weapon",10,"Horse"));
        P3.hand.add(new Card("Weapon",15,"Battle-Axe"));
        P3.hand.add(new Card("Weapon",20,"Lance"));
        //P4 Hand
        P4.hand.clear();
        P4.hand.add(new Card("Foe",5));
        P4.hand.add(new Card("Foe",15));
        P4.hand.add(new Card("Foe",15));
        P4.hand.add(new Card("Foe",40));
        P4.hand.add(new Card("Weapon",5,"Dagger"));
        P4.hand.add(new Card("Weapon",5,"Dagger"));
        P4.hand.add(new Card("Weapon",10,"Sword"));
        P4.hand.add(new Card("Weapon",10,"Horse"));
        P4.hand.add(new Card("Weapon",10,"Horse"));
        P4.hand.add(new Card("Weapon",15,"Battle-Axe"));
        P4.hand.add(new Card("Weapon",20,"Lance"));
        P4.hand.add(new Card("Weapon",30,"Excalibur"));
    }
    @When("P1 draws a quest of 4 stages")
    public void P1_draws_a_quest_of_4_stages(){
        String simulatedInput = "\n";
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        game.setScannerInput(inputStream);
        eventDrawn = game.eventCardDraw();
        eventDrawn = new Card("Quest",4);
    }
    @And("P1 is asked but declines to sponsor P2 is asked but agrees to sponsor")
    public void P1_is_asked_but_declines_to_sponsor_P2_is_asked_but_agrees_to_sponsor(){
        String simulatedInput = "n\ny\n";
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        game.setScannerInput(inputStream);
        sponsor = game.findSponsor(eventDrawn);
        assertEquals(2, sponsor.number);
    }
    @Then("P2 builds the 4 stages of the quest as specified")
    public void P2_builds_the_4_stages_of_the_quest_as_specified(){
        String simulatedInput = "1\n" +
                                "7\n" +
                                "q\n" +
                                "2\n" +
                                "5\n" +
                                "q\n" +
                                "2\n" +
                                "3\n" +
                                "4\n" +
                                "q\n" +
                                "2\n" +
                                "3\n" +
                                "q\n";
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        game.setScannerInput(inputStream);
        quest = game.questStages(eventDrawn, sponsor);
        //Stage 1 assert
        assertEquals("F5 (Value: 5)", quest.get(0).get(0).toString());
        assertEquals("Horse (Value: 10)", quest.get(0).get(1).toString());
        //Stage 2 assert
        assertEquals("F15 (Value: 15)", quest.get(1).get(0).toString());
        assertEquals("Sword (Value: 10)", quest.get(1).get(1).toString());
        //Stage 3 assert
        assertEquals("F15 (Value: 15)", quest.get(2).get(0).toString());
        assertEquals("Dagger (Value: 5)", quest.get(2).get(1).toString());
        assertEquals("Battle-Axe (Value: 15)", quest.get(2).get(2).toString());
        //Stage 4 assert
        assertEquals("F40 (Value: 40)", quest.get(3).get(0).toString());
        assertEquals("Battle-Axe (Value: 15)", quest.get(3).get(1).toString());
    }
    @Then("P1 is asked and decides to participate and draws an F30 and discards an F5")
    public void P1_is_asked_and_decides_to_participate_and_draws_an_F30_and_discards_an_F5(){
        String simulatedInput = "y\n" +
                                "\n" +
                                "1\n";
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        game.setScannerInput(inputStream);
        if(game.askParticipant(P1)){
            Card drawnCard = game.drawAdventureCard();
            drawnCard = new Card("Foe",30); //Rigged drawn card
            game.playerHandlesDrawnCard(drawnCard, P1);
            participants.add(P1);
        }
    }

    @Then("P3 is asked and decides to participate and draws a Sword and discards an F5")
    public void Then_P3_is_asked_and_decides_to_participate_and_draws_a_Sword_and_discards_an_F5(){
        String simulatedInput = "y\n" +
                                "\n" +
                                "1\n";
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        game.setScannerInput(inputStream);
        if(game.askParticipant(P3)){
            Card drawnCard = game.drawAdventureCard();
            drawnCard = new Card("Weapon",10,"Sword"); //Rigged drawn card
            game.playerHandlesDrawnCard(drawnCard, P3);
            participants.add(P3);
        }
    }

    @Then("P4 is asked and decides to participate and draws an Axe and discards an F5")
    public void Then_P4_is_asked_and_decides_to_participate_and_draws_an_Axe_and_discards_an_F5(){
        String simulatedInput = "y\n" +
                                "\n" +
                                "1\n";
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        game.setScannerInput(inputStream);
        if(game.askParticipant(P4)){
            Card drawnCard = game.drawAdventureCard();
            drawnCard = new Card("Weapon",15, "Battle-Axe"); //Rigged drawn card
            game.playerHandlesDrawnCard(drawnCard, P4);
            participants.add(P4);
        }
    }

    @Then("P1 builds an attack with Dagger and Sword total value 15")
    public void P1_builds_an_attack_with_Dagger_and_Sword_total_value_15(){
        String simulatedInput = "5\n" +
                                "5\n" +
                                "q\n";
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        game.setScannerInput(inputStream);
        List<Card> P1_attack = P1.prepareAttack(game.scanner);
        Attacks.add(P1_attack);
        assertEquals("Dagger", P1_attack.get(0).getLabel());
        assertEquals("Sword", P1_attack.get(1).getLabel());
    }

    @Then("P3 builds an attack with Sword and Dagger total value 15")
    public void P3_builds_an_attack_with_Sword_and_Dagger_total_value_15(){
        String simulatedInput = "5\n" +
                                "4\n" +
                                "q\n";
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        game.setScannerInput(inputStream);
        List<Card> P3_attack = P3.prepareAttack(game.scanner);
        Attacks.add(P3_attack);
        assertEquals("Sword", P3_attack.get(0).getLabel());
        assertEquals("Dagger", P3_attack.get(1).getLabel());
    }

    @Then("P4 builds an attack with Dagger and Horse total value 15")
    public void P4_builds_an_attack_with_Dagger_and_Horse_total_value_15(){
        String simulatedInput = "4\n" +
                                "6\n" +
                                "q\n";
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        game.setScannerInput(inputStream);
        List<Card> P4_attack = P4.prepareAttack(game.scanner);
        Attacks.add(P4_attack);
        assertEquals("Dagger", P4_attack.get(0).getLabel());
        assertEquals("Horse", P4_attack.get(1).getLabel());
    }

    @Then("all 3 players attacks are sufficient and proceed to the next stage")
    public void all_3_players_attacks_are_sufficient_and_proceed_to_the_next_stage(){
        List<Integer> goodAttacks = game.checkAttacks(Attacks, quest.get(0));
        participants = game.stageWinners(participants, goodAttacks);
        assertEquals(3, participants.size());
    }

    @And("all 3 participants discard the cards used for their attacks1")
    public void all_3_participants_discard_the_cards_used_for_their_attacks1(){
        participants.clear();
        Attacks.clear();
        assertEquals(10, P1.hand.size());
        assertEquals(10, P3.hand.size());
        assertEquals(10, P4.hand.size());
    }

    @Then("P1 is asked and decides to participate P1 draws a F10")
    public void P1_is_asked_and_decides_to_participate_P1_draws_a_F10(){
        String simulatedInput = "y\n" +
                                "\n";
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        game.setScannerInput(inputStream);
        if(game.askParticipant(P1)){
            Card drawnCard = game.drawAdventureCard();
            drawnCard = new Card("Foe",10); //Rigged drawn card
            game.playerHandlesDrawnCard(drawnCard, P1);
            participants.add(P1);
        }
    }

    @Then("P3 is asked and decides to participate P3 draws a Lance")
    public void P3_is_asked_and_decides_to_participate_P3_draws_a_Lance(){
        String simulatedInput = "y\n" +
                                "\n";
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        game.setScannerInput(inputStream);
        if(game.askParticipant(P3)){
            Card drawnCard = game.drawAdventureCard();
            drawnCard = new Card("Weapon",20, "Lance"); //Rigged drawn card
            game.playerHandlesDrawnCard(drawnCard, P3);
            participants.add(P3);
        }
    }

    @Then("P4 is asked and decides to participate P4 draws a Lance")
    public void P4_is_asked_and_decides_to_participate_P4_draws_a_Lance(){
        String simulatedInput = "y\n" +
                                "\n";
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        game.setScannerInput(inputStream);
        if(game.askParticipant(P4)){
            Card drawnCard = game.drawAdventureCard();
            drawnCard = new Card("Weapon",20, "Lance"); //Rigged drawn card
            game.playerHandlesDrawnCard(drawnCard, P4);
            participants.add(P4);
        }
    }

    @Then("P1 builds an attack with Horse and Sword")
    public void P1_builds_an_attack_with_Horse_and_Sword(){
        String simulatedInput = "7\n" +
                                "6\n" +
                                "q\n";
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        game.setScannerInput(inputStream);
        List<Card> P1_attack = P1.prepareAttack(game.scanner);
        Attacks.add(P1_attack);
        assertEquals("Horse", P1_attack.get(0).getLabel());
        assertEquals("Sword", P1_attack.get(1).getLabel());
    }

    @Then("P3 builds an attack with Axe and Sword")
    public void P3_builds_an_attack_with_Axe_and_Sword(){
        String simulatedInput = "9\n" +
                                "4\n" +
                                "q\n";
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        game.setScannerInput(inputStream);
        List<Card> P3_attack = P3.prepareAttack(game.scanner);
        Attacks.add(P3_attack);
        assertEquals("Battle-Axe", P3_attack.get(0).getLabel());
        assertEquals("Sword", P3_attack.get(1).getLabel());
    }

    @Then("P4 builds an attack with Horse and Axe")
    public void P4_builds_an_attack_with_Horse_and_Axe(){
        String simulatedInput = "6\n" +
                                "6\n" +
                                "q\n";
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        game.setScannerInput(inputStream);
        List<Card> P4_attack = P4.prepareAttack(game.scanner);
        Attacks.add(P4_attack);
        assertEquals("Horse", P4_attack.get(0).getLabel());
        assertEquals("Battle-Axe", P4_attack.get(1).getLabel());
    }

    @Then("P1s attack is insufficient P1 loses and cannot go to the next stage")
    public void P1s_attack_is_insufficient_P1_loses_and_cannot_go_to_the_next_stage(){
        List<Integer> goodAttacks = game.checkAttacks(Attacks, quest.get(1));
        participants = game.stageWinners(participants, goodAttacks);
        assertEquals(2, participants.size());
        assertNotEquals(1, participants.get(0).number);
        assertNotEquals(1, participants.get(1).number);
    }

    @And("P1 has no shields and their hand is F5 F10 F15 F15 F30 Horse Axe Axe Lance")
    public void P1_has_no_shields_and_their_hand_is_F5_F10_F15_F15_F30_Horse_Axe_Axe_Lance(){
        assertEquals(0, P1.shields);
        assertEquals("F5 (Value: 5)", P1.hand.get(0).toString());
        assertEquals("F10 (Value: 10)", P1.hand.get(1).toString());
        assertEquals("F15 (Value: 15)", P1.hand.get(2).toString());
        assertEquals("F15 (Value: 15)", P1.hand.get(3).toString());
        assertEquals("F30 (Value: 30)", P1.hand.get(4).toString());
        assertEquals("Horse (Value: 10)", P1.hand.get(5).toString());
        assertEquals("Battle-Axe (Value: 15)", P1.hand.get(6).toString());
        assertEquals("Battle-Axe (Value: 15)", P1.hand.get(7).toString());
        assertEquals("Lance (Value: 20)", P1.hand.get(8).toString());
    }

    @And("P3 and P4s attack are sufficient and proceed to the next stage")
    public void P3_and_P4s_attack_are_sufficient_and_proceed_to_the_next_stage(){
        assertEquals(2, participants.size());
        assertEquals(3, participants.get(0).number);
        assertEquals(4, participants.get(1).number);
    }

    @And("all 3 participants discard the cards used for their attacks2")
    public void all_3_participants_discard_the_cards_used_for_their_attacks2(){
        participants.clear();
        Attacks.clear();
        assertEquals(9, P1.hand.size());
        assertEquals(9, P3.hand.size());
        assertEquals(9, P4.hand.size());
    }

    @Then("P3 is asked and decides to participate P3 draws an Axe")
    public void P3_is_asked_and_decides_to_participate_P3_draws_an_Axe(){
        String simulatedInput = "y\n" +
                                "\n";
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        game.setScannerInput(inputStream);
        if(game.askParticipant(P3)){
            Card drawnCard = game.drawAdventureCard();
            drawnCard = new Card("Weapon",15, "Battle-Axe"); //Rigged drawn card
            game.playerHandlesDrawnCard(drawnCard, P3);
            participants.add(P3);
        }
    }

    @Then("P4 is asked and decides to participate P4 draws a Sword")
    public void P4_is_asked_and_decides_to_participate_P4_draws_a_Sword(){
        String simulatedInput = "y\n" +
                                "\n";
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        game.setScannerInput(inputStream);
        if(game.askParticipant(P4)){
            Card drawnCard = game.drawAdventureCard();
            drawnCard = new Card("Weapon",10, "Sword"); //Rigged drawn card
            game.playerHandlesDrawnCard(drawnCard, P4);
            participants.add(P4);
        }
    }

    @Then("P3 builds an attack with Lance Horse and Sword")
    public void P3_builds_an_attack_with_Lance_Horse_and_Sword(){
        String simulatedInput = "9\n" +
                                "7\n" +
                                "5\n" +
                                "q\n";
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        game.setScannerInput(inputStream);
        List<Card> P3_attack = P3.prepareAttack(game.scanner);
        Attacks.add(P3_attack);
        assertEquals("Lance", P3_attack.get(0).getLabel());
        assertEquals("Horse", P3_attack.get(1).getLabel());
        assertEquals("Sword", P3_attack.get(2).getLabel());
    }

    @Then("P4 builds an attack with Axe Sword and Lance")
    public void P4_builds_an_attack_with_Axe_Sword_and_Lance(){
        String simulatedInput = "7\n" +
                "6\n" +
                "7\n" +
                "q\n";
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        game.setScannerInput(inputStream);
        List<Card> P4_attack = P4.prepareAttack(game.scanner);
        Attacks.add(P4_attack);
        assertEquals("Battle-Axe", P4_attack.get(0).getLabel());
        assertEquals("Sword", P4_attack.get(1).getLabel());
        assertEquals("Lance", P4_attack.get(2).getLabel());
    }

    @Then("P3 and P4s attacks are sufficient and proceed to the next stage")
    public void P3_and_P4s_attacks_are_sufficient_and_proceed_to_the_next_stage(){
        List<Integer> goodAttacks = game.checkAttacks(Attacks, quest.get(2));
        participants = game.stageWinners(participants, goodAttacks);
        assertEquals(2, participants.size());
        assertEquals(3, participants.get(0).number);
        assertEquals(4, participants.get(1).number);
    }

    @And("All 2 participants discard the cards used for their attacks")
    public void All_2_participants_discard_the_cards_used_for_their_attacks(){
        participants.clear();
        Attacks.clear();
        assertEquals(7, P3.hand.size());
        assertEquals(7, P4.hand.size());
    }

    @Then("P3 is asked and decides to participate P3 draws a F30")
    public void P3_is_asked_and_decides_to_participate_P3_draws_a_F30(){
        String simulatedInput = "y\n" +
                                "\n";
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        game.setScannerInput(inputStream);
        if(game.askParticipant(P3)){
            Card drawnCard = game.drawAdventureCard();
            drawnCard = new Card("Foe",30); //Rigged drawn card
            game.playerHandlesDrawnCard(drawnCard, P3);
            participants.add(P3);
        }
    }

    @Then("P3 builds an attack with Axe Horse and Lance")
    public void P3_builds_an_attack_with_Axe_Horse_and_Lance(){
        String simulatedInput = "7\n" +
                                "6\n" +
                                "6\n" +
                                "q\n";
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        game.setScannerInput(inputStream);
        List<Card> P3_attack = P3.prepareAttack(game.scanner);
        Attacks.add(P3_attack);
        assertEquals("Battle-Axe", P3_attack.get(0).getLabel());
        assertEquals("Horse", P3_attack.get(1).getLabel());
        assertEquals("Lance", P3_attack.get(2).getLabel());
    }

    @Then("P4 builds an attack with Dagger Sword Lance and Excalibur")
    public void P4_builds_an_attack_with_Dagger_Sword_Lance_and_Excalibur(){
        String simulatedInput = "4\n" +
                                "4\n" +
                                "4\n" +
                                "5\n" +
                                "q\n";
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        game.setScannerInput(inputStream);
        List<Card> P4_attack = P4.prepareAttack(game.scanner);
        Attacks.add(P4_attack);
        assertEquals("Dagger", P4_attack.get(0).getLabel());
        assertEquals("Sword", P4_attack.get(1).getLabel());
        assertEquals("Lance", P4_attack.get(2).getLabel());
        assertEquals("Excalibur", P4_attack.get(3).getLabel());
    }

    @Then("P3s attack is insufficient P3 loses and receives no shields")
    public void P3s_attack_is_insufficient_P3_loses_and_receives_no_shields(){
        List<Integer> goodAttacks = game.checkAttacks(Attacks, quest.get(3));
        participants = game.stageWinners(participants, goodAttacks);
        assertEquals(0, P3.shields);
        assertEquals(1, participants.size());
        assertNotEquals(3, participants.get(0).number);
    }

    @And("P4s attack is sufficient P4 receives 4 shields")
    public void P4s_attack_is_sufficient_P4_receives_4_shields(){
        for(Player player : participants){
            game.questWinnerRewarded(player, quest);
        }
        assertEquals(4, P4.shields);
    }

    @And("All 2 participants discard the cards used for their attacks2")
    public void All_2_participants_discard_the_cards_used_for_their_attacks2(){
        participants.clear();
        Attacks.clear();
        assertEquals(5, P3.hand.size());
        assertEquals(4, P4.hand.size());
    }

    @And("P3 has no shields and has the cards F5 F5 F15 F30 Sword")
    public void And_P3_has_no_shields_and_has_the_cards_F5_F5_F15_F30_Sword(){
        assertEquals(0, P3.shields);
        assertEquals("F5 (Value: 5)", P3.hand.get(0).toString());
        assertEquals("F5 (Value: 5)", P3.hand.get(1).toString());
        assertEquals("F15 (Value: 15)", P3.hand.get(2).toString());
        assertEquals("F30 (Value: 30)", P3.hand.get(3).toString());
        assertEquals("Sword (Value: 10)", P3.hand.get(4).toString());
    }

    @And("P4 has 4 shields and has the cards F15 F15 F40 Lance")
    public void And_P4_has_4_shields_and_has_the_cards_F15_F15_F40_Lance(){
        assertEquals(4, P4.shields);
        assertEquals("F15 (Value: 15)", P4.hand.get(0).toString());
        assertEquals("F15 (Value: 15)", P4.hand.get(1).toString());
        assertEquals("F40 (Value: 40)", P4.hand.get(2).toString());
        assertEquals("Lance (Value: 20)", P4.hand.get(3).toString());
    }




}
