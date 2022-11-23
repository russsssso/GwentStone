package game;

import java.util.ArrayList;
public final class Utils {

    private static Utils instance = null;
    private final ArrayList<String> minions;
    private final ArrayList<String> minionsWithAbility;
    private final ArrayList<String> heroes;
    private final ArrayList<String> environment;

    private final int heroHp = 30;
    private final int maxMana = 10;


    public Utils() {
        environment = new ArrayList<>();
        minionsWithAbility = new ArrayList<>();
        heroes = new ArrayList<>();
        minions = new ArrayList<>();

        minions.add("Sentinel");
        minions.add("Berserker");
        minions.add("Goliath");
        minions.add("Warden");
        minions.add("Miraj");
        minions.add("The Ripper");
        minions.add("Disciple");
        minions.add("The Cursed One");

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

    public ArrayList<String> getEnvironment() {
        return environment;
    }
}
