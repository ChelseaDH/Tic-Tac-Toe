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

        // Start command must be followed by 2 further commands denoting the player type
        // If any part of the input is incorrect - throw IllegalArgumentException
        if (commands.length == 3 && commands[0].equals("start")) {
            for (int i = 1; i < commands.length; i++) {
                if (!commands[i].equals("user") && !isValidDifficulty(commands[i])) {
                    throw new IllegalArgumentException("Bad parameters!");
                }
            }
            return commands;
        } else {
            throw new IllegalArgumentException("Bad parameters!");
        }
    }

    // Checks if a given input matches a Difficulty option
    private boolean isValidDifficulty(String input) {
        for (Difficulty d: Difficulty.values()) {
            if (input.equals(d.name().toLowerCase())) {
                return true;
            }
        }
        return false;
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
        Point p = new Point();

        // Check that coordinates are in range
        if (coord1 < 1 || coord1 > 3 || coord2 < 1 || coord2 > 3) {
            throw new IllegalArgumentException("Coordinates should be from 1 to 3!");
        }

        // Change coordinates into array indices
        switch (coord1) {
            case 2:
                p.y = 1;
                break;
            case 3:
                p.y = 2;
                break;
        }

        switch (coord2) {
            case 1:
                p.x = 2;
                break;
            case 2:
                p.x = 1;
                break;
        }

        // Check if the board is already occupied at those indices
        if (this.isOccupied(p)) {
            throw new IllegalArgumentException("This cell is occupied! Choose another one!");
        }

        // Set the cell
        setCell(p, symbol);
    }

    void setCell(Point p, char symbol) {
        this.cells[p.x][p.y] = symbol;

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

    // Checks if a cell in the board is already occupied
    boolean isOccupied(Point p) {
        return this.cells[p.x][p.y] != ' ';
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
        Point p;

        for (int i = 0; i < 3; i++) {
            // Check for possible wins in rows
            if (this.cells[i][0] == this.cells[i][1] && !isOccupied(p = new Point(i, 2))) {winners.add(new WinningState(p, this.cells[i][0]));}
            if (this.cells[i][0] == this.cells[i][2] && !isOccupied(p = new Point(i, 1))) {winners.add(new WinningState(p, this.cells[i][0]));}
            if (this.cells[i][1] == this.cells[i][2] && !isOccupied(p = new Point(i, 0))) {winners.add(new WinningState(p, this.cells[i][1]));}

            // Check for possible wins in columns
            if (this.cells[0][i] == this.cells[1][i] && !isOccupied(p = new Point(2, i))) {winners.add(new WinningState(p, this.cells[0][i]));}
            if (this.cells[0][i] == this.cells[2][i] && !isOccupied(p = new Point(1, i))) {winners.add(new WinningState(p, this.cells[0][i]));}
            if (this.cells[1][i] == this.cells[2][i] && !isOccupied(p = new Point(0, i))) {winners.add(new WinningState(p, this.cells[1][i]));}
        }

        // Check for possible wins in main diagonal
        if (this.cells[0][0] == this.cells[1][1] && !isOccupied(p = new Point(2, 2))) {winners.add(new WinningState(p, this.cells[0][0]));}
        if (this.cells[0][0] == this.cells[2][2] && !isOccupied(p = new Point(1, 1))) {winners.add(new WinningState(p, this.cells[0][0]));}
        if (this.cells[1][1] == this.cells[2][2] && !isOccupied(p = new Point(0, 0))) {winners.add(new WinningState(p, this.cells[1][1]));}

        // Check for possible wins in side diagonal
        if (this.cells[0][2] == this.cells[1][2] && !isOccupied(p = new Point(2, 0))) {winners.add(new WinningState(p, this.cells[0][2]));}
        if (this.cells[0][2] == this.cells[2][0] && !isOccupied(p = new Point(1, 2))) {winners.add(new WinningState(p, this.cells[0][2]));}
        if (this.cells[1][2] == this.cells[2][0] && !isOccupied(p = new Point(0, 2))) {winners.add(new WinningState(p, this.cells[1][2]));}

        return winners;
    }

    public List<Point> getEmptyCells() {
        List<Point> empty = new ArrayList<>();

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                Point e = new Point(i, j);
                if (!isOccupied(e)) {
                    empty.add(e);
                }
            }
        }
        return empty;
    }
}
