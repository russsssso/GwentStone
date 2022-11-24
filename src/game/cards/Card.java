package game.cards;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fileio.CardInput;

import java.util.ArrayList;

@JsonIgnoreProperties(value = { "frozen", "used" })
public abstract class Card {
    private int mana;
    protected int attackDamage;
    protected int health;
    protected Boolean frozen;
    protected Boolean used;
    private String description;
    private ArrayList<String> colors;
    private String name;

    public Card() { }
    public Card(final CardInput input) {
        this.setName(input.getName());
        this.setDescription(input.getDescription());
        this.setMana(input.getMana());
        this.setColors(input.getColors());
    }

    public Card(final Card input) {
        this.setName(input.getName());
        this.setDescription(input.getDescription());
        this.setMana(input.getMana());
        this.setColors(input.getColors());
    }

    public final int getMana() {
        return mana;
    }

    public final void setMana(final int mana) {
        this.mana = mana;
    }

    public final String getDescription() {
        return description;
    }

    public final void setDescription(final String description) {
        this.description = description;
    }

    public final ArrayList<String> getColors() {
        return colors;
    }

    public final void setColors(final ArrayList<String> colors) {
        this.colors = colors;
    }

    public final String getName() {
        return name;
    }

    public final void setName(final String name) {
        this.name = name;
    }
}
