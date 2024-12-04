package org.example;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:8081")
public class MainController {
    @PostMapping("/input")
    public String input(@RequestBody String userInput) {
        return "Received: "+userInput;
    }
}
