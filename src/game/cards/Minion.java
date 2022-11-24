package game.cards;

import fileio.CardInput;

public final class Minion extends Card {
    public Minion(final CardInput input) {
        super(input);
        this.setAttackDamage(input.getAttackDamage());
        this.setHealth(input.getHealth());
        this.setFrozen(false);
        this.setUsed(false);
    }

    public Minion(final Card input) {
        super(input);
        this.setAttackDamage(((Minion) input).getAttackDamage());
        this.setHealth(((Minion) input).getHealth());
        this.setFrozen(((Minion) input).getFrozen());
        this.setUsed(((Minion) input).getUsed());
    }

    public void setAttackDamage(final int attackDamage) {
        if (attackDamage >= 0){
            this.attackDamage = attackDamage;
        } else {
            this.attackDamage = 0;
        }

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

    public Boolean getUsed() {
        return this.used;
    }

    public void setUsed(final Boolean used) {
        this.used = used;
    }


    /**
     * Method used by minion card when recieving damage from environment
     * or minion cards
     */
    public void recieveDamage(final int damage) {
        this.health = this.health - damage;
    }

}
