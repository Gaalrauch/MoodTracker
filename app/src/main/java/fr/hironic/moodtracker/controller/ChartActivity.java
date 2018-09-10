package fr.hironic.moodtracker.controller;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;

import org.json.JSONArray;

import java.util.ArrayList;

import fr.hironic.moodtracker.R;
import fr.hironic.moodtracker.model.MoodHistory;

import static fr.hironic.moodtracker.Constants.MOOD_COLORS;
import static fr.hironic.moodtracker.Constants.PREF_KEY_SHARED_KEY;
import static fr.hironic.moodtracker.Constants.PREF_MOOD_HISTORY;

public class ChartActivity extends AppCompatActivity {

    private int[] mMoodsCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        mMoodsCount = new int[] { 0, 0, 0, 0, 0 };

        SharedPreferences preferences = getSharedPreferences(PREF_KEY_SHARED_KEY, MODE_PRIVATE);
        String history = preferences.getString(PREF_MOOD_HISTORY, "");
        countMoods(history);

        generateChart();
    }

    /**
     * Count how many time each moods has been used
     * @param history
     */
    private void countMoods(String history) {
        MoodHistory moodHistory = new MoodHistory(history);
        JSONArray moods = moodHistory.getMoods();
        for(int i = 0; i < moods.length(); i++) {
            try {
                JSONArray moodData = new JSONArray(moods.getString(i));
                int moodID = moodData.getInt(1);
                mMoodsCount[moodID]++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Use MPAndroid API to display a pie chart
     */
    private void generateChart() {

        PieChart pieChart = findViewById(R.id.piechart);

        ArrayList<Entry> yValues = new ArrayList<>();
        ArrayList<String> xValues = new ArrayList<>();

        String[] titles = new String[]{ "Triste", "Mécontent", "Neutre", "Heureux", "Aux anges" };
        for(int i= 0; i < 5; i++) {
            if(mMoodsCount[i] > 0) {
                yValues.add(new Entry(mMoodsCount[i], i));
                xValues.add(titles[i]);
            }
        }

        PieDataSet dataSet = new PieDataSet(yValues, "Humeurs");
        PieData data = new PieData(xValues, dataSet);

        // In percentage Term
        data.setValueFormatter(new PercentFormatter());
        // Default value
        //data.setValueFormatter(new DefaultValueFormatter(0));
        pieChart.setUsePercentValues(true);

        pieChart.getLegend().setEnabled(false); // Hide legend, we use custom one in activity layout
        pieChart.setDrawSliceText(false); // Hide parts name
        pieChart.setDescription(""); // Hide description
        // Define parts colors
        dataSet.setColors(MOOD_COLORS, getApplicationContext());
        // Define parts text size and text color
        data.setValueTextSize(17f);
        data.setValueTextColor(Color.BLACK);
        // Define hole
        pieChart.setDrawHoleEnabled(false);
        //pieChart.setTransparentCircleRadius(22f);
        //pieChart.setHoleRadius(20f);
        // Set chart data
        pieChart.setData(data);

    }
}
