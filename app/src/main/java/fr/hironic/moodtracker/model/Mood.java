package fr.hironic.moodtracker.model;

import org.json.JSONArray;

/**
 * Created by Gaalrauch
 * Create a mood with following data:
 * mDayNumber : "day number" of the day when the mood was created. Day number is the number of days since January 1st, 1970 in the GMT TimeZone
 * mType : type of mood from 0 (sad) to 4 (super happy)
 * mComment : comment of this mood, may be an empty String
 *
 */

public class Mood {

    private int mDayNumber;
    private int mType;
    private String mComment;

    public Mood(int dayNumber, int type, String comment) {
        this.mDayNumber = dayNumber;
        this.mType = type;
        this.mComment = comment;
    }

    public Mood(String mood) {
        try {
            JSONArray array= new JSONArray(mood);
            mDayNumber = array.getInt(0);
            mType = array.getInt(1);
            mComment = array.getString(2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String toString() {
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(mDayNumber);
        jsonArray.put(mType);
        jsonArray.put(mComment);
        return jsonArray.toString();
    }

    public int getDayNumber() {
        return mDayNumber;
    }

    public int getType() {
        return mType;
    }

    public String getComment() {
        return mComment;
    }

    public void setType(int type) {
        mType = type;
    }

    public void setComment(String comment) {
        mComment = comment;
    }
}
