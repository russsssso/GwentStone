package game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.ActionsInput;
import fileio.GameInput;
import fileio.Input;
import fileio.StartGameInput;

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
     * @param player The player that just finished the turn
     */
    public void switchPlayer(int player) {
        if (player == 0) {
            player = 1;
        } else if (player == 1) {
            player = 0;
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
                Boolean turnComplete = false;
                players.get(0).drawCard();
                players.get(0).incMana();
                players.get(1).drawCard();
                players.get(1).incMana();

                while (!turnComplete && !actions.isEmpty()) {
                    Action currentAction = actions.get(0);
                    currentAction.execute(output, this, objectMapper, turnComplete);
                    actions.remove(0);
                }

                turnComplete = false;
                switchPlayer(currentPlayer);
                while (!turnComplete && !actions.isEmpty()) {
                    Action currentAction = actions.get(0);
                    currentAction.execute(output, this, objectMapper, turnComplete);
                    actions.remove(0);
                }
                switchPlayer(currentPlayer);
            }
        }
    }
}
