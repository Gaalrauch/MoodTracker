package fr.hironic.moodtracker.model;

import android.content.Context;

import org.json.JSONArray;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MoodHistory {


    private Context mContext;
    private static final String FILE_NAME = "history.txt";

    private JSONArray mMoods;

    public MoodHistory (Context context) {
        mContext = context;
        mMoods = new JSONArray();
        ReloadMoodsFromFile();
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
