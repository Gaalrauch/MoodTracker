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

public class HistoryActivity extends AppCompatActivity {

    private SharedPreferences mSharedPreferences;
    public static final String PREF_KEY_SHARED_KEY = "MOOD_TRACKER";
    public static final String PREF_MOOD_HISTORY = "MOOD_HISTORY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mSharedPreferences = getSharedPreferences(PREF_KEY_SHARED_KEY, MODE_PRIVATE);
        String history = mSharedPreferences.getString(PREF_MOOD_HISTORY, "");

        System.out.println("HistoryActivity:: onCreate() history = " + history);
        MoodHistory mMoodHistory = new MoodHistory(history);
        JSONArray moods = mMoodHistory.getMoods();

        int[] moodColors = { R.color.faded_red,
                R.color.warm_grey,
                R.color.cornflower_blue_65,
                R.color.light_sage,
                R.color.banana_yellow };

        int currentDay = DateManager.getTodayNumber();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;

        for(int i = 0; i < 7; i++) {

            int layoutId = getResources().getIdentifier("llHistory" + i, "id", getPackageName());
            ConstraintLayout layout = findViewById(layoutId);

            if(i < moods.length()) {
                // Show this mood
                try {
                    JSONArray moodData = new JSONArray(moods.getString(i));
                    int moodID = moodData.getInt(1);
                    layout.setBackgroundColor(getResources().getColor(moodColors[moodID]));
                    layout.setVisibility(View.VISIBLE);

                    layout.setMaxWidth(Math.round(screenWidth * 0.4f + screenWidth * 0.15f * moodID));
                    int dayNumber = moodData.getInt(0);

                    int days = DateManager.GetDaysBetweenTwoDates(currentDay, dayNumber);
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
