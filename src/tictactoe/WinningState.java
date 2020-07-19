package tictactoe;

public class WinningState {
    Point p;
    char symbol;

    WinningState(Point p, char symbol) {
        this.p = p;
        this.symbol = symbol;
    }
}
