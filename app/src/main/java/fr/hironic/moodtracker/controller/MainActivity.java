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
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import fr.hironic.moodtracker.R;
import fr.hironic.moodtracker.model.Mood;
import fr.hironic.moodtracker.model.MoodsHistory;
import fr.hironic.moodtracker.tools.DayNumber;

import static fr.hironic.moodtracker.Constants.DEFAULT_MOOD_VALUE;
import static fr.hironic.moodtracker.Constants.MOOD_COLORS;
import static fr.hironic.moodtracker.Constants.MOOD_DRAWABLES;
import static fr.hironic.moodtracker.Constants.PREF_KEY_MOOD_TODAY;
import static fr.hironic.moodtracker.Constants.PREF_KEY_MOOD_HISTORY;

/**
 * Created by Gaalrauch
 * Display a smiley representing user's mood
 * Allows user to :
 *  - fling the current smiley to select another mood
 *  - add a comment
 * Moods and their comment are saved on a new day
 *
 */

public class MainActivity extends AppCompatActivity implements
        View.OnTouchListener,
        GestureDetector.OnGestureListener {

    private SharedPreferences mPreferences; // Contains mTodayNumber, mTodayMood, mTodayComment and mHistory

    private Mood mTodayMood; // A Mood to store today data (current day number, mood type and comment)
    private String mHistory; // Moods history (list of Mood)

    private ConstraintLayout mMainLayout; // Used to update background color
    private ImageView mSmiley; // Used to update smiley picture
    private GestureDetectorCompat mGestureDetector; // Used to detect fling

    private MoodsHistory mMoodsHistory; // List of Moods (day numbers, types and comments)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPreferences = getPreferences(MODE_PRIVATE);

        String moodData = mPreferences.getString(PREF_KEY_MOOD_TODAY, "[0,3,\"\"]");
        mTodayMood = new Mood(moodData);

        mHistory = mPreferences.getString(PREF_KEY_MOOD_HISTORY, "");
        mMoodsHistory = new MoodsHistory(mHistory);

        // If there is something saved, show chart and history buttons
        if(!mHistory.equals("")) {
            findViewById(R.id.btnChart).setVisibility(View.VISIBLE);
            findViewById(R.id.btnHistory).setVisibility(View.VISIBLE);
        }

        mMainLayout = findViewById(R.id.mainLayout);
        mSmiley = findViewById(R.id.imgSmiley);
        mGestureDetector = new GestureDetectorCompat(this, this);

        selectMood(mTodayMood.getType());

        findViewById(R.id.btnComment).setOnTouchListener(this);
        findViewById(R.id.btnHistory).setOnTouchListener(this);
        findViewById(R.id.btnChart).setOnTouchListener(this);
        findViewById(R.id.fling).setOnTouchListener(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        checkForNewDay();
    }

    @Override
    public void onResume() {
        super.onResume();
        checkForNewDay();
    }

    /**
     * Get the number of days since January 1st 1970
     * Compares it to the current value of mTodayNumber
     * If the value is not the same we know the day has changed
     * If mTodayNumber was not zero we add current values (day number, mood type, comment) to mHistory
     */
    private void checkForNewDay() {

        int currentDayNumber = DayNumber.getTodayNumber();
        if(currentDayNumber != mTodayMood.getDayNumber()) {

            if(mTodayMood.getDayNumber() > 0) { // There was something to save, then save it
                mMoodsHistory.addMoodToHistory(mTodayMood);
                mHistory = mMoodsHistory.getHistory();
                // In case there was no history before, show chart and history buttons
                findViewById(R.id.btnChart).setVisibility(View.VISIBLE);
                findViewById(R.id.btnHistory).setVisibility(View.VISIBLE);
            }

            mTodayMood = new Mood(currentDayNumber, DEFAULT_MOOD_VALUE, "");

            mPreferences.edit()
                    .putString(PREF_KEY_MOOD_TODAY, mTodayMood.toString())
                    .putString(PREF_KEY_MOOD_HISTORY, mHistory)
                    .apply();
        }
    }

    /**
     * Update the background color and mood picture depending on mood value
     * @param type mood type from 0 (sad) to 4 (super happy)
     */
    private void displayMood(int type) {
        mMainLayout.setBackgroundColor(getResources().getColor(MOOD_COLORS[type]));
        mSmiley.setImageDrawable(getResources().getDrawable(MOOD_DRAWABLES[type]));
    }

    /**
     * Check for new day before to save if necessary the current mood values (day number, mood, comment)
     * Update current mood value
     * Reset comment to an empty String if necessary
     * Update mPreferences
     * Call displayMood() to display visual changes
     * @param type mood type from 0 (sad) to 4 (super happy)
     */
    private void selectMood(int type) {
        checkForNewDay();
        // Remove current comment if mood type has changed
        if(type != mTodayMood.getType()) {
            mTodayMood.setType(type);
            mTodayMood.setComment("");
        }
        mPreferences.edit().putString(PREF_KEY_MOOD_TODAY, mTodayMood.toString()).apply();
        displayMood(type);
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
        etUserInput.setText(mTodayMood.getComment());

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(mView);

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(getString(R.string.mood_comment_btn_validate), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        updateComment(etUserInput.getText().toString().trim());
                    }
                })

                .setNegativeButton(getString(R.string.mood_comment_btn_cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        if(alertDialogAndroid.getWindow() != null)
            alertDialogAndroid.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        alertDialogAndroid.show();
    }

    private void updateComment(String comment) {
        if(!comment.equals(mTodayMood.getComment())) {
            mTodayMood.setComment(comment);
            mPreferences.edit().putString(PREF_KEY_MOOD_TODAY, mTodayMood.toString()).apply();
            String message;
            if(comment.equals("")) {
                message = getString(R.string.mood_comment_removed);
            } else {
                message = getString(R.string.mood_comment_registered);
            }
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
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
        if(Math.abs(motionEvent.getX() - motionEvent1.getX()) > Math.abs(motionEvent.getY() - motionEvent1.getY())) { // User flings horizontally
            return false;
        }
        int type = mTodayMood.getType();
        if(motionEvent.getY() > motionEvent1.getY()) { // Fling in top direction, increase mood type
            if(type > 3) return false;
            selectMood(type + 1);
        } else { // Fling down, decrease mood type
            if(type == 0) return false;
            selectMood(type - 1);
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
            Intent intent = new Intent(MainActivity.this, ChartActivity.class);
            intent.putExtra("history", mHistory);
            startActivity(intent);
            return true;
        }

        if(view.getId() == R.id.btnHistory) {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            intent.putExtra("history", mHistory);
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
