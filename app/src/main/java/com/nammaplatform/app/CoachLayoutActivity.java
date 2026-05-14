package com.nammaplatform.app;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Locale;

/**
 * CoachLayoutActivity — Displays the visual coach arrangement for a train
 * and provides Kannada Text-to-Speech announcements.
 */
public class CoachLayoutActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    // UI references
    private LinearLayout coachContainer;
    private TextView tvGeneralHint;
    private Button btnSpeak;

    // Data from intent
    private String trainName;
    private String departure;
    private int platform;
    private ArrayList<String> coaches;

    // TTS engine
    private TextToSpeech textToSpeech;
    private boolean ttsReady = false;

    // Dynamically computed position (1-indexed) of first General coach
    private int generalCoachPosition = -1;

    // Coach dimensions in dp
    private static final int COACH_WIDTH_DP = 70;
    private static final int COACH_HEIGHT_DP = 90;
    private static final int COACH_MARGIN_DP = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_layout);

        // Retrieve data passed from TrainAdapter / TrainListActivity
        trainName = getIntent().getStringExtra("trainName");
        departure = getIntent().getStringExtra("departure");
        platform = getIntent().getIntExtra("platform", 1);
        coaches = getIntent().getStringArrayListExtra("coaches");

        if (trainName == null) trainName = "Train";
        if (departure == null) departure = "--:--";
        if (coaches == null) coaches = new ArrayList<>();

        // Set up Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(trainName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Platform info card
        TextView tvPlatformInfo = findViewById(R.id.tv_platform_info);
        tvPlatformInfo.setText(getString(R.string.platform_info_prefix) + platform);

        // Coach layout container
        coachContainer = findViewById(R.id.coach_container);

        // General coach hint box
        tvGeneralHint = findViewById(R.id.tv_general_coach_hint);

        // Speak button
        btnSpeak = findViewById(R.id.btn_speak_kannada);
        btnSpeak.setOnClickListener(v -> speakKannada());

        // Build the coach layout views
        buildCoachLayout();

        // Initialise TTS with Kannada locale
        textToSpeech = new TextToSpeech(this, this);
    }

    /**
     * Dynamically creates a coloured box for each coach and adds it to the horizontal layout.
     */
    private void buildCoachLayout() {
        coachContainer.removeAllViews();
        generalCoachPosition = -1;

        if (coaches == null || coaches.isEmpty()) {
            return;
        }

        int widthPx = dpToPx(COACH_WIDTH_DP);
        int heightPx = dpToPx(COACH_HEIGHT_DP);
        int marginPx = dpToPx(COACH_MARGIN_DP);

        for (int i = 0; i < coaches.size(); i++) {
            String coachType = coaches.get(i);

            // Track the first General coach position (1-indexed)
            if ("GEN".equals(coachType) && generalCoachPosition == -1) {
                generalCoachPosition = i + 1;
            }

            // Create the coach TextView
            TextView coachView = new TextView(this);
            coachView.setText(coachType);
            coachView.setTextColor(Color.WHITE);
            coachView.setTextSize(12f);
            coachView.setTypeface(null, android.graphics.Typeface.BOLD);
            coachView.setGravity(Gravity.CENTER);

            // Layout params
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthPx, heightPx);
            params.setMargins(marginPx, marginPx, marginPx, marginPx);
            coachView.setLayoutParams(params);

            // Rounded background with coach-specific color
            GradientDrawable background = new GradientDrawable();
            background.setShape(GradientDrawable.RECTANGLE);
            background.setCornerRadius(dpToPx(8));
            background.setColor(getCoachColor(coachType));
            background.setStroke(dpToPx(1), Color.WHITE);
            coachView.setBackground(background);

            // Coach number label below
            coachView.setText(coachType + "\n" + (i + 1));

            coachContainer.addView(coachView);
        }

        // Update the General Coach hint
        if (generalCoachPosition > 0) {
            tvGeneralHint.setText(
                    getString(R.string.general_coach_hint_prefix) + generalCoachPosition
            );
            tvGeneralHint.setVisibility(View.VISIBLE);
        } else {
            tvGeneralHint.setVisibility(View.GONE);
        }
    }

    /**
     * Returns the background color integer for a given coach type string.
     */
    private int getCoachColor(String coachType) {
        switch (coachType) {
            case "ENG": return Color.parseColor("#607D8B");
            case "L":   return Color.parseColor("#E91E63");
            case "SL":  return Color.parseColor("#2E7D32");
            case "AC":  return Color.parseColor("#6A1B9A");
            case "GEN":
            default:    return Color.parseColor("#1565C0");
        }
    }

    /**
     * Constructs the Kannada announcement string and speaks it via TTS.
     */
    private void speakKannada() {
        if (!ttsReady) {
            Toast.makeText(this, getString(R.string.tts_not_supported), Toast.LENGTH_SHORT).show();
            return;
        }

        int genPos = generalCoachPosition > 0 ? generalCoachPosition : 1;
        String announcement = getString(R.string.tts_announcement, trainName, platform, genPos);

        // Speak using TextToSpeech engine
        textToSpeech.speak(announcement, TextToSpeech.QUEUE_FLUSH, null, "NP_ANNOUNCEMENT");
    }

    /**
     * TextToSpeech.OnInitListener callback.
     * Sets the language to Kannada (kn_IN) if available.
     */
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(new Locale("kn", "IN"));
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                ttsReady = false;
                Toast.makeText(this, getString(R.string.tts_not_supported), Toast.LENGTH_SHORT).show();
            } else {
                ttsReady = true;
            }
        } else {
            ttsReady = false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Shutdown TTS engine to prevent memory leaks.
     */
    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
            textToSpeech = null;
        }
        super.onDestroy();
    }

    /**
     * Converts dp units to pixels for the current display density.
     */
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}
