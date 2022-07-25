package ponggame;

import javax.swing.*;
import java.util.*;

/**
 *
 * @author Pham Nhat Quang
 */
public class Clock {

    private final JLabel time; //Label of clock (show the time, e.g 01:02:36
    private int currentHour; //Number of hours on label
    private int currentMinute; //Number of minutes on label
    private int currentSecond; //Number of seconds on label
    java.util.Timer timer;

    /**
     * Create new clock
     * @param time Initial time of the clock
     */
    public Clock(JLabel time) {
        String times[] = time.getText().split(":"); //Extract information from time label
        this.currentHour = Integer.parseInt(times[0]); //Get hours
        this.currentMinute = Integer.parseInt(times[1]); //Get minutes
        this.currentSecond = Integer.parseInt(times[2]); //Get seconds
        this.time = time; //Assign label of this clock as the initial time label to update on that label

    }

    /**
     * Reset the clock to 00:00:00
     */
    public void reset() {
        currentSecond = 0;
        currentMinute = 0;
        currentHour = 0;
        time.setText(String.format("%02d:%02d:%02d", currentHour, currentMinute, currentSecond));
        
    }

    /**
     * Start the clock
     */
    public void start() {
        reset();
        timer = new java.util.Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (currentSecond == 60) {
                    currentMinute++;
                    if (currentMinute == 60) {
                        currentHour++;
                        currentMinute = 0;
                    }
                    currentSecond = 0;
                }
                time.setText(String.format("%02d:%02d:%02d", currentHour, currentMinute, currentSecond));
                currentSecond++;
            }

        }, 0, 1000);
    }

    /**
     * Pause clock from working
     */
    public void pause() {
        try {
            timer.cancel();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * Resume clock from paused state
     */
    public void resume() {
        try {
            if (timer!=null) timer.cancel();
        } catch (Exception e) {
            System.err.println(e);
        }
        timer = new java.util.Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (currentSecond == 60) {
                    currentMinute++;
                    if (currentMinute == 60) {
                        currentHour++;
                        currentMinute = 0;
                    }
                    currentSecond = 0;
                }
                time.setText(String.format("%02d:%02d:%02d", currentHour, currentMinute, currentSecond));
                currentSecond++;
            }

        }, 0, 1000);
    }

    /**
     * Get current time of this clock in number of seconds
     * @return time in seconds
     */
    public int getTime() {
        return this.currentHour * 3600 + this.currentMinute * 60 + this.currentSecond;
    }

    /**
     * Get label of this clock (time in format hh:mm:ss
     * @return Label of clock
     */
    public JLabel getLabel() {
        return this.time;
    }

    public void stop(){
        this.timer.cancel();
    }
}
