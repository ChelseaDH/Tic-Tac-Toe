package tictactoe;

import java.util.Scanner;
import java.util.Vector;

public class HumanPlayer extends Player {
    private Scanner s;

    HumanPlayer(char symbol, Scanner s) {
        super(symbol);
        this.s = s;
    }

    private Vector<Integer> getCoordinates() {
        Vector<Integer> v = new Vector<>(2, 2);

        while (true) {
            // Get a coordinate string from the user
            System.out.print("Enter the coordinates:  ");
            String[] coordinates = s.nextLine().split("\\s");

            // Parse the coordinates into integers
            try {
                v.add(0, Integer.parseInt(coordinates[0]));
                v.add(1, Integer.parseInt(coordinates[1]));
                return v;
            } catch (NumberFormatException e) {
                System.out.println("You should enter numbers!");
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Well done moron!");
            }
        }
    }

    @Override
    public void makeMove(Board board) {
        Vector<Integer> coordinates = getCoordinates();

        // Try to add the cell into the board
        try {
            board.addCell(coordinates.get(0), coordinates.get(1), this.symbol);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
