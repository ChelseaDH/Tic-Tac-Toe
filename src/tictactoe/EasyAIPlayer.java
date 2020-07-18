package tictactoe;

public class EasyAIPlayer extends AIPlayer {

    public EasyAIPlayer(char symbol) {
        super(symbol, Difficulty.EASY);
    }

    @Override
    protected void makeAIMove(Board board) {
        makeRandomMove(board);
    }
}
