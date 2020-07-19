package tictactoe;

import java.util.Random;

public abstract class AIPlayer extends Player {
    protected Difficulty difficulty;

    AIPlayer(char symbol, Difficulty difficulty) {
        super(symbol);
        this.difficulty = difficulty;
    }

    @Override
    public void makeMove(Board board) {
        // Print out move making
        System.out.printf("Making move level \"%s\"\n", this.difficulty.name().toLowerCase());

        this.makeAIMove(board);
    }

    protected abstract void makeAIMove(Board board);

    protected void makeRandomMove(Board board) {
        Random random = new Random(System.currentTimeMillis());
        Point p = new Point();
        do {
            p.x = random.nextInt(board.boardSize);
            p.y = random.nextInt(board.boardSize);
        } while (board.isOccupied(p));

        // Set the cell
        board.setCell(p, this.symbol);
    }
}
