package fr.hironic.moodtracker.controller;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import fr.hironic.moodtracker.R;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);


        ConstraintLayout layout = findViewById(R.id.llHistory0);
        layout.setBackgroundColor(getResources().getColor(R.color.banana_yellow));
    }
}
