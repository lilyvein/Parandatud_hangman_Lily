package models.datastructures;

import views.View;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;

/**
 * Data structure for database ranking (table scores)
 */
public record DataScores(LocalDateTime gameTime, String playerName, String word, String missedLetters,
                         int timeSeconds) {



    /**
     * Konstruktor
     */
    public DataScores(LocalDateTime gameTime, String playerName, String word, String missedLetters, int timeSeconds) {
        this.gameTime = gameTime;
        this.playerName = playerName;
        this.word = word;
        this.missedLetters = missedLetters;
        this.timeSeconds = timeSeconds;
    }

    // Getters

    /**
     * Tagastab kuupäeva ja aja
     * @return LocalDateTime
     */
    public LocalDateTime getGameTime() {
        return gameTime;
    }

    /**
     * Tagastab mängija nime
     * @return String
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Tagastab äraarvatud sõna
     * @return String
     */
    public String getGuessWord() {
        return word;
    }

    /**
     * Tasgastab valesti sisestatud tähed
     * @return String
     */
    public String getMissingLetters() {
        return missedLetters;
    }
    /**
     * Tagastab mängu aja sekundites
     * @return int
     */
    public int getTimeSeconds() {
        LocalDateTime endTime = LocalDateTime.now();
        Temporal startTime = null;
        Duration duration = Duration.between(startTime, endTime);
        return (int) duration.getSeconds();
    }
}
