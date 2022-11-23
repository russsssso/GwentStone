package game.cards;

import fileio.CardInput;

public final class Minion extends Card {
    public Minion(final CardInput input) {
        super(input);
        this.setAttackDamage(input.getAttackDamage());
        this.setHealth(input.getHealth());
    }

    public Minion(final Card input) {
        super(input);
        this.setAttackDamage(((Minion) input).getAttackDamage());
        this.setHealth(((Minion) input).getHealth());
    }

    public void setAttackDamage(final int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public int getAttackDamage() {
        return this.attackDamage;
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

}
