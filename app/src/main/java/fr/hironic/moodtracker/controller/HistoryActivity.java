package fr.hironic.moodtracker.controller;

import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import fr.hironic.moodtracker.R;
import fr.hironic.moodtracker.model.MoodHistory;
import fr.hironic.moodtracker.tools.DateManager;

import static fr.hironic.moodtracker.Constants.MOOD_COLORS;
import static fr.hironic.moodtracker.Constants.PREF_KEY_SHARED_KEY;
import static fr.hironic.moodtracker.Constants.PREF_MOOD_HISTORY;

public class HistoryActivity extends AppCompatActivity {

    JSONArray mMoods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        SharedPreferences preferences = getSharedPreferences(PREF_KEY_SHARED_KEY, MODE_PRIVATE);
        String history = preferences.getString(PREF_MOOD_HISTORY, "");

        MoodHistory mMoodHistory = new MoodHistory(history);
        mMoods = mMoodHistory.getMoods();

        updateViews();
    }

    /**
     * Update moods view to display background color, days and eventually add a comment button
     */
    private void updateViews() {

        int currentDay = DateManager.getTodayNumber();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;

        for(int i = 0; i < 7; i++) {

            int layoutId = getResources().getIdentifier("llHistory" + i, "id", getPackageName());
            ConstraintLayout layout = findViewById(layoutId);

            if(i < mMoods.length()) {
                // Show this mood
                try {
                    JSONArray moodData = new JSONArray(mMoods.getString(i));
                    int moodID = moodData.getInt(1);
                    layout.setBackgroundColor(getResources().getColor(MOOD_COLORS[moodID]));
                    layout.setVisibility(View.VISIBLE);

                    layout.setMaxWidth(Math.round(screenWidth * 0.4f + screenWidth * 0.15f * moodID));
                    int dayNumber = moodData.getInt(0);

                    int days = currentDay - dayNumber;
                    String text;
                    switch (days) {
                        case 1:
                            text = "Hier";
                            break;
                        case 2:
                            text = "Avant-hier";
                            break;
                        case 7:
                            text = "Il y a une semaine";
                            break;
                        default:
                            text = "Il y a " + days + " jours";
                    }

                    TextView textView = layout.findViewById(R.id.tvTime);
                    textView.setText(text);

                    final String comment = moodData.getString(2);
                    if(!comment.equals("")) {
                        Button button = layout.findViewById(R.id.btnComment);
                        button.setVisibility(View.VISIBLE);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getApplicationContext(), comment, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
