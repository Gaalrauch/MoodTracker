package fr.hironic.moodtracker.controller;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fr.hironic.moodtracker.R;
import fr.hironic.moodtracker.model.Mood;
import fr.hironic.moodtracker.model.MoodAdapter;

public class MainActivity extends AppCompatActivity {


    private SharedPreferences mPreferences;
    public static final String PREF_KEY_LASTYEAR = "LAST_YEAR";
    public static final String PREF_KEY_DAY_OF_YEAR = "LAST_DAY_OF_YEAR";
    public static final String PREF_KEY_TODAY_MOOD = "TODAY_MOOD";
    private int mLastYear;
    private int mLastDayOfYear;
    private int mTodayMood;

    public static final int DEFAULT_MOOD_VALUE = 3;

    private Calendar mCalendar;

    private ListView mMoodsListView;
    private Boolean mAutoScrolling = false;


    public static int mScreenHeight = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPreferences = getPreferences(MODE_PRIVATE);
        mLastYear = mPreferences.getInt(PREF_KEY_LASTYEAR, 0);
        mLastDayOfYear = mPreferences.getInt(PREF_KEY_DAY_OF_YEAR, 0);
        mTodayMood = mPreferences.getInt(PREF_KEY_TODAY_MOOD, DEFAULT_MOOD_VALUE);

        mCalendar = Calendar.getInstance();

        mMoodsListView = findViewById(R.id.lvMoods);

        List<Mood> moods = generateMoods();

        MoodAdapter adapter = new MoodAdapter(MainActivity.this, moods);
        mMoodsListView.setAdapter(adapter);

        mMoodsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(final AbsListView view, final int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    //System.out.println("ScrollY = " + mMoodsListView.getFirstVisiblePosition());

                    int scrollingTo = mMoodsListView.getFirstVisiblePosition();

                    View firstChildView = mMoodsListView.getChildAt(0);
                    int scrollY = -firstChildView.getTop();

                    if(mAutoScrolling && scrollY < 5) {
                        mTodayMood = scrollingTo;
                        System.out.println("Scroll End ==> " + mTodayMood);
                        mAutoScrolling = false;


                        mPreferences.edit().putInt(PREF_KEY_TODAY_MOOD, mTodayMood).apply();

                        return;
                    }


                    mAutoScrolling = true;


                    if(scrollY > mScreenHeight / 2) {
                        scrollingTo++;
                    }

                    System.out.println("Scrolling To " + scrollingTo);
                    mMoodsListView.smoothScrollToPositionFromTop(scrollingTo, 0, 500);

                    //getFirstVisiblePosition()
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenHeight = metrics.heightPixels;

        CheckForHistoryUpdate();

        mMoodsListView.setSelection(mTodayMood);



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

    private void CheckForHistoryUpdate() {


        int currentYear = mCalendar.get(Calendar.YEAR);
        int currentDayOfYear = mCalendar.get(Calendar.DAY_OF_YEAR);

        System.out.println("MainActivity:: Year = " + currentYear);
        System.out.println("MainActivity:: Day = " + currentDayOfYear);

        if(currentYear != mLastYear || currentDayOfYear != mLastDayOfYear) {

            if (mLastYear > 0) {
                System.out.println("MainActivity:: Déjà des infos");

            } else {

                System.out.println("MainActivity:: Pas encore d'info");
            }
            mLastYear = currentYear;
            mLastDayOfYear = currentDayOfYear;
            mTodayMood = DEFAULT_MOOD_VALUE;

            mPreferences.edit()
                    .putInt(PREF_KEY_LASTYEAR, mLastYear)
                    .putInt(PREF_KEY_DAY_OF_YEAR, mLastDayOfYear)
                    .putInt(PREF_KEY_TODAY_MOOD, mTodayMood)
                    .apply();

        } else {

            System.out.println("MainActivity:: Déjà fait aujourd'hui");
        }
    }
}
