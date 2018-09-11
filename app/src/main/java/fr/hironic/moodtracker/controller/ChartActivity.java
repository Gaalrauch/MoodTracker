package fr.hironic.moodtracker.controller;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.ArrayList;

import fr.hironic.moodtracker.R;
import fr.hironic.moodtracker.model.Mood;
import fr.hironic.moodtracker.model.MoodsHistory;
import fr.hironic.moodtracker.tools.ChartValueFormatter;

import static fr.hironic.moodtracker.Constants.MOOD_COLORS;

/**
 * Created by Gaalrauch
 * Get moods history data, parse data to count use of each mood
 * Display a pie chart using MPAndroidChart API
 *
 */

public class ChartActivity extends AppCompatActivity {

    private int[] mMoodsCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        String history = getIntent().getStringExtra("history");
        countMoods(history);

        generateChart();
    }

    /**
     * Count how many time each moods has been used
     */
    private void countMoods(String history) {

        MoodsHistory moodsHistory = new MoodsHistory(history);
        ArrayList<Mood> moods = moodsHistory.getMoods();

        mMoodsCount = new int[] { 0, 0, 0, 0, 0 };
        for(int i = 0; i < moods.size(); i++) {
            int type = moods.get(i).getType();
            mMoodsCount[type]++;
        }
    }

    /**
     * Use MPAndroidChart API to display the pie chart
     */
    private void generateChart() {

        PieChart pieChart = findViewById(R.id.piechart);

        ArrayList<Entry> yValues = new ArrayList<>();
        ArrayList<String> xValues = new ArrayList<>();

        for(int i= 0; i < 5; i++) {
            //if(mMoodsCount[i] > 0) {
                yValues.add(new Entry(mMoodsCount[i], i));
                xValues.add(""); // entry title, empty here, we hide titles
            //}
        }

        PieDataSet dataSet = new PieDataSet(yValues, "Humeurs");
        PieData data = new PieData(xValues, dataSet);

        // In percentage Term
        data.setValueFormatter(new ChartValueFormatter());
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
