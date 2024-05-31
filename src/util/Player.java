package util;

public class Player {
    private int victories;
    private int draws;
    private int defeats;


    public void increaseVictories() {
        victories++;
    }

    public void increaseDraws() {
        draws++;
    }

    public void increaseDefeats() {
        defeats++;
    }

    // Getters
    public int getVictories() {
        return victories;
    }

    public void setVictories(int victories) {
        this.victories = victories;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public int getDefeats() {
        return defeats;
    }

    public void setDefeats(int defeats) {
        this.defeats = defeats;
    }
}