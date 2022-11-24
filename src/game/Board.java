package game;

import game.cards.Card;

import java.util.ArrayList;

public final class Board {
    private final int row = 4;
    private final int col = 5;
    private ArrayList<ArrayList<Card>> board;

    public Board() {
        board = new ArrayList<ArrayList<Card>>(row);
        for (int i = 0; i < row; i++) {
            board.add(new ArrayList<Card>(col));
        }

    }

    public ArrayList<ArrayList<Card>> getBoard() {
        return board;
    }

    public int getNrRows() {
        return row;
    }

    public int getNrCol() {
        return row;
    }
}
