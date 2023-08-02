package helpers;


import views.View;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Class game time
 */
public class GameTimer {
    private int minutes;
    private int seconds;
    private boolean running; // true or false
    private final Timer timer;

    /**
     * Game time constructor. Every time a new game is started, this object is created again!
     * @param view To access label lblTime
     */
    public GameTimer(View view) {
        this.minutes = 0; // Initial state
        this.seconds = 0; // Initial state
        this.running = false; // Initial state
        timer = new Timer(1000, (ActionEvent e) ->{
            seconds++; // seconds increment by one
            if(seconds >= 60) { // if the seconds are 60 or more
                seconds = 0; // seconds start from zero after 60 seconds are up
                minutes++; // minutes increment by one
            }
            view.getGameBoard().getLblTime().setText(formatGameTime());
        });
    }

    /**
     * Format the minutes and seconds to a human-reasonable 00:00
     * @return Formatted game time
     */
    private String formatGameTime() {
        return String.format("%02d:%02d", minutes, seconds);
    }
    /**
     * Let's start the game time
     */
    public void startTimer() {
        this.timer.start();
        //this.running = true;
        System.out.println("Timer started!");
    }

    /**
     * Let's stop the game time
     */
    public void stopTimer() {
        this.timer.stop();
        //this.running = false;
        System.out.println("Timer stopped! Played time: " + getPlayedTimeInSeconds() + " seconds");
    }


    /**
     * Is the game running?
     * @return true or false
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Sets whether the game is running or not
     * @param running running true or not running false
     */
    public void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * Sets the minutes
     * @param minutes minutes
     */
    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    /**
     * Sets the seconds
     * @param seconds sekundid
     */
    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    /**
     * Adds seconds to the time (penalty)
     * @param seconds seconds
     */
    public void addSeconds(int seconds) {
        if((this.seconds + seconds) >= 60) {
            minutes++;
            this.seconds = (this.seconds + seconds) - 60;
        } else {
            this.seconds += seconds;
        }
    }

    /**
     * Returns the game time in seconds
     * @return minutes multiplied by 60 plus seconds
     */
    public int getPlayedTimeInSeconds() {
        return (this.minutes * 60) + seconds;

    }




}