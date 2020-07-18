package tictactoe;

public class WinningState {
    char symbol;
    int index1;
    int index2;

    WinningState(char symbol, int index1, int index2) {
        this.symbol = symbol;
        this.index1 = index1;
        this.index2 = index2;
    }
}
