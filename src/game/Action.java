package game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import fileio.Coordinates;
import game.cards.Card;

import java.util.ArrayList;

public final class Action {
    private String command;
    private int handIdx;
    private Coordinates cardAttacker;
    private Coordinates cardAttacked;
    private int affectedRow;
    private int playerIdx;
    private int x;
    private int y;

    public Action(final ActionsInput action) {
        this.command = action.getCommand();


        switch (this.command) {
            case "getPlayerDeck":
            case "getPlayerHero":
            case "getCardsInHand":
            case "getPlayerMana":
            case "getEnviormentCardsInHand":
                this.playerIdx = action.getPlayerIdx() - 1;
                break;
            case "getCardAtPosition":
                this.x = action.getX();
                this.y = action.getY();
                break;
            default:
                break;
        }
    }

    /**
     * Method that identifies the command and aexcutes it
     *
     * @param output ArrayNode where the output is written
     * @param game  current game
     * @param objectMapper objectMapper used in current game
     * @param complete used for end turn command
     */
    public void execute(final ArrayNode output, final Game game, final ObjectMapper objectMapper, final Boolean complete) {

        switch (this.command) {
            case "getPlayerDeck" -> {
                ObjectNode playerDeck = objectMapper.createObjectNode();
                playerDeck.put("command", this.command);
                playerDeck.put("playerIdx", this.playerIdx + 1);
                playerDeck.putPOJO("output", game.getPlayers().get(this.playerIdx).getGameDeck());
                output.add(playerDeck);
            }
            case "getPlayerHero" -> {
                ObjectNode playerHero = objectMapper.createObjectNode();
                playerHero.put("command", this.command);
                playerHero.put("playerIdx", this.playerIdx + 1);
                playerHero.putPOJO("output", game.getPlayers().get(this.playerIdx).getHeroCard());
                output.add(playerHero);
            }
            case "getPlayerTurn" -> {
                ObjectNode turn = objectMapper.createObjectNode();
                turn.put("command", this.command);
                turn.put("output", game.getCurrentPlayer() + 1);
                output.add(turn);
            }
            case "getCardsInHand" -> {
                ObjectNode cardsInHand = objectMapper.createObjectNode();
                cardsInHand.put("command", this.command);
                cardsInHand.put("playerIdx", this.playerIdx + 1);
                cardsInHand.putPOJO("output", game.getPlayers().get(this.playerIdx).getGameHand());
                output.add(cardsInHand);
            }
            case "getCardsOnTable" -> {
                ObjectNode cardsOnTable = objectMapper.createObjectNode();
                cardsOnTable.put("command", this.command);
                cardsOnTable.putPOJO("output", game.getGameBoard().getBoard());
                output.add(cardsOnTable);
            }
            case "getCardAtPosition" -> {
                ObjectNode cardAtPosition = objectMapper.createObjectNode();
                cardAtPosition.put("command", this.command);
                cardAtPosition.putPOJO("output", game.getGameBoard().getCardAtPosition(x, y));
                output.add(cardAtPosition);
            }
            case "getPlayerMana" -> {
                ObjectNode playerMana = objectMapper.createObjectNode();
                playerMana.put("command", this.command);
                playerMana.put("output", game.getPlayers().get(this.playerIdx).getMana());
                output.add(playerMana);
            }
            case "getEnviormentCardsInHand" -> {
                ObjectNode playerEnviorments = objectMapper.createObjectNode();
                ArrayList<Card> enviorments = new ArrayList<>();
                Utils utils = Utils.getInstance();
                for (Card card : game.getPlayers().get(this.playerIdx).getGameHand()) {
                    if (utils.getEnviorment().contains(card.getName())) {
                        enviorments.add(card);
                    }
                }
                playerEnviorments.put("command", this.command);
                playerEnviorments.put("playerIdx", this.playerIdx + 1);
                playerEnviorments.putPOJO("output", enviorments);
                output.add(playerEnviorments);
            }
            case "getFrozenCardsOnTable" -> {
                ObjectNode frozen = objectMapper.createObjectNode();
                frozen.put("command", this.command);
                frozen.putPOJO("output", game.getGameBoard().getFrozenCards());
                output.add(frozen);
            }
            case "getTotalGamesPlayed" -> {
                ObjectNode games = objectMapper.createObjectNode();
                games.put("command", this.command);
                games.put("output", game.getNoGames());
                output.add(games);
            }
            case "getPlayerOneWins" -> {
                ObjectNode playerOneWins = objectMapper.createObjectNode();
                playerOneWins.put("command", this.command);
                playerOneWins.put("output", game.getPlayerOneWins());
                output.add(playerOneWins);
            }
            case "getPlayerTwoWins" -> {
                ObjectNode playerTwoWins = objectMapper.createObjectNode();
                playerTwoWins.put("command", this.command);
                playerTwoWins.put("output", game.getPlayerTwoWins());
                output.add(playerTwoWins);
            }
            default -> {
            }
        }
    }
}
