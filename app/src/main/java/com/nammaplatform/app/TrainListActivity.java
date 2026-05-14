package com.nammaplatform.app;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * TrainListActivity — Displays the next 3 trains for the selected station.
 */
public class TrainListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TrainAdapter adapter;
    private ArrayList<Train> trainList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_list);

        // Retrieve station name from intent
        String stationName = getIntent().getStringExtra("stationName");
        if (stationName == null || stationName.isEmpty()) {
            stationName = "Station";
        }

        // Set up toolbar with back button and station name as title
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(stationName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Subtitle TextView
        TextView tvSubtitle = findViewById(R.id.tv_next_trains_subtitle);
        tvSubtitle.setText(getString(R.string.next_trains_title));

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recycler_trains);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load trains from JSON for the selected station
        trainList = JsonHelper.getTrainsForStation(this, stationName);

        if (trainList == null || trainList.isEmpty()) {
            Toast.makeText(this, getString(R.string.no_trains_found), Toast.LENGTH_LONG).show();
            trainList = new ArrayList<>();
        }

        adapter = new TrainAdapter(this, trainList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle back button press in toolbar
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
