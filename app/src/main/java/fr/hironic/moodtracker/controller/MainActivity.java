package fr.hironic.moodtracker.controller;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fr.hironic.moodtracker.R;
import fr.hironic.moodtracker.model.Mood;
import fr.hironic.moodtracker.model.MoodAdapter;
import fr.hironic.moodtracker.model.MoodHistory;

public class MainActivity extends AppCompatActivity {


    private SharedPreferences mPreferences;
    public static final String PREF_KEY_LAST_DATE = "LAST_DATE";
    public static final String PREF_KEY_TODAY_MOOD = "TODAY_MOOD";
    public static final String PREF_KEY_TODAY_COMMENT = "TODAY_COMMENT";
    private String mLastDate;
    private int mTodayMood;
    private String mTodayComment;

    public static final int DEFAULT_MOOD_VALUE = 3;

    private ListView mMoodsListView;
    private Boolean mAutoScrolling = false;
    public static int mScreenHeight = 1;

    private MoodHistory mMoodHistory;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPreferences = getPreferences(MODE_PRIVATE);
        mLastDate = mPreferences.getString(PREF_KEY_LAST_DATE, "Never");
        mTodayMood = mPreferences.getInt(PREF_KEY_TODAY_MOOD, DEFAULT_MOOD_VALUE);
        mTodayComment = mPreferences.getString(PREF_KEY_TODAY_COMMENT, "");

        mMoodHistory = new MoodHistory(this);

        mMoodsListView = findViewById(R.id.lvMoods);
        List<Mood> moods = generateMoods();

        MoodAdapter adapter = new MoodAdapter(MainActivity.this, moods);
        mMoodsListView.setAdapter(adapter);

        mMoodsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(final AbsListView view, final int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {

                    int scrollingTo = mMoodsListView.getFirstVisiblePosition();

                    // in case user slowly drags smileys, stop and release finger, there will be no auto scrolling
                    // we can check scrollY to catche this case, then we skip the next condition if
                    // scrollY is not under 5 pixels
                    View firstChildView = mMoodsListView.getChildAt(0);
                    int scrollY = -firstChildView.getTop();

                    if(mAutoScrolling && scrollY < 5) {
                        mTodayMood = scrollingTo;
                        mPreferences.edit().putInt(PREF_KEY_TODAY_MOOD, mTodayMood).apply();

                        mMoodHistory.SaveMood("09/09/1983", mTodayMood, "Hello de LU");
                        mAutoScrolling = false;
                        return;
                    }


                    mAutoScrolling = true;
                    if(scrollY > mScreenHeight / 2) {
                        scrollingTo++;
                    }

                    mMoodsListView.smoothScrollToPositionFromTop(scrollingTo, 0, 500);

                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenHeight = metrics.heightPixels;

        CheckForNewDay();

        mMoodsListView.setSelection(mTodayMood);


        Button button = findViewById(R.id.btnHistory);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //
                // Start history activity
                // ...
                //

            }
        });

    }


    private List<Mood> generateMoods() {

        List<Mood> moods = new ArrayList<>();
        moods.add(new Mood(getResources().getColor(R.color.faded_red), getResources().getDrawable(R.drawable.smiley_sad)));
        moods.add(new Mood(getResources().getColor(R.color.warm_grey), getResources().getDrawable(R.drawable.smiley_disappointed)));
        moods.add(new Mood(getResources().getColor(R.color.cornflower_blue_65), getResources().getDrawable(R.drawable.smiley_normal)));
        moods.add(new Mood(getResources().getColor(R.color.light_sage), getResources().getDrawable(R.drawable.smiley_happy)));
        moods.add(new Mood(getResources().getColor(R.color.banana_yellow), getResources().getDrawable(R.drawable.smiley_super_happy)));
        return moods;
    }

    private void CheckForNewDay() {

        Calendar calendar = Calendar.getInstance();
        String currentDate = calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH);

        if(!currentDate.equals(mLastDate)) {

            if(mLastDate != "Never") {

                //
                // Here we save previous mood with/without comment
                //
                mMoodHistory.SaveMood(mLastDate, mTodayMood, mTodayComment);

            }

            mLastDate = currentDate;
            mTodayMood = DEFAULT_MOOD_VALUE;
            mTodayComment = "";

            mPreferences.edit()
                    .putString(PREF_KEY_LAST_DATE, mLastDate)
                    .putInt(PREF_KEY_TODAY_MOOD, mTodayMood)
                    .putString(PREF_KEY_TODAY_COMMENT, mTodayComment)
                    .apply();
        }
    }
}
