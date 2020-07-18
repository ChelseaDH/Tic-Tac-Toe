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
                player = new EasyAIPlayer(symbol);
                break;
            case "medium":
                player = new MediumAIPlayer(symbol);
                break;
            case "hard":
                player = new HardAIPlayer(symbol);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        return player;
    }
}
