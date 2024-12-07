package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
