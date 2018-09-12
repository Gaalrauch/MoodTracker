package fr.hironic.moodtracker.model;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Gaalrauch
 * Manage Moods history
 *
 */

public class MoodsHistory {

    private ArrayList<Mood> mMoods = new ArrayList<>(); // List of moods to manage history

    /**
     * Generate mMoods, list of Mood, from history
     * history is a JSONArray String containing each Mood in JSONArray String too
     */
    public MoodsHistory (String history) {

        if(!history.equals("")) {
            try {
                JSONArray array = new JSONArray(history);
                for(int i = 0; i < array.length(); i++) {

                    JSONArray moodData = new JSONArray(array.getString(i));
                    int dayNumber = moodData.getInt(0);
                    int mood = moodData.getInt(1);
                    String comment = moodData.getString(2);

                    mMoods.add(new Mood(dayNumber, mood, comment));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Get moods list
     * @return mMoods
     */
    public ArrayList<Mood> getMoods() {
        return mMoods;
    }

    /**
     * Add a new mood to mMoods
     * @param mood the mood to add
     */
    public void addMoodToHistory(Mood mood) {
        mMoods.add(mood);
        if(mMoods.size() > 7) { // We only keep last 7 moods
            mMoods.remove(0);
        }
    }

    /**
     * Get moods history
     * @return mMoods in JSONArray String, ready to be saved in SharedPreferences
     */
    public String getHistory() {
        return mMoods.toString();
    }
}
