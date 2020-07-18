package tictactoe;

import java.sql.Time;
import java.util.*;

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


enum State {
    IN_PROGRESS,
    DRAW,
    WINNER_FOUND
}

enum Difficulty {
    EASY,
    MEDIUM,
    HARD
}

abstract class Player {
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

class AIPlayer extends Player {
    Difficulty difficulty;

    AIPlayer(char symbol, Difficulty difficulty) {
        super(symbol);
        this.difficulty = difficulty;
    }

    @Override
    public void makeMove(Board board) {
        // Print out move making
        System.out.printf("Making move level \"%s\"\n", this.difficulty.name().toLowerCase());

        switch (this.difficulty) {
            case EASY:
                makeRandomMove(board);
                break;

            case MEDIUM:
                // Grab the list of current winning states on the board
                List<WinningState> winningStates = board.cellsToWinNextTurn();

                // If there are no winning states - make a random move
                if (winningStates.size() == 0) {
                    makeRandomMove(board);
                } else {
                    // Check if the player can win - make that move
                    for (WinningState winningState : winningStates) {
                        if (winningState.symbol == this.symbol) {
                            board.setCell(winningState.index1, winningState.index2, this.symbol);
                            return;
                        }
                    }

                    // If not, block the first win of the possible wins
                    board.setCell(winningStates.get(0).index1, winningStates.get(0).index2, this.symbol);
                    break;
                }
        }
    }

    private void makeRandomMove(Board board) {
        Random random = new Random(System.currentTimeMillis());
        int index1, index2;
        do {
            index1 = random.nextInt(board.boardSize);
            index2 = random.nextInt(board.boardSize);
        } while (board.isOccupied(index1, index2));

        // Set the cell
        board.setCell(index1, index2, this.symbol);
    }
}

class HumanPlayer extends Player {
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

class Board {
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

class WinningState {
    char symbol;
    int index1;
    int index2;

    WinningState(char symbol, int index1, int index2) {
        this.symbol = symbol;
        this.index1 = index1;
        this.index2 = index2;
    }
}