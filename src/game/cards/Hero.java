package game.cards;

import fileio.CardInput;
import game.Utils;

public final class Hero extends Card {
    public Hero(final CardInput input) {
        super(input);
        Utils utils = Utils.getInstance();
        int heroHealth = utils.getHeroHp();
        this.setHealth(heroHealth);
        this.setFrozen(false);
        this.setUsed(false);
    }

    public Hero(final Card input) {
        super(input);
        Utils utils = Utils.getInstance();
        int heroHealth = utils.getHeroHp();
        this.setHealth(input.health);
        this.setFrozen(false);
        this.setUsed(false);
    }

    public void setHealth(final int health) {
        this.health = health;
    }

    public int getHealth() {
        return this.health;
    }

    public Boolean getFrozen() {
        return this.frozen;
    }

    public void setFrozen(final Boolean frozen) {
        this.frozen = frozen;
    }

    public Boolean getUsed() {
        return this.used;
    }

    public void setUsed(final Boolean used) {
        this.used = used;
    }


    /**
     * Method used by minion and hero cards when recieving damage from environment
     * or minion cards
     */
    public void recieveDamage(final int damage) {
        this.health = this.health - damage;
    }
}
