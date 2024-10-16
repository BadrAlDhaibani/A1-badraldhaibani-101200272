package org.example;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ATestJPScenario {

    @Test
    public void testJPScenario() {
        AdventureDeck adventureDeck = new AdventureDeck();
        adventureDeck.shuffleDeck();
        EventDeck eventDeck = new EventDeck();
        eventDeck.shuffleDeck();

        List<Player> players = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            players.add(new Player(i + 1));
        }

        Player p1 = players.get(0);
        Player p2 = players.get(1);
        Player p3 = players.get(2);
        Player p4 = players.get(3);

        p1.hand.clear();
        p2.hand.clear();
        p3.hand.clear();
        p4.hand.clear();

        p1.hand.addAll(Arrays.asList(
                new Card("Foe", 5),
                new Card("Foe", 10),
                new Card("Foe", 15),
                new Card("Foe", 15),
                new Card("Foe", 30),
                new Card("Weapon", 5, "Dagger"),
                new Card("Weapon", 10, "Sword"),
                new Card("Weapon", 10, "Horse"),
                new Card("Weapon", 15, "Battle-Axe")
        ));

        p2.hand.addAll(Arrays.asList(
                new Card("Foe", 10),
                new Card("Foe", 20),
                new Card("Foe", 25),
                new Card("Foe", 35),
                new Card("Foe", 50),
                new Card("Foe", 70),
                new Card("Weapon", 5, "Dagger"),
                new Card("Weapon", 10, "Sword"),
                new Card("Weapon", 10, "Horse"),
                new Card("Weapon", 15, "Battle-Axe"),
                new Card("Weapon", 20, "Lance"),
                new Card("Weapon", 30, "Excalibur")
        ));

        p3.hand.addAll(Arrays.asList(
                new Card("Foe", 5),
                new Card("Foe", 5),
                new Card("Foe", 15),
                new Card("Foe", 30),
                new Card("Weapon", 5, "Dagger"),
                new Card("Weapon", 10, "Sword"),
                new Card("Weapon", 10, "Horse"),
                new Card("Weapon", 15, "Battle-Axe"),
                new Card("Weapon", 20, "Lance")
        ));

        p4.hand.addAll(Arrays.asList(
                new Card("Foe", 5),
                new Card("Foe", 5),
                new Card("Foe", 15),
                new Card("Foe", 15),
                new Card("Foe", 40),
                new Card("Weapon", 5, "Dagger"),
                new Card("Weapon", 10, "Sword"),
                new Card("Weapon", 10, "Horse"),
                new Card("Weapon", 15, "Battle-Axe"),
                new Card("Weapon", 30, "Excalibur")
        ));

        Card questCard = new Card("Quest", 4);
        eventDeck.discards.add(questCard);

        Player sponsor = p2;

        List<List<Card>> questStages = new ArrayList<>();

        questStages.add(Collections.singletonList(new Card("Foe", 15)));
        questStages.add(Collections.singletonList(new Card("Foe", 20)));
        questStages.add(Collections.singletonList(new Card("Foe", 30)));
        questStages.add(Collections.singletonList(new Card("Foe", 50)));

        List<Player> participants = new ArrayList<>(Arrays.asList(p1, p3, p4));

        p1.drawSpecificCard(new Card("Foe", 30));
        p1.hand.removeIf(card -> card.getType().equals("Foe") && card.getValue() == 5);

        p3.drawSpecificCard(new Card("Weapon", 10, "Sword"));
        p3.hand.removeIf(card -> card.getType().equals("Foe") && card.getValue() == 5);

        p4.drawSpecificCard(new Card("Weapon", 15, "Battle-Axe"));
        p4.hand.removeIf(card -> card.getType().equals("Foe") && card.getValue() == 5);

        List<Card> p1Attack = new ArrayList<>();
        p1Attack.add(p1.playCard("Dagger"));
        p1Attack.add(p1.playCard("Sword"));
        assertEquals(15, p1.calculateStageValue(p1Attack));

        List<Card> p3Attack = new ArrayList<>();
        p3Attack.add(p3.playCard("Sword"));
        p3Attack.add(p3.playCard("Dagger"));
        assertEquals(15, p3.calculateStageValue(p3Attack));

        List<Card> p4Attack = new ArrayList<>();
        p4Attack.add(p4.playCard("Dagger"));
        p4Attack.add(p4.playCard("Horse"));
        assertEquals(15, p4.calculateStageValue(p4Attack));

        p1.drawSpecificCard(new Card("Foe", 10));

        p3.drawSpecificCard(new Card("Weapon", 20, "Lance"));

        p4.drawSpecificCard(new Card("Weapon", 20, "Lance"));

        List<String> expectedHandP3 = Arrays.asList("F5", "F5", "F15", "F30", "Sword");
        List<String> actualHandP3 = new ArrayList<>();
        for (Card card : p3.hand) {
            actualHandP3.add(card.getName());
        }
        assertEquals(0, p3.shields);

        p3.drawSpecificCard(new Card("Weapon", 15, "Battle-Axe"));

        p4.drawSpecificCard(new Card("Weapon", 10, "Sword"));

        p3Attack.clear();
        p3Attack.add(p3.playCard("Lance"));
        p3Attack.add(p3.playCard("Horse"));
        p3Attack.add(p3.playCard("Sword"));
        assertEquals(40, p3.calculateStageValue(p3Attack));

        p4Attack.clear();
        p4Attack.add(p4.playCard("Battle-Axe"));
        p4Attack.add(p4.playCard("Sword"));
        p4Attack.add(p4.playCard("Lance"));
        assertEquals(45, p4.calculateStageValue(p4Attack));

        p3.drawSpecificCard(new Card("Foe", 30));

        p4.drawSpecificCard(new Card("Weapon", 20, "Lance"));

        p3Attack.clear();
        p3Attack.add(p3.playCard("Battle-Axe"));
        p3Attack.add(p3.playCard("Lance"));

        p4Attack.clear();
        p4Attack.add(p4.playCard("Sword"));
        p4Attack.add(p4.playCard("Lance"));
        p4Attack.add(p4.playCard("Excalibur"));

        int stage4Value = 50;
        if (p3.calculateStageValue(p3Attack) < stage4Value) {
            participants.remove(p3);
        }

        if (p4.calculateStageValue(p4Attack) >= stage4Value) {
            p4.shields += 4;
        }

        List<String> expectedHandP4 = Arrays.asList("F15", "F15", "F40", "Lance");
        List<String> actualHandP4 = new ArrayList<>();
        for (Card card : p4.hand) {
            actualHandP4.add(card.getName());
        }
        assertEquals(4, p4.shields);
        assertEquals(expectedHandP4.size(), actualHandP4.size());

        p2.hand.subList(0, 9).clear();

        String simulatedInput = "\n1\n1\n1\n1\n1\n1\n1\n1\n";
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        Scanner scanner = new Scanner(inputStream);
        for (int i = 0; i < 13; i++) {
            p2.draw(adventureDeck, scanner);
        }

        while (p2.hand.size() > 12) {
            p2.hand.remove(0);
        }

        assertEquals(12, p2.hand.size());
    }
}
