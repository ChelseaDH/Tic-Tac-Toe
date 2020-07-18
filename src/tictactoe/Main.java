package tictactoe;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        // Create the initial board and players
        Board board = new Board();

        // Take in user commands until a valid input is provided
        String[] commands = null;
        boolean valid = false;

        while (!valid) {
            System.out.print("Input command: ");
            try {
                commands = board.parseCommand(s.nextLine());
                valid = true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        // Create players
        Player player1 = Player.NewForType(commands[1], 'X', s);
        Player player2 = Player.NewForType(commands[2], 'O', s);
        Player currentPlayer = player1;

        // Print initial board
        board.printBoard();

        // Play the game
        do {
            currentPlayer.makeMove(board);

            // Print the updated board
            board.printBoard();

            // Check for winners or draw
            board.check();
            // Print out winner if applicable
            if (board.state == State.WINNER_FOUND) {
                System.out.println(currentPlayer.symbol + " wins");
            }

            // Change player
            currentPlayer = currentPlayer == player1 ? player2 : player1;

        } while (board.state == State.IN_PROGRESS);
    }
}


