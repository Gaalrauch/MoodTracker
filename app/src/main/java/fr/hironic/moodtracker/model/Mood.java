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

    Mood (int dayNumber, int type, String comment) {
        this.mDayNumber = dayNumber;
        this.mType = type;
        this.mComment = comment;
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
}
