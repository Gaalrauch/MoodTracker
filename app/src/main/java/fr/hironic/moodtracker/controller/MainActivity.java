package fr.hironic.moodtracker.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import fr.hironic.moodtracker.R;
import fr.hironic.moodtracker.model.MoodHistory;
import fr.hironic.moodtracker.tools.DateManager;

public class MainActivity extends AppCompatActivity implements
        View.OnTouchListener,
        GestureDetector.OnGestureListener {

    private SharedPreferences mSharedPreferences;
    public static final String PREF_KEY_SHARED_KEY = "MOOD_TRACKER";
    public static final String PREF_KEY_LAST_DATE = "LAST_DATE";
    public static final String PREF_KEY_TODAY_MOOD = "TODAY_MOOD";
    public static final String PREF_KEY_TODAY_COMMENT = "TODAY_COMMENT";
    public static final String PREF_MOOD_HISTORY = "MOOD_HISTORY";

    private int mTodayNumber;
    private int mTodayMood;
    private String mTodayComment;
    private String mHistory;

    public static final int DEFAULT_MOOD_VALUE = 3;

    private MoodHistory mMoodHistory;

    private ConstraintLayout mMainLayout;
    private ImageView mSmiley;
    private GestureDetectorCompat mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSharedPreferences = getSharedPreferences(PREF_KEY_SHARED_KEY, MODE_PRIVATE);
        mTodayNumber = mSharedPreferences.getInt(PREF_KEY_LAST_DATE, 0);
        mTodayMood = mSharedPreferences.getInt(PREF_KEY_TODAY_MOOD, DEFAULT_MOOD_VALUE);
        mTodayComment = mSharedPreferences.getString(PREF_KEY_TODAY_COMMENT, "");
        mHistory = mSharedPreferences.getString(PREF_MOOD_HISTORY, "");
        mMoodHistory = new MoodHistory(mHistory);
        // If there is something saved, show chart and history buttons
        if(!mHistory.equals("")) {
            findViewById(R.id.btnChart).setVisibility(View.VISIBLE);
            findViewById(R.id.btnHistory).setVisibility(View.VISIBLE);
        }

        System.out.println("MainActivity:: onCreate() history = " + mHistory);

        mMainLayout = findViewById(R.id.mainLayout);
        mSmiley = findViewById(R.id.imgSmiley);
        mSmiley.setOnTouchListener(this);

        mGestureDetector = new GestureDetectorCompat(this, this);

        checkForNewDay();

        selectMood(mTodayMood);

        DateManager.getTodayNumber();

        findViewById(R.id.btnComment).setOnTouchListener(this);
        findViewById(R.id.btnHistory).setOnTouchListener(this);
        findViewById(R.id.btnChart).setOnTouchListener(this);

    }

    private void checkForNewDay() {

        int currentDayNumber = DateManager.getTodayNumber();
        if(currentDayNumber != mTodayNumber) {

            if(mTodayNumber > 0) { // There was something to save, then save it
                mHistory = mMoodHistory.saveMood(mTodayNumber, mTodayMood, mTodayComment);
                // In case there was no history before, show chart and history buttons
                findViewById(R.id.btnChart).setVisibility(View.VISIBLE);
                findViewById(R.id.btnHistory).setVisibility(View.VISIBLE);
            }

            mTodayNumber = currentDayNumber;
            mTodayMood = DEFAULT_MOOD_VALUE;
            mTodayComment = "";

            mSharedPreferences.edit()
                    .putInt(PREF_KEY_LAST_DATE, mTodayNumber)
                    .putInt(PREF_KEY_TODAY_MOOD, mTodayMood)
                    .putString(PREF_KEY_TODAY_COMMENT, mTodayComment)
                    .putString(PREF_MOOD_HISTORY, mHistory)
                    .apply();
        }
    }

    private void displayMood(int mood) {
        switch (mood) {
            case 0:
                mMainLayout.setBackgroundColor(getResources().getColor(R.color.faded_red));
                mSmiley.setImageDrawable(getResources().getDrawable(R.drawable.smiley_sad));
                break;
            case 1:
                mMainLayout.setBackgroundColor(getResources().getColor(R.color.warm_grey));
                mSmiley.setImageDrawable(getResources().getDrawable(R.drawable.smiley_disappointed));
                break;
            case 2:
                mMainLayout.setBackgroundColor(getResources().getColor(R.color.cornflower_blue_65));
                mSmiley.setImageDrawable(getResources().getDrawable(R.drawable.smiley_normal));
                break;
            case 3:
                mMainLayout.setBackgroundColor(getResources().getColor(R.color.light_sage));
                mSmiley.setImageDrawable(getResources().getDrawable(R.drawable.smiley_happy));
                break;
            case 4:
                mMainLayout.setBackgroundColor(getResources().getColor(R.color.banana_yellow));
                mSmiley.setImageDrawable(getResources().getDrawable(R.drawable.smiley_super_happy));
        }
    }

    private void selectMood(int mood) {
        checkForNewDay();
        // Remove current comment if we change mood
        if(mood != mTodayMood) {
            mTodayComment = "";
        }
        mTodayMood = mood;
        mSharedPreferences.edit().putInt(PREF_KEY_TODAY_MOOD, mTodayMood).apply();
        displayMood(mood);
    }

    private void openCommentForm() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(MainActivity.this);
        final ViewGroup nullParent = null;
        View mView = layoutInflaterAndroid.inflate(R.layout.add_comment_dialog_box, nullParent);

        // Set text to today comment
        final EditText etUserInput = mView.findViewById(R.id.etComment);
        etUserInput.setText(mTodayComment);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(mView);

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("VALIDER", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        // Save comment for today
                        mTodayComment = etUserInput.getText().toString();
                        // Update SharedPreferences
                        mSharedPreferences.edit().putString(PREF_KEY_TODAY_COMMENT, mTodayComment).apply();
                        Toast.makeText(getApplicationContext(), "Votre commentaire a bien été enregistré.", Toast.LENGTH_SHORT).show();
                    }
                })

                .setNegativeButton("ANNULER",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float velX, float velY) {

        if(velY < 0) {
            if(mTodayMood > 3) return false;
            selectMood(mTodayMood + 1);
        } else {
            if(mTodayMood == 0) return false;
            selectMood(mTodayMood - 1);
        }
        return true;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        int action = motionEvent.getAction();


        if(view.getId() == R.id.imgSmiley) {
            mGestureDetector.onTouchEvent(motionEvent);
            return true;
        }

        if(action != MotionEvent.ACTION_UP) {
            return false;
        }

        view.performClick();

        if(view.getId() == R.id.btnChart) {

            checkForNewDay();
            Intent intent = new Intent(MainActivity.this, ChartActivity.class);
            startActivity(intent);
            return true;
        }

        if(view.getId() == R.id.btnHistory) {

            checkForNewDay();
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
            return true;
        }

        if(view.getId() == R.id.btnComment) {

            openCommentForm();
            return true;
        }

        return false;
    }
}
