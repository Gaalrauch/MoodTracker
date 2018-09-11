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
import fr.hironic.moodtracker.model.MoodsHistory;
import fr.hironic.moodtracker.tools.DateManager;

import static fr.hironic.moodtracker.Constants.DEFAULT_MOOD_VALUE;
import static fr.hironic.moodtracker.Constants.PREF_KEY_TODAY_NUMBER;
import static fr.hironic.moodtracker.Constants.PREF_KEY_TODAY_COMMENT;
import static fr.hironic.moodtracker.Constants.PREF_KEY_TODAY_MOOD;
import static fr.hironic.moodtracker.Constants.PREF_MOOD_HISTORY;

/**
 * Created by Gaalrauch
 * Display a smiley representing your mood
 * Allows you to :
 *  - fling the current smiley to select another mood
 *  - add a comment
 * Moods and their comment are saved on a new day
 *
 */

public class MainActivity extends AppCompatActivity implements
        View.OnTouchListener,
        GestureDetector.OnGestureListener {

    private SharedPreferences mPreferences; // Contains mTodayNumber, mTodayMood, mTodayComment and mHistory

    private int mTodayNumber; // Number of days since January 1st 1970
    private int mTodayMood; // Current mood value for today from 0 (sad) to 4 (super happy)
    private String mTodayComment; // Current comment for today mood
    private String mHistory; // Moods history

    private ConstraintLayout mMainLayout; // Used to update background color
    private ImageView mSmiley; // Used to update smiley picture
    private GestureDetectorCompat mGestureDetector; // Used to detect fling

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPreferences = getPreferences(MODE_PRIVATE);
        mTodayNumber = mPreferences.getInt(PREF_KEY_TODAY_NUMBER, 0);
        mTodayMood = mPreferences.getInt(PREF_KEY_TODAY_MOOD, DEFAULT_MOOD_VALUE);
        mTodayComment = mPreferences.getString(PREF_KEY_TODAY_COMMENT, "");
        mHistory = mPreferences.getString(PREF_MOOD_HISTORY, "");

        MoodsHistory.setMoods(mHistory);
        // If there is something saved, show chart and history buttons
        if(!mHistory.equals("")) {
            findViewById(R.id.btnChart).setVisibility(View.VISIBLE);
            findViewById(R.id.btnHistory).setVisibility(View.VISIBLE);
        }

        System.out.println("MainActivity:: onCreate() history = " + mHistory);

        mMainLayout = findViewById(R.id.mainLayout);
        mSmiley = findViewById(R.id.imgSmiley);

        mGestureDetector = new GestureDetectorCompat(this, this);

        checkForNewDay();

        selectMood(mTodayMood);

        findViewById(R.id.btnComment).setOnTouchListener(this);
        findViewById(R.id.btnHistory).setOnTouchListener(this);
        findViewById(R.id.btnChart).setOnTouchListener(this);
        findViewById(R.id.fling).setOnTouchListener(this);

    }

    /**
     * Get the number of days since January 1st 1970
     * Compares it to the current value of mTodayNumber
     * If the value is not the same we know the day has changed
     * If mTodayNumber was not zero we add current values (day number, mood value, comment) to mHistory
     */
    private void checkForNewDay() {

        int currentDayNumber = DateManager.getTodayNumber();
        if(currentDayNumber != mTodayNumber) {

            if(mTodayNumber > 0) { // There was something to save, then save it
                mHistory = MoodsHistory.addMoodToHistory(mTodayNumber, mTodayMood, mTodayComment);
                // In case there was no history before, show chart and history buttons
                findViewById(R.id.btnChart).setVisibility(View.VISIBLE);
                findViewById(R.id.btnHistory).setVisibility(View.VISIBLE);
            }

            mTodayNumber = currentDayNumber;
            mTodayMood = DEFAULT_MOOD_VALUE;
            mTodayComment = "";

            mPreferences.edit()
                    .putInt(PREF_KEY_TODAY_NUMBER, mTodayNumber)
                    .putInt(PREF_KEY_TODAY_MOOD, mTodayMood)
                    .putString(PREF_KEY_TODAY_COMMENT, mTodayComment)
                    .putString(PREF_MOOD_HISTORY, mHistory)
                    .apply();
        }
    }

    /**
     * Update the background color and mood picture depending on mood value
     * @param mood mood value from 0 (sad) to 4 (super happy)
     */
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

    /**
     * Check for new day before to save if necessary the current mood values (day number, mood, comment)
     * Update current mood value
     * Reset comment to an empty String if necessary
     * Update mPreferences
     * Call displayMood() to display visual changes
     * @param mood mood value from 0 (sad) to 4 (super happy)
     */
    private void selectMood(int mood) {
        checkForNewDay();
        // Remove current comment if we change mood
        if(mood != mTodayMood) {
            mTodayComment = "";
            mTodayMood = mood;
        }
        mPreferences.edit().putInt(PREF_KEY_TODAY_MOOD, mTodayMood).apply();
        displayMood(mood);
    }

    /**
     * Open a dialog box to allow user to enter / edit a comment about his mood
     * Commment is saved in mPreferences
     */
    private void openCommentDialogBox() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(MainActivity.this);
        final ViewGroup nullParent = null;
        View mView = layoutInflaterAndroid.inflate(R.layout.view_comment_dialogbox, nullParent);

        // Set text to today comment
        final EditText etUserInput = mView.findViewById(R.id.etComment);
        etUserInput.setText(mTodayComment);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(mView);

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(getString(R.string.mood_comment_btn_validate), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        // Save comment for today
                        mTodayComment = etUserInput.getText().toString();
                        // Update SharedPreferences
                        mPreferences.edit().putString(PREF_KEY_TODAY_COMMENT, mTodayComment).apply();
                        Toast.makeText(getApplicationContext(), getString(R.string.mood_comment_registered), Toast.LENGTH_SHORT).show();
                    }
                })

                .setNegativeButton(getString(R.string.mood_comment_btn_cancel),
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

        if(Math.abs(velX) > Math.abs(velY)) { // User fling horizontally, we don't care
            return false;
        }
        if(velY < 0) { // Fling in top direction, increase mood value
            if(mTodayMood > 3) return false;
            selectMood(mTodayMood + 1);
        } else { // Fling down, decrease mood value
            if(mTodayMood == 0) return false;
            selectMood(mTodayMood - 1);
        }
        return true;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        int action = motionEvent.getAction();


        if(view.getId() == R.id.fling) {
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

            openCommentDialogBox();
            return true;
        }

        return false;
    }
}
