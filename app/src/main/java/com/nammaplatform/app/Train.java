package com.nammaplatform.app;

import java.util.ArrayList;

/**
 * Model class representing a train with its details and coach layout.
 */
public class Train {

    private String trainName;
    private String departure;
    private int platform;
    private ArrayList<String> coaches;

    // Constructor
    public Train(String trainName, String departure, int platform, ArrayList<String> coaches) {
        this.trainName = trainName;
        this.departure = departure;
        this.platform = platform;
        this.coaches = coaches;
    }

    // Getters
    public String getTrainName() {
        return trainName;
    }

    public String getDeparture() {
        return departure;
    }

    public int getPlatform() {
        return platform;
    }

    public ArrayList<String> getCoaches() {
        return coaches;
    }

    // Setters
    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public void setCoaches(ArrayList<String> coaches) {
        this.coaches = coaches;
    }
}
