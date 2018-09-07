package fr.hironic.moodtracker.model;

import android.content.Context;

import org.json.JSONArray;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;

public class MoodHistory {

    private boolean mTest = true;

    private Context mContext;
    private static final String FILE_NAME = "history.txt";

    private JSONArray mMoods;

    public JSONArray getMoods() {
        return mMoods;
    }

    private void SetHistoryTest() {

        SaveMood("2018_7_30", 3, "I love apples");
        SaveMood("2018_7_31", 0, "I hate vegetables");
        SaveMood("2018_8_1", 1, "I'm a bit frustrated");
        SaveMood("2018_8_3", 2, "I'm just not happy");
        SaveMood("2018_8_4", 4, "I meet LOVE");
        SaveMood("2018_8_5", 0, "I hate vegetables");
        SaveMood("2018_8_6", 1, "I'm a bit frustrated");
    }

    public MoodHistory (Context context) {
        mContext = context;
        mMoods = new JSONArray();

        if(mTest) {
            SetHistoryTest();
        } else {
            ReloadMoodsFromFile();
        }

        System.out.println(mMoods);

        for(int i = 0; i < mMoods.length(); i++) {

            try {
                System.out.println("Entry " + i + " : " + mMoods.getString(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void SaveMood(String date, int mood, String comment) {

        try {
            JSONArray moodData = new JSONArray();
            moodData.put(date);
            moodData.put(mood);
            moodData.put(comment);
            mMoods.put(moodData.toString());
            // We only keep last 7 moods
            if(mMoods.length() > 7) {
                mMoods.remove(0);
            }

            FileOutputStream fileOutputStream = mContext.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fileOutputStream.write(mMoods.toString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ReloadMoodsFromFile() {
        File historyFile = new File(FILE_NAME);
        if(!historyFile.exists()) {
            try {
                historyFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            FileInputStream fileInputStream = mContext.openFileInput(FILE_NAME);
            int size = fileInputStream.available();
            byte[] buffer = new byte[size];
            fileInputStream.read(buffer);
            fileInputStream.close();
            String text = new String(buffer);
            mMoods = new JSONArray(text);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
