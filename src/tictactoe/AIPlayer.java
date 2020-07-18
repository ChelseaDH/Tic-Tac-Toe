package tictactoe;

import java.util.List;
import java.util.Random;

public class AIPlayer extends Player {
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
