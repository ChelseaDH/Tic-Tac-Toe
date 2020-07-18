package tictactoe;

import java.util.Scanner;

public abstract class Player {
    char symbol;

    Player(char symbol) {
        this.symbol = symbol;
    }

    public void makeMove(Board board) {}

    public static Player NewForType(String type, char symbol, Scanner s) {
        Player player;

        switch (type) {
            case "user":
                player = new HumanPlayer(symbol, s);
                break;
            case "easy":
                player = new AIPlayer(symbol, Difficulty.EASY);
                break;
            case "medium":
                player = new AIPlayer(symbol, Difficulty.MEDIUM);
                break;
            case "hard":
                player = new AIPlayer(symbol, Difficulty.HARD);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        return player;
    }
}
