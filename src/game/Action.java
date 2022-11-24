package game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import fileio.Coordinates;
import game.cards.Card;
import game.cards.Environment;
import game.cards.Minion;

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
            case "endPlayerTurn":
            case "getPlayerDeck":
            case "getPlayerHero":
            case "getCardsInHand":
            case "getPlayerMana":
            case "getEnvironmentCardsInHand":
                this.playerIdx = action.getPlayerIdx() - 1;
                break;
            case "getCardAtPosition":
                this.x = action.getX();
                this.y = action.getY();
                break;
            case "placeCard":
                this.handIdx = action.getHandIdx();
            default:
                break;
        }
    }

    private void getPlayerDeck(final ArrayNode out, final Game game, final ObjectMapper mapper) {
        ObjectNode playerDeck = mapper.createObjectNode();
        playerDeck.put("command", this.command);
        playerDeck.put("playerIdx", this.playerIdx + 1);
        playerDeck.putPOJO("output", game.getPlayers().get(this.playerIdx).getGameDeck());
        out.add(playerDeck);
    }
    private void getPlayerHero(final ArrayNode out, final Game game, final ObjectMapper mapper) {
        ObjectNode playerHero = mapper.createObjectNode();
        playerHero.put("command", this.command);
        playerHero.put("playerIdx", this.playerIdx + 1);
        playerHero.putPOJO("output", game.getPlayers().get(this.playerIdx).getHeroCard());
        out.add(playerHero);
    }

    private void getPlayerTurn(final ArrayNode out, final Game game, final ObjectMapper mapper) {
        ObjectNode turn = mapper.createObjectNode();
        turn.put("command", this.command);
        turn.put("output", game.getCurrentPlayer() + 1);
        out.add(turn);
    }
    private void getCardsInHand(final ArrayNode out, final Game game, final ObjectMapper mapper) {
        ObjectNode cardsInHand = mapper.createObjectNode();
        cardsInHand.put("command", this.command);
        cardsInHand.put("playerIdx", this.playerIdx + 1);
        ArrayList<Card> hand = new ArrayList<Card>();
        for (Card card : game.getPlayers().get(this.playerIdx).getGameHand()) {
            if (Utils.getInstance().getMinions().contains(card.getName())) {
                hand.add(new Minion(card));
            } else {
                hand.add(new Environment(card));
            }
        }
        cardsInHand.putPOJO("output", hand);
        out.add(cardsInHand);
    }
    private void getCardsOnTable(final ArrayNode out, final Game game, final ObjectMapper mapper) {
        ObjectNode cardsOnTable = mapper.createObjectNode();
        ArrayList<ArrayList<Card>> table = new ArrayList<>();
        for (ArrayList<Card> rows : game.getGameBoard().getBoard()) {
            ArrayList<Card> row = new ArrayList<>();
            for (Card card : rows) {
                row.add(card);
            }
            table.add(row);
        }
        cardsOnTable.put("command", this.command);
        cardsOnTable.putPOJO("output", table);
        out.add(cardsOnTable);
    }

    private void getCardAtPos(final ArrayNode out, final Game game, final ObjectMapper mapper) {
        ObjectNode cardAtPosition = mapper.createObjectNode();
        cardAtPosition.put("command", this.command);
        cardAtPosition.putPOJO("output", game.getGameBoard().getCardAtPosition(x, y));
        out.add(cardAtPosition);
    }

    private void getPlayerMana(final ArrayNode out, final Game game, final ObjectMapper mapper) {
        ObjectNode playerMana = mapper.createObjectNode();
        playerMana.put("command", this.command);
        playerMana.put("playerIdx", this.playerIdx + 1);
        playerMana.put("output", game.getPlayers().get(this.playerIdx).getMana());
        out.add(playerMana);
    }
    private void getEnvironmentHand(final ArrayNode out, final Game game, final ObjectMapper mapper) {
        ObjectNode playerEnvironment = mapper.createObjectNode();
        ArrayList<Card> environments = new ArrayList<>();
        Utils utils = Utils.getInstance();
        for (Card card : game.getPlayers().get(this.playerIdx).getGameHand()) {
            if (utils.getEnvironment().contains(card.getName())) {
                environments.add(card);
            }
        }
        playerEnvironment.put("command", this.command);
        playerEnvironment.put("playerIdx", this.playerIdx + 1);
        playerEnvironment.putPOJO("output", environments);
        out.add(playerEnvironment);
    }
    private void getFrozenCards(final ArrayNode out, final Game game, final ObjectMapper mapper) {
        ObjectNode frozen = mapper.createObjectNode();
        frozen.put("command", this.command);
        frozen.putPOJO("output", game.getGameBoard().getFrozenCards());
        out.add(frozen);
    }
    private void getTotalGames(final ArrayNode out, final Game game, final ObjectMapper mapper) {
        ObjectNode games = mapper.createObjectNode();
        games.put("command", this.command);
        games.put("output", game.getNoGames());
        out.add(games);
    }
    private void getPlayerOneWins(final ArrayNode out, final Game game, final ObjectMapper mapper) {
        ObjectNode playerOneWins = mapper.createObjectNode();
        playerOneWins.put("command", this.command);
        playerOneWins.put("output", game.getPlayerOneWins());
        out.add(playerOneWins);
    }
    private void getPlayerTwoWins(final ArrayNode out, final Game game, final ObjectMapper mapper) {
        ObjectNode playerTwoWins = mapper.createObjectNode();
        playerTwoWins.put("command", this.command);
        playerTwoWins.put("output", game.getPlayerTwoWins());
        out.add(playerTwoWins);
    }
    private void placeCard(final ArrayNode out, final Game game, final ObjectMapper mapper) {
        Player currentPlayer = game.getPlayers().get(game.getCurrentPlayer());
        Card desiredCard = currentPlayer.getGameHand().get(this.handIdx);
        Utils utils = Utils.getInstance();
        if (utils.getEnvironment().contains(desiredCard.getName())) {
            ObjectNode environmentCard = mapper.createObjectNode();
            environmentCard.put("command", this.command);
            environmentCard.put("handIdx", this.handIdx);
            environmentCard.put("error", "Cannot place environment card on table.");
            out.add(environmentCard);
            return;
        }

        if (currentPlayer.getMana() < desiredCard.getMana()) {
            ObjectNode noMana = mapper.createObjectNode();
            noMana.put("command", this.command);
            noMana.put("handIdx", this.handIdx);
            noMana.put("error", "Not enough mana to place card on table.");
            out.add(noMana);
            return;
        }

        if (utils.getFrontRowCards().contains(desiredCard.getName())) {
            if (game.getPlayerFrontRow().size() == Utils.getInstance().getRowSize()) {
                ObjectNode rowFull = mapper.createObjectNode();
                rowFull.put("command", this.command);
                rowFull.put("handIdx", this.handIdx);
                rowFull.put("error", "Cannot place card on table since row is full.");
                out.add(rowFull);
            } else {
                currentPlayer.useMana(desiredCard.getMana());
                game.getPlayerFrontRow().add(new Minion(desiredCard));
                currentPlayer.getGameHand().remove(this.handIdx);
            }
        } else {
            if (game.getPlayerBackRow().size() == Utils.getInstance().getRowSize()) {
                ObjectNode rowFull = mapper.createObjectNode();
                rowFull.put("command", this.command);
                rowFull.put("handIdx", this.handIdx);
                rowFull.put("error", "Cannot place card on table since row is full.");
                out.add(rowFull);

            } else {
                currentPlayer.useMana(desiredCard.getMana());
                game.getPlayerBackRow().add(new Minion(desiredCard));
                currentPlayer.getGameHand().remove(this.handIdx);
            }
        }


    }

    /**
     * Method that identifies the command and executes it
     *
     * @param output ArrayNode where the output is written
     * @param game  current game
     * @param objectMapper objectMapper used in current game
     */
    public void execute(final ArrayNode output, final Game game, final ObjectMapper objectMapper) {

        switch (this.command) {
            case "getPlayerDeck" -> {
                getPlayerDeck(output, game, objectMapper);
            }
            case "getPlayerHero" -> {
                getPlayerHero(output, game, objectMapper);
            }
            case "getPlayerTurn" -> {
                getPlayerTurn(output, game, objectMapper);
            }
            case "getCardsInHand" -> {
                getCardsInHand(output, game, objectMapper);
            }
            case "getCardsOnTable" -> {
                getCardsOnTable(output, game, objectMapper);
            }
            case "getCardAtPosition" -> {
                getCardAtPos(output, game, objectMapper);
            }
            case "getPlayerMana" -> {
               getPlayerMana(output, game, objectMapper);
            }
            case "getEnvironmentCardsInHand" -> {
                getEnvironmentHand(output, game, objectMapper);
            }
            case "getFrozenCardsOnTable" -> {
                getFrozenCards(output, game, objectMapper);
            }
            case "getTotalGamesPlayed" -> {
                getTotalGames(output, game, objectMapper);
            }
            case "getPlayerOneWins" -> {
                getPlayerOneWins(output, game, objectMapper);
            }
            case "getPlayerTwoWins" -> {
                getPlayerTwoWins(output, game, objectMapper);
            }
            case "endPlayerTurn" -> {
                game.endTurn();
            }
            case "placeCard" -> {
                placeCard(output, game, objectMapper);
            }
            default -> {
            }
        }
    }
}
