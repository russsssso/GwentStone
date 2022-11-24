package game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import fileio.Coordinates;
import game.cards.Card;
import game.cards.Environment;
import game.cards.Hero;
import game.cards.Minion;

import java.util.ArrayList;

public final class Action {
    private final String command;
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
                break;
            case "useEnvironmentCard":
                this.handIdx = action.getHandIdx();
                this.affectedRow = action.getAffectedRow();
                break;
            case "cardUsesAttack":
            case "cardUsesAbility":
                this.cardAttacker = new Coordinates();
                this.cardAttacked = new Coordinates();
                this.cardAttacked.setX(action.getCardAttacked().getX());
                this.cardAttacked.setY(action.getCardAttacked().getY());
                this.cardAttacker.setX(action.getCardAttacker().getX());
                this.cardAttacker.setY(action.getCardAttacker().getY());
                break;
            case "useAttackHero":
                this.cardAttacker = new Coordinates();
                this.cardAttacker.setX(action.getCardAttacker().getX());
                this.cardAttacker.setY(action.getCardAttacker().getY());
                break;
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
        playerHero.putPOJO("output", new Hero(game.getPlayers().get(this.playerIdx).getHeroCard()));
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
        for (ArrayList<Card> rows : game.getBoard()) {
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
        Minion card;
        if (game.getCardAtPosition(x, y) != null) {
            card = new Minion(game.getCardAtPosition(x, y));
        } else {
            cardAtPosition.put("x", x);
            cardAtPosition.put("y", y);
            cardAtPosition.putPOJO("output", "No card available at that position.");
            out.add(cardAtPosition);
            return;
        }
        cardAtPosition.putPOJO("output", card);
        cardAtPosition.put("x", x);
        cardAtPosition.put("y", y);

        out.add(cardAtPosition);
    }

    private void getPlayerMana(final ArrayNode out, final Game game, final ObjectMapper mapper) {
        ObjectNode playerMana = mapper.createObjectNode();
        playerMana.put("command", this.command);
        playerMana.put("playerIdx", this.playerIdx + 1);
        playerMana.put("output", game.getPlayers().get(this.playerIdx).getMana());
        out.add(playerMana);
    }
    private void getEnvironmentHand(final ArrayNode out,
                                    final Game game, final ObjectMapper mapper) {
        ObjectNode playerEnvironment = mapper.createObjectNode();
        ArrayList<Card> environments = new ArrayList<>();
        Utils utils = Utils.getInstance();
        for (Card card : game.getPlayers().get(this.playerIdx).getGameHand()) {
            if (utils.getEnvironment().contains(card.getName())) {
                environments.add(new Environment(card));
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
        frozen.putPOJO("output", game.getFrozenCards());
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

    private void  useEnvironmentCard(final ArrayNode out,
                                     final Game game, final ObjectMapper mapper) {
        Player currentPlayer = game.getPlayers().get(game.getCurrentPlayer());
        Card desiredCard = currentPlayer.getGameHand().get(this.handIdx);
        Utils utils = Utils.getInstance();

        if (!utils.getEnvironment().contains(desiredCard.getName())) {
            ObjectNode notEnvironment = mapper.createObjectNode();
            notEnvironment.put("command", this.command);
            notEnvironment.put("handIdx", this.handIdx);
            notEnvironment.put("affectedRow", this.affectedRow);
            notEnvironment.put("error", "Chosen card is not of type environment.");
            out.add(notEnvironment);
            return;
        }

        if (currentPlayer.getMana() < desiredCard.getMana()) {
            ObjectNode noMana = mapper.createObjectNode();
            noMana.put("command", this.command);
            noMana.put("handIdx", this.handIdx);
            noMana.put("affectedRow", this.affectedRow);
            noMana.put("error", "Not enough mana to use environment card.");
            out.add(noMana);
            return;
        }

        if (this.affectedRow == game.getPlayerBackRowNr()
                || this.affectedRow == game.getPlayerFrontRowNr()) {
            ObjectNode notEnemy = mapper.createObjectNode();
            notEnemy.put("command", this.command);
            notEnemy.put("handIdx", this.handIdx);
            notEnemy.put("affectedRow", this.affectedRow);
            notEnemy.put("error", "Chosen row does not belong to the enemy.");
            out.add(notEnemy);
            return;
        }

        int frontRowSize = game.getPlayerFrontRow().size();
        int backRowSize = game.getPlayerFrontRow().size();

        if (desiredCard.getName().equals("Heart Hound")) {
            if (this.affectedRow == game.getOpponentFrontRowNr()
                    && frontRowSize == Utils.getInstance().getRowSize()) {
                noSpace(out, mapper);
                return;
            } else if (this.affectedRow == game.getOpponentBackRowNr()
                    && backRowSize == Utils.getInstance().getRowSize()) {
                noSpace(out, mapper);
            }
        }

        ArrayList<Card> row = game.getRow(this.affectedRow);
        switch (desiredCard.getName()) {
            case "Firestorm":
                for (int i = 0; i < row.size(); i++) {
                    Card card = row.get(i);
                    ((Minion) card).recieveDamage(1);
                    if (((Minion) card).getHealth() <= 0) {
                        row.remove(card);
                        i--;
                    }
                }
                break;
            case "Winterfell":
                for (Card card : row) {
                    ((Minion) card).setFrozen(true);
                }
                break;
            default:
                Card maxHpCard = row.get(0);
                for (Card card : row) {
                    if (((Minion) card).getHealth() > ((Minion) maxHpCard).getHealth()) {
                        maxHpCard = card;
                    }
                }
                Minion convertedMinion = new Minion(maxHpCard);
                row.remove(maxHpCard);

                if (this.affectedRow == game.getOpponentBackRowNr()) {
                    game.getPlayerBackRow().add(convertedMinion);
                } else if (this.affectedRow == game.getOpponentFrontRowNr()) {
                    game.getPlayerFrontRow().add(convertedMinion);
                }
                break;

        }
        currentPlayer.useMana(desiredCard.getMana());
        currentPlayer.getGameHand().remove(desiredCard);

    }

    private void cardUsesAttack(final ArrayNode out, final Game game, final ObjectMapper mapper) {
        Utils utils = Utils.getInstance();
        Player currentPLayer = game.getPlayers().get(game.getCurrentPlayer());
        Minion attackerCard = (Minion) game.getCardAtPosition(this.cardAttacker.getX(),
                this.cardAttacker.getY());
        Minion attackedCard = (Minion) game.getCardAtPosition(this.cardAttacked.getX(),
                this.cardAttacked.getY());

        if (attackerCard == null || attackedCard == null) {
            return;
        }

        if (notEnemyError(out, game, mapper)) {
            return;
        }

        if (cardUsedError(out, mapper, attackerCard)) {
            return;
        }

        if (cardFrozenError(out, mapper, attackerCard)) {
            return;
        }

        if (notTank(out, game, mapper, attackedCard)) {
            return;
        }

        attackedCard.recieveDamage(attackerCard.getAttackDamage());
        if (attackedCard.getHealth() <= 0) {
            game.getRow(this.cardAttacked.getX()).remove(attackedCard);
        }
        attackerCard.setUsed(true);
    }

    private void cardUsesAbility(final ArrayNode out, final Game game, final ObjectMapper mapper) {
        Utils utils = Utils.getInstance();
        Player currentPLayer = game.getPlayers().get(game.getCurrentPlayer());
        Minion attackerCard = (Minion) game.getCardAtPosition(this.cardAttacker.getX(),
                this.cardAttacker.getY());
        Minion attackedCard = (Minion) game.getCardAtPosition(this.cardAttacked.getX(),
                this.cardAttacked.getY());

        if (attackerCard == null || attackedCard == null) {
            return;
        }

        if (cardFrozenError(out, mapper, attackerCard)) {
            return;
        }

        if (cardUsedError(out, mapper, attackerCard)) {
            return;
        }

        if (!attackerCard.getName().equals("Disciple")) {
            if (notEnemyError(out, game, mapper)) {
                return;
            }
            if (notTank(out, game, mapper, attackedCard)) {
                return;
            }
            int aux;
            switch (attackerCard.getName()) {
                case "The Ripper":
                    attackedCard.setAttackDamage(attackedCard.getAttackDamage() - 2);
                    break;
                case "Miraj":
                    aux = attackerCard.getHealth();
                    attackerCard.setHealth(attackedCard.getHealth());
                    attackedCard.setHealth(aux);
                    break;
                default:
                    aux = attackedCard.getHealth();
                    attackedCard.setHealth(attackedCard.getAttackDamage());
                    attackedCard.setAttackDamage(aux);
                    if (attackedCard.getHealth() <= 0) {
                        game.getRow(this.cardAttacked.getX()).remove(attackedCard);
                    }
                    break;
            }
        } else {
            if (notAllyError(out, game, mapper)) {
                return;
            }
            attackedCard.recieveDamage(-2);
        }
        attackerCard.setUsed(true);
    }

    private void useAttackHero(final ArrayNode out, final Game game, final ObjectMapper mapper) {
        Utils utils = Utils.getInstance();
        Player currentPLayer = game.getPlayers().get(game.getCurrentPlayer());
        Minion attackerCard = (Minion) game.getCardAtPosition(this.cardAttacker.getX(),
                this.cardAttacker.getY());
        Hero attackedCard = game.getOpponentHero();

        if (attackerCard == null || attackedCard == null) {
            return;
        }

        if (cardFrozenError(out, mapper, attackerCard)) {
            return;
        }

        if (cardUsedError(out, mapper, attackerCard)) {
            return;
        }

        if(game.opponentHasTank()){
            ObjectNode hasTank = mapper.createObjectNode();
            hasTank.put("command", this.command);
            hasTank.putPOJO("cardAttacker", this.cardAttacker);
            hasTank.put("error", "Attacked card is not of type 'Tank'.");
            out.add(hasTank);
        }

        attackedCard.recieveDamage(attackerCard.getAttackDamage());
        attackerCard.setUsed(true);
        if (attackedCard.getHealth() <= 0){
            game.won(out, mapper);
        }
    }

    /*
    * Helper methods for detecting and writing command errors*/
    private void noSpace(final ArrayNode out, final ObjectMapper mapper) {
        ObjectNode noSpace = mapper.createObjectNode();
        noSpace.put("command", this.command);
        noSpace.put("handIdx", this.handIdx);
        noSpace.put("affectedRow", this.affectedRow);
        noSpace.put("error", "Cannot steal enemy card since the player's row is full.");
        out.add(noSpace);
        return;
    }

    private boolean notTank(final ArrayNode out, final Game game, ObjectMapper mapper, final Minion attackedCard) {
        if (game.opponentHasTank()
                && !Utils.getInstance().getTanks().contains(attackedCard.getName())) {
            ObjectNode tank = mapper.createObjectNode();
            tank.put("command", this.command);
            tank.putPOJO("cardAttacker", this.cardAttacker);
            tank.putPOJO("cardAttacked", this.cardAttacked);
            tank.put("error", "Attacked card is not of type 'Tank'.");
            out.add(tank);
            return true;
        }
        return false;
    }

    private Boolean notEnemyError(final ArrayNode out, final Game game, final ObjectMapper mapper) {
        if (this.cardAttacked.getX() != game.getOpponentBackRowNr()
                && this.cardAttacked.getX() != game.getOpponentFrontRowNr()) {
            ObjectNode notEnemy = mapper.createObjectNode();
            notEnemy.put("command", this.command);
            notEnemy.putPOJO("cardAttacker", this.cardAttacker);
            if (this.cardAttacked != null){
                notEnemy.putPOJO("cardAttacked", this.cardAttacked);
            }
            notEnemy.put("error", "Attacked card does not belong to the enemy.");
            out.add(notEnemy);
            return true;
        }
        return false;
    }

    private Boolean notAllyError(final ArrayNode out, final Game game, final ObjectMapper mapper) {
        if (this.cardAttacked.getX() != game.getPlayerBackRowNr()
                && this.cardAttacked.getX() != game.getPlayerFrontRowNr()) {
            ObjectNode notAlly = mapper.createObjectNode();
            notAlly.put("command", this.command);
            notAlly.putPOJO("cardAttacker", this.cardAttacker);
            notAlly.putPOJO("cardAttacked", this.cardAttacked);
            notAlly.put("error", "Attacked card does not belong to the current player.");
            out.add(notAlly);
            return true;
        }
        return false;
    }

    private boolean cardUsedError(final ArrayNode out, final ObjectMapper mapper, final Minion attackerCard) {
        if (attackerCard.getUsed()) {
            ObjectNode used = mapper.createObjectNode();
            used.put("command", this.command);
            used.putPOJO("cardAttacker", this.cardAttacker);
            if (this.cardAttacked != null){
                used.putPOJO("cardAttacked", this.cardAttacked);
            }
            used.put("error", "Attacker card has already attacked this turn.");
            out.add(used);
            return true;
        }
        return false;
    }

    private boolean cardFrozenError(final ArrayNode out, final ObjectMapper mapper, final Minion attackerCard) {
        if (attackerCard.getFrozen()) {
            ObjectNode frozen = mapper.createObjectNode();
            frozen.put("command", this.command);
            frozen.putPOJO("cardAttacker", this.cardAttacker);
            if (this.cardAttacked != null){
                frozen.putPOJO("cardAttacked", this.cardAttacked);
            }
            frozen.put("error", "Attacker card is frozen.");
            out.add(frozen);
            return true;
        }
        return false;
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
            case "getPlayerDeck" -> getPlayerDeck(output, game, objectMapper);
            case "getPlayerHero" -> getPlayerHero(output, game, objectMapper);
            case "getPlayerTurn" -> getPlayerTurn(output, game, objectMapper);
            case "getCardsInHand" -> getCardsInHand(output, game, objectMapper);
            case "getCardsOnTable" -> getCardsOnTable(output, game, objectMapper);
            case "getCardAtPosition" -> getCardAtPos(output, game, objectMapper);
            case "getPlayerMana" -> getPlayerMana(output, game, objectMapper);
            case "getEnvironmentCardsInHand" -> getEnvironmentHand(output, game, objectMapper);
            case "getFrozenCardsOnTable" -> getFrozenCards(output, game, objectMapper);
            case "getTotalGamesPlayed" -> getTotalGames(output, game, objectMapper);
            case "getPlayerOneWins" -> getPlayerOneWins(output, game, objectMapper);
            case "getPlayerTwoWins" -> getPlayerTwoWins(output, game, objectMapper);
            case "endPlayerTurn" -> game.endTurn();
            case "placeCard" -> placeCard(output, game, objectMapper);
            case "useEnvironmentCard" -> useEnvironmentCard(output, game, objectMapper);
            case "cardUsesAttack" -> cardUsesAttack(output, game, objectMapper);
            case "cardUsesAbility" -> cardUsesAbility(output, game, objectMapper);
            case "useAttackHero" -> useAttackHero(output, game, objectMapper);
            default -> {
            }
        }
    }
}
