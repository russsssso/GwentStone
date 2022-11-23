package game;



import fileio.CardInput;
import fileio.DecksInput;
import game.cards.Card;
import game.cards.Environment;
import game.cards.Hero;
import game.cards.Minion;

import java.util.ArrayList;

public final class Player {
    private int nrDecks;
    private int nrCardsInDeck;
    private ArrayList<ArrayList<Card>> decks;

    private ArrayList<Card> gameDeck;

    private ArrayList<Card> gameHand;
    private Card heroCard;

    private int mana;
    public Player(final DecksInput input) {
        this.setNrDecks(input.getNrDecks());
        this.setNrCardsInDeck(input.getNrCardsInDeck());
        this.setDecks(input);
    }

    public int getNrDecks() {
        return nrDecks;
    }

    public void setNrDecks(final int nrDecks) {
        this.nrDecks = nrDecks;
    }

    public int getNrCardsInDeck() {
        return nrCardsInDeck;
    }

    public void setNrCardsInDeck(final int nrCardsInDeck) {
        this.nrCardsInDeck = nrCardsInDeck;
    }

    public ArrayList<ArrayList<Card>> getDecks() {
        return decks;
    }

    /**
     * Sets all the avalible decks for the player
     * @param input fileio input
     */
    public void setDecks(final DecksInput input) {
        this.decks = new ArrayList<>(this.nrDecks);

        Utils nameList = Utils.getInstance();
        for (int i = 0; i < this.getNrDecks(); i++) {
            this.decks.add(new ArrayList<>(this.nrCardsInDeck));
            for (CardInput j : input.getDecks().get(i)) {
                if (nameList.getMinions().contains(j.getName())) {
                    decks.get(i).add(new Minion(j));
                } else if (nameList.getEnvironment().contains(j.getName())) {
                    decks.get(i).add(new Environment(j));
                }
            }
        }
    }

    /**
     * Gets the chosen Deck for current game
     */
    public ArrayList<Card> getGameDeck() {
        return gameDeck;
    }

    /**
     * Sets the desitred Deck for current game
     * @param index desired Deck's index
     */
    public void setGameDeck(final int index) {
        this.gameHand = new ArrayList<>();
        ArrayList<Card> game = this.decks.get(index);
        this.gameDeck = new ArrayList<>();
        Utils nameList = Utils.getInstance();
        for (Card card : game) {
            if (nameList.getMinions().contains(card.getName())) {
                this.gameDeck.add(new Minion(card));
            } else if (nameList.getEnvironment().contains(card.getName())) {
                this.gameDeck.add(new Environment(card));
            }
        }
    }

    public ArrayList<Card> getGameHand() {
        return gameHand;
    }

    /**
     * Transfers the next card from deck to player's hand
     */
    public void drawCard() {
        if (!this.gameDeck.isEmpty()) {
            this.gameHand.add(this.gameDeck.get(0));
            this.gameDeck.remove(0);
        }
    }

    public Card getHeroCard() {
        return heroCard;
    }

    public void setHeroCard(final CardInput heroCard) {
        this.heroCard = new Hero(heroCard);
    }

    public int getMana() {
        return mana;
    }

    public void setMana(final int mana) {
        this.mana = mana;
    }

    /**
     * Used for incrementing player's mana at the beginning of each round
     */
    public void incMana() {
        Utils util = Utils.getInstance();
        if (this.mana >= util.getMaxMana()) {
            this.mana = util.getMaxMana();
        } else {
            this.mana += 1;
        }
    }

}
