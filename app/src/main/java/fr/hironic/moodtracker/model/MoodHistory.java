package fr.hironic.moodtracker.model;

import org.json.JSONArray;

public class MoodHistory {

    private JSONArray mMoods;

    public MoodHistory (String history) {
        mMoods = new JSONArray();
        if(!history.equals("")) {
            try {
                mMoods = new JSONArray(history);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public JSONArray getMoods() {
        return mMoods;
    }

    public String saveMood(int dayNumber, int mood, String comment) {
        try {
            JSONArray moodData = new JSONArray();
            moodData.put(dayNumber);
            moodData.put(mood);
            moodData.put(comment);
            mMoods.put(moodData.toString());
            if(mMoods.length() > 7) { // We only keep last 7 moods
                mMoods.remove(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mMoods.toString();
    }
}
