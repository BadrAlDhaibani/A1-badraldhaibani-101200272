package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:8081")
public class MainController {
    private Game game = new Game();
    @PostMapping("/input")
    public String input(@RequestBody String userInput) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(userInput);
            String inputText = jsonNode.get("userInput").asText();
            return game.handleInput(inputText);
        } catch (Exception e) {
            return "Error processing input." + e;
        }
    }
    @PostMapping("/rig")
    public String rigGame(@RequestBody Map<String, Object> rigData) {
        try {
            game = new Game();
            List<List<Map<String, Object>>> playerHands = (List<List<Map<String, Object>>>) rigData.get("playerHands");
            for (int i = 0; i < playerHands.size(); i++) {
                List<Map<String, Object>> cards = playerHands.get(i);
                List<Card> hand = new ArrayList<>();
                for (Map<String, Object> cardData : cards) {
                    String type = (String) cardData.get("type");
                    int value = (int) cardData.get("value");
                    String label = (String) cardData.getOrDefault("label", null);
                    if (label != null) {
                        hand.add(new Card(type, value, label));
                    } else {
                        hand.add(new Card(type, value));
                    }
                }
                game.players.get(i).hand = hand;
            }
            return "Game rigged successfully.";
        } catch (Exception e) {
            return "Error rigging game: " + e.getMessage();
        }
    }
    @PostMapping("/rig-event")
    public String rigEventCard(@RequestBody Map<String, Object> cardData) {
        try {
            String type = (String) cardData.get("type");
            int value = (int) cardData.get("value");
            String label = (String) cardData.getOrDefault("label", null);

            Card card;
            if (label != null) {
                card = new Card(type, value, label);
            } else {
                card = new Card(type, value);
            }

            game.rigEventCard(card);
            return "Event card rigged successfully.";
        } catch (Exception e) {
            return "Error rigging event card: " + e.getMessage();
        }
    }

}
