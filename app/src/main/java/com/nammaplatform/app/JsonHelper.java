package com.nammaplatform.app;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Helper class to load and parse train data from the local assets/stations.json file.
 * Uses Android AssetManager + BufferedReader; no third-party libraries.
 */
public class JsonHelper {

    private static final String STATIONS_FILE = "stations.json";

    /**
     * Reads the stations.json asset file, locates the given station by name,
     * and returns a list of Train objects for that station.
     *
     * @param context     Android Context (used for AssetManager)
     * @param stationName The station name to look up (must match JSON exactly)
     * @return ArrayList of Train objects, or an empty list if not found / on error
     */
    public static ArrayList<Train> getTrainsForStation(Context context, String stationName) {
        ArrayList<Train> trainList = new ArrayList<>();

        try {
            // Read JSON file from assets using AssetManager + BufferedReader
            String jsonString = readAssetFile(context, STATIONS_FILE);
            if (jsonString == null || jsonString.isEmpty()) {
                return trainList;
            }

            // Parse root object
            JSONObject root = new JSONObject(jsonString);
            JSONArray stationsArray = root.getJSONArray("stations");

            // Iterate stations to find a match
            for (int i = 0; i < stationsArray.length(); i++) {
                JSONObject stationObject = stationsArray.getJSONObject(i);
                String name = stationObject.getString("stationName");

                if (name.equalsIgnoreCase(stationName)) {
                    JSONArray trainsArray = stationObject.getJSONArray("trains");

                    // Build Train objects for this station
                    for (int j = 0; j < trainsArray.length(); j++) {
                        JSONObject trainObject = trainsArray.getJSONObject(j);

                        String trainName = trainObject.getString("trainName");
                        String departure = trainObject.getString("departure");
                        int platform = trainObject.getInt("platform");

                        // Parse coach list
                        ArrayList<String> coaches = new ArrayList<>();
                        JSONArray coachArray = trainObject.getJSONArray("coaches");
                        for (int k = 0; k < coachArray.length(); k++) {
                            coaches.add(coachArray.getString(k));
                        }

                        trainList.add(new Train(trainName, departure, platform, coaches));
                    }
                    break; // Station found; no need to continue
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return trainList;
    }

    /**
     * Reads a text file from the app's assets folder and returns its content as a String.
     *
     * @param context  Android Context
     * @param fileName File name within assets/
     * @return Full file content as a String, or null on failure
     */
    private static String readAssetFile(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;

        try {
            inputStream = context.getAssets().open(fileName);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            // Close streams safely
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ignored) {}
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignored) {}
            }
        }

        return stringBuilder.toString();
    }
}
