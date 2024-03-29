package game;

import java.util.ArrayList;
public final class Utils {

    private static Utils instance = null;
    private final ArrayList<String> minions;

    private final ArrayList<String> tanks;
    private final ArrayList<String> minionsWithAbility;
    private final ArrayList<String> heroes;
    private final ArrayList<String> environment;
    private final ArrayList<String> frontRowCards;
    private final ArrayList<String> backRowCards;

    private final int heroHp = 30;
    private final int maxMana = 10;

    private final int rowSize = 5;


    public Utils() {
        environment = new ArrayList<String>();
        minionsWithAbility = new ArrayList<String>();
        heroes = new ArrayList<String>();
        minions = new ArrayList<String>();
        backRowCards = new ArrayList<String>();
        frontRowCards = new ArrayList<String>();
        tanks = new ArrayList<String>();

        minions.add("Sentinel");
        minions.add("Berserker");
        minions.add("Goliath");
        minions.add("Warden");
        minions.add("Miraj");
        minions.add("The Ripper");
        minions.add("Disciple");
        minions.add("The Cursed One");

        tanks.add("Goliath");
        tanks.add("Warden");

        minionsWithAbility.add("Miraj");
        minionsWithAbility.add("The Ripper");
        minionsWithAbility.add("Disciple");
        minionsWithAbility.add("The Cursed One");

        environment.add("Firestorm");
        environment.add("Winterfell");
        environment.add("Heart Hound");

        heroes.add("Lord Royce");
        heroes.add("Empress Thorina");
        heroes.add("King Mudface");
        heroes.add("General Kocioraw");

        backRowCards.add("Sentinel");
        backRowCards.add("Berserker");
        backRowCards.add("Disciple");
        backRowCards.add("The Cursed One");

        frontRowCards.add("Goliath");
        frontRowCards.add("Warden");
        frontRowCards.add("Miraj");
        frontRowCards.add("The Ripper");
    }

    /**
     * Singleton implementation
     * @return the Singleton instance
     */
    public static Utils getInstance() {
        if (instance == null) {
            instance = new Utils();
        }
        return instance;
    }

    public ArrayList<String> getMinions() {
        return minions;
    }

    public ArrayList<String> getTanks() {
        return tanks;
    }

    public ArrayList<String> getFrontRowCards() {
        return frontRowCards;
    }

    public ArrayList<String> getBackRowCards() {
        return backRowCards;
    }

    public ArrayList<String> getMinionsWithAbility() {
        return minionsWithAbility;
    }

    public ArrayList<String> getHeroes() {
        return heroes;
    }

    public int getHeroHp() {
        return heroHp;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public int getRowSize() {
        return rowSize;
    }

    public ArrayList<String> getEnvironment() {
        return environment;
    }
}
