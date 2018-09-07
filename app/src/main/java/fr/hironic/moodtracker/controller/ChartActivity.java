package fr.hironic.moodtracker.controller;

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

public class ChartActivity extends AppCompatActivity {

    // ToDo : Create icon for chart button on main activity layout
    // ToDo : Add legend in activity layout

    private int[] mMoodsCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        mMoodsCount = new int[] { 0, 0, 0, 0, 0 };
        CountMoods();
        GenerateChart();
    }

    private void CountMoods() {
        MoodHistory moodHistory = new MoodHistory(this);
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

    private void GenerateChart() {

        PieChart pieChart = findViewById(R.id.piechart);

        ArrayList<Entry> yVals = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();

        String[] titles = new String[]{ "Triste", "Mécontent", "Neutre", "Heureux", "Aux anges" };
        for(int i= 0; i < 5; i++) {
            if(mMoodsCount[i] > 0) {
                yVals.add(new Entry(mMoodsCount[i], i));
                xVals.add(titles[i]);
            }
        }

        PieDataSet dataSet = new PieDataSet(yVals, "Humeurs");
        PieData data = new PieData(xVals, dataSet);

        // In percentage Term
        data.setValueFormatter(new PercentFormatter());
        // Default value
        //data.setValueFormatter(new DefaultValueFormatter(0));
        pieChart.setUsePercentValues(true);

        pieChart.getLegend().setEnabled(false); // Hide legend, we use custom one in activity layout
        pieChart.setDrawSliceText(false); // Hide parts name
        pieChart.setDescription(""); // Hide description
        // Define parts colors
        dataSet.setColors(new int[]{ R.color.faded_red,
                R.color.warm_grey,
                R.color.cornflower_blue_65,
                R.color.light_sage,
                R.color.banana_yellow }, getApplicationContext());
        // Define parts text size and text color
        data.setValueTextSize(17f);
        data.setValueTextColor(Color.BLACK);
        // Define hole
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(22f);
        pieChart.setHoleRadius(20f);
        // Set chart data
        pieChart.setData(data);

    }
}
