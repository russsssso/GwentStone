package game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import fileio.GameInput;
import fileio.Input;
import fileio.StartGameInput;
import game.cards.Card;
import game.cards.Hero;
import game.cards.Minion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public final class Game {
    private int noGames;
    private int playerOneWins;
    private int playerTwoWins;
    private Board board;
    private ArrayList<GameInput> games;

    private ArrayList<Player> players;

    private Integer currentPlayer;

    private Integer roundNr;

    private Boolean turnComplete;

    private Boolean gameWon;

    public Game(final Input inputData) {
        this.board = new Board();
        this.games = inputData.getGames();
        this.players = new ArrayList<>();
        this.players.add(new Player(inputData.getPlayerOneDecks()));
        this.players.add(new Player(inputData.getPlayerTwoDecks()));
        this.noGames = 0;
        this.playerOneWins = 0;
        this.playerTwoWins = 0;
    }

    public ArrayList<ArrayList<Card>> getBoard() {
        return board.getBoard();
    }

    public ArrayList<GameInput> getGames() {
        return games;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Switches the player turn
     */
    public void switchPlayer() {
        if (this.currentPlayer == 0) {
            this.currentPlayer = 1;
        } else if (this.currentPlayer == 1) {
            this.currentPlayer = 0;
        }
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public int getNoGames() {
        return noGames;
    }

    public int getPlayerOneWins() {
        return playerOneWins;
    }

    public int getPlayerTwoWins() {
        return playerTwoWins;
    }

    /**
     * Method that returns the current player's BACK row;
     * */
    public ArrayList<Card> getPlayerBackRow() {
        if (currentPlayer == 0) {
            return board.getBoard().get(3);
        }
        return board.getBoard().get(0);
    }

    /**
     * Method that returns the current player's BACK row's index;
     * */
    public int getPlayerBackRowNr() {
        if (currentPlayer == 0) {
            return 3;
        }
        return 0;
    }

    /**
     * Method that returns the current player's FRONT row;
     * */
    public ArrayList<Card> getPlayerFrontRow() {
        if (currentPlayer == 0) {
            return board.getBoard().get(2);
        }
        return board.getBoard().get(1);
    }

    /**
     * Method that returns the current player's FRONT row's index;
     * */
    public int getPlayerFrontRowNr() {
        if (currentPlayer == 0) {
            return 2;
        }
        return 1;
    }

    /**
     * Method that returns the current player's OPPONENT BACK row;
     * */
    public ArrayList<Card> getOpponentBackRow() {
        if (currentPlayer == 0) {
            return board.getBoard().get(0);
        }
        return board.getBoard().get(3);
    }

    /**
     * Method that returns the opponent's BACK row's index;
     * */
    public int getOpponentBackRowNr() {
        if (currentPlayer == 0) {
            return 0;
        }
        return 3;
    }

    /**
     * Method that returns the current player's OPPONENT FRONT row;
     * */
    public ArrayList<Card> getOpponentFrontRow() {
        if (currentPlayer == 0) {
            return board.getBoard().get(1);
        }
        return board.getBoard().get(2);
    }

    /**
     * Method that returns the opponent's FRONT row's index;
     * */
    public int getOpponentFrontRowNr() {
        if (currentPlayer == 0) {
            return 1;
        }
        return 2;
    }

    public ArrayList<Card> getRow(final int index) {
        return getBoard().get(index);
    }

    /**
     * Begin new turn
     */
    public void beginTurn() {
        this.turnComplete = false;
    }

    /**
     * End current turn
     */
    public void endTurn() {
        this.turnComplete = true;
    }

    /** Function used to get all frozne cards on board
     *   in an ArrayList of Cards */
    public ArrayList<Card> getFrozenCards() {
        ArrayList<Card> frozen = new ArrayList<>();
        for (int i = 0; i < board.getNrRows(); i++) {
            for (int j = 0; j < board.getNrCol(); j++) {
                if (getCardAtPosition(i, j) != null) {
                    Minion minion = (Minion) getCardAtPosition(i, j);

                    if (minion.getFrozen()) {
                        frozen.add(minion);
                    }
                }
            }
        }
        return frozen;
    }

    /**
     * checks is current player's opponent has a tank minion in the front row
     */
    public Boolean opponentHasTank() {
        for (Card card : getOpponentFrontRow()) {
            if (Utils.getInstance().getTanks().contains(card.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the requested card
     * @param rowNr the x position of the card
     * @param colNr the y position of the card
     */
    public Card getCardAtPosition(final int rowNr, final int colNr) {
        if (board.getBoard().isEmpty()) {
            return null;
        }
        if (board.getBoard().size() <= rowNr) {
            return null;
        }
        if (board.getBoard().get(rowNr).size() <= colNr) {
            return null;
        }
        return board.getBoard().get(rowNr).get(colNr);
    }

    public Hero getOpponentHero() {
        if (currentPlayer == 0) {
            return (Hero) this.players.get(1).getHeroCard();
        }
        return (Hero) this.players.get(0).getHeroCard();
    }
    public Hero getPlayerHero() {
        if (currentPlayer == 0) {
            return (Hero) this.players.get(0).getHeroCard();
        }
        return (Hero) this.players.get(1).getHeroCard();
    }

    public void won(final ArrayNode output, ObjectMapper objectMapper){
        this.gameWon = true;

        ObjectNode won = objectMapper.createObjectNode();
        switch (currentPlayer) {
            case 0:
                won.put("gameEnded", "Player one killed the enemy hero.");
                this.playerOneWins++;
                break;
            default:
                won.put("gameEnded", "Player two killed the enemy hero.");
                this.playerTwoWins++;
                break;
        }
        output.add(won);
    }



    /**
     * Method where most of the game logic is implemented: it does the initial setup and
     * executes the command for the games
     * @param output the ArrayNode where the game output is written
     * @param objectMapper main objMapper
     */
    public void run(final ArrayNode output, final ObjectMapper objectMapper) {
        for (GameInput game : games) {
            this.noGames++;
            StartGameInput gameStart = game.getStartGame();
            players.get(0).setGameDeck(gameStart.getPlayerOneDeckIdx());
            players.get(1).setGameDeck(gameStart.getPlayerTwoDeckIdx());
            this.roundNr = 0;
            this.gameWon = false;
            int seed = gameStart.getShuffleSeed();

            Collections.shuffle(players.get(0).getGameDeck(), new Random(seed));
            Collections.shuffle(players.get(1).getGameDeck(), new Random(seed));

            players.get(0).setHeroCard(gameStart.getPlayerOneHero());
            players.get(1).setHeroCard(gameStart.getPlayerTwoHero());

            this.currentPlayer = gameStart.getStartingPlayer() - 1;
            players.get(0).setMana(0);
            players.get(1).setMana(0);

            ArrayList<Action> actions = new ArrayList<>();

            for (ActionsInput action : game.getActions()) {
                actions.add(new Action(action));
            }

            while (!actions.isEmpty()) {
                roundNr += 1;
                players.get(0).drawCard();
                players.get(0).incMana(roundNr);
                players.get(1).drawCard();
                players.get(1).incMana(roundNr);


                doTurn(output, objectMapper, actions);

                doTurn(output, objectMapper, actions);

            }
        }
    }

    private void doTurn(final ArrayNode output,
                        final ObjectMapper objectMapper, final ArrayList<Action> actions) {
        this.beginTurn();
        while (!turnComplete && !actions.isEmpty() ) {
            Action currentAction = actions.get(0);
            currentAction.execute(output, this, objectMapper);
            actions.remove(0);
        }

        for (Card card : getPlayerBackRow()) {
            ((Minion) card).setFrozen(false);
            ((Minion) card).setUsed(false);
        }
        for (Card card : getPlayerFrontRow()) {
            ((Minion) card).setFrozen(false);
            ((Minion) card).setUsed(false);
        }
        switchPlayer();
    }
}
