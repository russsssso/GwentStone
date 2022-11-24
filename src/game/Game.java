package game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.ActionsInput;
import fileio.GameInput;
import fileio.Input;
import fileio.StartGameInput;
import game.cards.Card;

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

    public Board getGameBoard() {
        return board;
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
     * Method that returns the current player's FRONT row;
     * */
    public ArrayList<Card> getPlayerFrontRow() {
        if (currentPlayer == 0) {
            return board.getBoard().get(2);
        }
        return board.getBoard().get(1);
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
     * Method that returns the current player's OPPONENT FRONT row;
     * */
    public ArrayList<Card> getOpponentFrontRow() {
        if (currentPlayer == 0) {
            return board.getBoard().get(1);
        }
        return board.getBoard().get(2);
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

                this.beginTurn();
                while (!turnComplete && !actions.isEmpty()) {
                    Action currentAction = actions.get(0);
                    currentAction.execute(output, this, objectMapper);
                    actions.remove(0);

                }

                switchPlayer();
                this.beginTurn();
                while (!turnComplete && !actions.isEmpty()) {
                    Action currentAction = actions.get(0);
                    currentAction.execute(output, this, objectMapper);
                    actions.remove(0);
                }
                switchPlayer();
            }
        }
    }
}
