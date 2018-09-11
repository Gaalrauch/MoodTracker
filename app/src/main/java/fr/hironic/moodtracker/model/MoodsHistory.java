package fr.hironic.moodtracker.model;

import org.json.JSONArray;

public class MoodsHistory {

    private JSONArray mMoods; // moods history in JSONArray format, contains 0 to 7 moods, with their day number, type and comment.

    /**
     * Generate a JSONArray from history
     */
    public MoodsHistory (String history) {
        mMoods = new JSONArray();
        if(!history.equals("")) {
            try {
                mMoods = new JSONArray(history);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Get moods in JSONArray format
     * @return mMoods
     */
    public JSONArray getMoods() {
        return mMoods;
    }

    /**
     * Add a new mood to mMoods
     * @param dayNumber number of days since January 1st, 1970
     * @param mood mood value from 0 (sad) to 4 (super happy)
     * @param comment mood comment, may be an empty String
     */
    public void addMoodToHistory(int dayNumber, int mood, String comment) {
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
    }

    /**
     * Get moods history in String
     * @return mMoods in String format, ready to be saved in SharedPreferences
     */
    public String getHistory() {
        return mMoods.toString();
    }
}
