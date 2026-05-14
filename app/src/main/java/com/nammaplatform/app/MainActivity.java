package com.nammaplatform.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * MainActivity — Station selection screen.
 * Displays the app name in Kannada, a station spinner, and a button to view trains.
 */
public class MainActivity extends AppCompatActivity {

    private Spinner spinnerStation;

    // Station list — must match stationName values in stations.json
    private final String[] stations = {
            "Mysuru",
            "Mandya",
            "Maddur",
            "Ramanagara",
            "Bengaluru City"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerStation = findViewById(R.id.spinner_station);
        Button btnViewTrains = findViewById(R.id.btn_view_trains);

        // Populate station spinner with a custom white-text adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                stations
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStation.setAdapter(adapter);

        // Navigate to TrainListActivity with selected station name
        btnViewTrains.setOnClickListener(v -> {
            String selectedStation = spinnerStation.getSelectedItem().toString();
            if (selectedStation.isEmpty()) {
                Toast.makeText(this, "Please select a station.", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(MainActivity.this, TrainListActivity.class);
            intent.putExtra("stationName", selectedStation);
            startActivity(intent);
        });
    }
}
