package fr.hironic.moodtracker.model;

import org.json.JSONArray;

public class MoodsHistory {

    private static JSONArray mMoods; // moods history

    /**
     * Generate a JSONArray from history
     */
    public static void setMoods(String history) {
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
     * Return a JSONArray containing 0 to 7 moods from history, with their type and comment.
     */
    public static JSONArray getMoods() {
        return mMoods;
    }

    /**
     * Add a new mood to mMoods and return the new history in a String
     * @param dayNumber number of days since January 1st, 1970
     * @param mood mood value from 0 (sad) to 4 (super happy)
     * @param comment mood comment, may be an empty String
     * @return mMoods.toString()
     */
    public static String addMoodToHistory(int dayNumber, int mood, String comment) {
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
