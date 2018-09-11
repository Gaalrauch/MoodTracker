package fr.hironic.moodtracker.controller;

import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import fr.hironic.moodtracker.R;
import fr.hironic.moodtracker.model.MoodsHistory;
import fr.hironic.moodtracker.tools.GetTodayNumber;

import static fr.hironic.moodtracker.Constants.MOOD_COLORS;

/**
 * Created by Gaalrauch
 * Get moods history data, parse it do display previous days mood
 * User can see last seven registered moods with their comment
 *
 */

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        int currentDay = GetTodayNumber.getTodayNumber();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;

        String history = getIntent().getStringExtra("history");
        MoodsHistory moodsHistory = new MoodsHistory(history);
        JSONArray moods = moodsHistory.getMoods();

        for(int i = 0; i < 7; i++) {

            if (i < moods.length()) { // Parse data to update this view
                try {
                    JSONArray moodData = new JSONArray(moods.getString(i));
                    int dayNumber = moodData.getInt(0);
                    int mood = moodData.getInt(1);
                    String comment = moodData.getString(2);
                    int width = Math.round(screenWidth * 0.4f + screenWidth * 0.15f * mood);

                    updateView(i, width, mood, currentDay - dayNumber, comment);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else { // Hide this view
                updateView(i, 0, 0, 0, "");
            }
        }

    }

    /**
     * Update the mood history view, hide it if not used
     * @param id view number (from 0 to 6)
     * @param width width in pixel for this view
     * @param mood mood id (from 0 to 4) to select background color
     * @param days days passed since this mood was registered
     * @param comment comment of this mood, maybe be an empty String
     */
    private void updateView(int id, int width, int mood, int days, final String comment) {

        int layoutId = getResources().getIdentifier("llHistory" + id, "id", getPackageName());
        ConstraintLayout layout = findViewById(layoutId);

        if(width > 0) {
            layout.setBackgroundColor(getResources().getColor(MOOD_COLORS[mood]));
            layout.setMaxWidth(width);

            String text = daysToText(days);
            TextView textView = layout.findViewById(R.id.tvTime);
            textView.setText(text);

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
        } else {
            layout.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Return x days ago or special case like Yesterday, 1 week ago,..
     */
    private String daysToText(int days) {
        String text;
        switch (days) {
            case 1:
                text = getString(R.string.history_yesterday);
                break;
            case 2:
                text = getString(R.string.history_two_days_ago);
                break;
            case 7:
                text = getString(R.string.history_one_week_ago);
                break;
            default:
                text = getString(R.string.history_x_days_ago, days);
        }
        return text;
    }

}
