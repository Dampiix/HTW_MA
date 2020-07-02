package com.example.spacegame_ma.storage;

public interface LocalStorageI {

    /**
     * compares the current score with the highscore
     *
     * @param score
     * @return true if new score is higher than the highscore
     */
    boolean compareScore(int score);

    /**
     * checks if score is higher, saves if its a new highscore
     * */
    void saveNewHighscore(int score);

}
