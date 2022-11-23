package game.cards;

import fileio.CardInput;
import game.Utils;

public final class Hero extends Card {
    public Hero(final CardInput input) {
        super(input);
        Utils utils = Utils.getInstance();
        int heroHealth = utils.getHeroHp();
        this.setHealth(heroHealth);
    }

    public void setHealth(final int health) {
        this.health = health;
    }

    public int getHealth() {
        return this.health;
    }
}
