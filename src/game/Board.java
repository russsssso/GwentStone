package game;

import game.cards.Card;

import game.cards.Minion;

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

    /** Function used to get all frozne cards on board
    *   in an ArrayList of Cards */
    public ArrayList<Card> getFrozenCards() {
        ArrayList<Card> frozen = new ArrayList<>();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (getCardAtPosition(row, col) != null) {
                    Minion minion = (Minion) getCardAtPosition(row, col);

                    if (minion.getFrozen()) {
                        frozen.add(minion);
                    }
                }
            }
        }
        return frozen;
    }

    /**
     * Returns the requested card
     * @param rowNr the x position of the card
     * @param colNr the y position of the card
     */
    public Card getCardAtPosition(final int rowNr, final int colNr) {
        if (board.isEmpty()) {
            System.out.println("Board Empty");
            return null;
        }
        if (board.size() <= rowNr) {
            System.out.println("Row Empty");
            return null;
        }
        if (board.get(rowNr).size() <= colNr) {
            System.out.println("No card here!: " + rowNr + " " + colNr);
            return null;
        }
        return board.get(rowNr).get(colNr);
    }
}
