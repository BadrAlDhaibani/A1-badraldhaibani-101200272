package org.example;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args){
        Game currentGame = new Game();
        List<Player> winners = new ArrayList<>();
        boolean winnerFlag = false;
        while (!winnerFlag){
            currentGame.round();
            winners = currentGame.checkForWinners();
            if (!winners.isEmpty()) {
                winnerFlag = true;
            }
        }
        System.out.println("Winner(s):");
        for (Player winner : winners) {
            winner.printWinner();
        }
        System.out.println("Game Over.");
    }
}