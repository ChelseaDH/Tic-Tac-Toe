package tictactoe;

import java.util.ArrayList;
import java.util.List;

public class Board {
    int boardSize = 3;
    char[][] cells = new char[boardSize][boardSize];
    int noXs, noOs;
    State state;

    public Board() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.cells[i][j] = ' ';
            }
        }
        this.state = State.IN_PROGRESS;
        this.noXs = 0;
        this.noOs = 0;
    }

    public String[] parseCommand(String s) {
        String[] commands = s.split("\\s");

        if (commands[0].equals("exit")) {
            System.exit(0);
        }

        if (commands.length == 3 && commands[0].equals("start")
                && (commands[1].equals("user") || commands[1].equals("easy") || commands[1].equals("medium"))
                && (commands[2].equals("user") || commands[2].equals("easy") || commands[2].equals("medium"))) {
            return commands;
        } else {
            throw new IllegalArgumentException("Bad parameters!");
        }
    }

    public void printBoard() {
        System.out.println("---------");
        System.out.printf("| %c %c %c | \n", this.cells[0][0], this.cells[0][1], this.cells[0][2]);
        System.out.printf("| %c %c %c | \n", this.cells[1][0], this.cells[1][1], this.cells[1][2]);
        System.out.printf("| %c %c %c | \n", this.cells[2][0], this.cells[2][1], this.cells[2][2]);
        System.out.println("---------");
    }

    // Adds a new cell into the board
    public void addCell(int coord1, int coord2, char symbol) {
        int index1 = 0, index2 = 0;

        // Check that coordinates are in range
        if (coord1 < 1 || coord1 > 3 || coord2 < 1 || coord2 > 3) {
            throw new IllegalArgumentException("Coordinates should be from 1 to 3!");
        }

        // Change coordinates into array indices
        switch (coord1) {
            case 2:
                index2 = 1;
                break;
            case 3:
                index2 = 2;
                break;
        }

        switch (coord2) {
            case 1:
                index1 = 2;
                break;
            case 2:
                index1 = 1;
                break;
        }

        // Check if the board is already occupied at those indices
        if (this.isOccupied(index1, index2)) {
            throw new IllegalArgumentException("This cell is occupied! Choose another one!");
        }

        // Set the cell
        setCell(index1, index2, symbol);
    }

    void setCell(int index1, int index2, char symbol) {
        this.cells[index1][index2] = symbol;

        // Add to the player count
        switch (symbol) {
            case 'X':
                this.noXs++;
                break;
            case 'O':
                this.noOs++;
                break;
        }
    }

    // Removes the symbol from a given cell
    private void removeCell(int index1, int index2) {
        // Remove player count
        switch (this.cells[index1][index2]) {
            case 'X':
                this.noXs--;
                break;
            case 'O':
                this.noOs--;
                break;
        }

        // Set the cell to have no symbol
        setCell(index1, index2, ' ');
    }

    // Checks if a cell in the board is already occupied
    boolean isOccupied(int x, int y) {
        return this.cells[x][y] == 'X' || this.cells[x][y] == 'O';
    }

    // Check for wins in rows, columns, and diagonals
    public void check() {
        // Check for wins in rows and columns
        for (int i = 0; i < 3; i++) {
            if ((this.cells[0][i] == this.cells[1][i] && this.cells[0][i] == this.cells[2][i] && this.cells[0][i] != ' ')
                    || (this.cells[i][0] == this.cells[i][1] && this.cells[i][0] == this.cells[i][2] && this.cells[i][0] != ' ')) {
                this.state = State.WINNER_FOUND;
                break;
            }
        }

        // Check for wins on the diagonals
        if ((this.cells[0][0] == this.cells[1][1] && this.cells[0][0] == this.cells[2][2] && this.cells[1][1] != ' ')
                || (this.cells[2][0] == this.cells[1][1] && this.cells[2][0] == this.cells[0][2] && this.cells[1][1] != ' ')) {
            this.state = State.WINNER_FOUND;
        }

        // Checks for a draw
        if (this.state != State.WINNER_FOUND && noXs + noOs == 9) {
            this.state = State.DRAW;
            System.out.println("Draw");
        }
    }

    // Returns the cell coordinates that will cause a win in the next turn
    // Or null if win not possible
    public List<WinningState> cellsToWinNextTurn() {
        List<WinningState> winners = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            // Check for possible wins in rows
            if (this.cells[i][0] == this.cells[i][1] && !isOccupied(i, 2)) {winners.add(new WinningState(this.cells[i][0], i, 2));}
            if (this.cells[i][0] == this.cells[i][2] && !isOccupied(i, 1)) {winners.add(new WinningState(this.cells[i][0], i, 1));}
            if (this.cells[i][1] == this.cells[i][2] && !isOccupied(i, 0)) {winners.add(new WinningState(this.cells[i][1], i, 0));}

            // Check for possible wins in columns
            if (this.cells[0][i] == this.cells[1][i] && !isOccupied(2, i)) {winners.add(new WinningState(this.cells[0][i], 2, i));}
            if (this.cells[0][i] == this.cells[2][i] && !isOccupied(1, i)) {winners.add(new WinningState(this.cells[0][i], 1, i));}
            if (this.cells[1][i] == this.cells[2][i] && !isOccupied(0, i)) {winners.add(new WinningState(this.cells[1][i], 0, i));}
        }

        // Check for possible wins in main diagonal
        if (this.cells[0][0] == this.cells[1][1] && !isOccupied(2, 2)) {winners.add(new WinningState(this.cells[0][0], 2, 2));}
        if (this.cells[0][0] == this.cells[2][2] && !isOccupied(1, 1)) {winners.add(new WinningState(this.cells[0][0], 1, 1));}
        if (this.cells[1][1] == this.cells[2][2] && !isOccupied(0, 0)) {winners.add(new WinningState(this.cells[1][1], 0, 0));}

        // Check for possible wins in side diagonal
        if (this.cells[0][2] == this.cells[1][2] && !isOccupied(2, 0)) {winners.add(new WinningState(this.cells[0][2], 2, 0));}
        if (this.cells[0][2] == this.cells[2][0] && !isOccupied(1, 2)) {winners.add(new WinningState(this.cells[0][2], 1, 2));}
        if (this.cells[1][2] == this.cells[2][0] && !isOccupied(0, 2)) {winners.add(new WinningState(this.cells[1][2], 0, 2));}

        return winners;
    }
}
