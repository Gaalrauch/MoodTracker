package fr.hironic.moodtracker.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;

import fr.hironic.moodtracker.R;

public class ChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);


        PieChart pieChart = (PieChart) findViewById(R.id.piechart);
        pieChart.setUsePercentValues(true);

        ArrayList<Entry> yvalues = new ArrayList<Entry>();
        yvalues.add(new Entry(8f, 0));
        yvalues.add(new Entry(15f, 1));
        yvalues.add(new Entry(12f, 2));
        yvalues.add(new Entry(25f, 3));
        yvalues.add(new Entry(23f, 4));
        PieDataSet dataSet = new PieDataSet(yvalues, "Election Results");

        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("Triste");
        xVals.add("Mécontent");
        xVals.add("Neutre");
        xVals.add("Heureux");
        xVals.add("Aux anges");
        PieData data = new PieData(xVals, dataSet);

        // In percentage Term
        data.setValueFormatter(new PercentFormatter());
        // Default value
        //data.setValueFormatter(new DefaultValueFormatter(0));

        dataSet.setColors(new int[]{ R.color.faded_red,
                R.color.warm_grey,
                R.color.cornflower_blue_65,
                R.color.light_sage,
                R.color.banana_yellow }, getApplicationContext());

        pieChart.setData(data);

        // ToDo : load real moods data
        // ToDo : Change text size
        // ToDo : Remove/change description and title
        // ToDo : Replace default library legend by smileys ?
        // ToDo : Create icon for chart button on main activity layout
    }
}
