package tictactoe;

import java.util.List;

public class MediumAIPlayer extends AIPlayer{

    public MediumAIPlayer(char symbol) {
        super(symbol, Difficulty.MEDIUM);
    }

    @Override
    protected void makeAIMove(Board board) {
        // Grab the list of current winning states on the board
        List<WinningState> winningStates = board.cellsToWinNextTurn();

        // If there are no winning states - make a random move
        if (winningStates.size() == 0) {
            makeRandomMove(board);
        } else {
            // Check if the player can win - make that move
            for (WinningState winningState : winningStates) {
                if (winningState.symbol == this.symbol) {
                    board.setCell(winningState.p, this.symbol);
                    return;
                }
            }

            // If not, block the first win of the possible wins
            board.setCell(winningStates.get(0).p, this.symbol);
        }
    }
}
