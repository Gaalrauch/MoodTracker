package fr.hironic.moodtracker.model;

import org.json.JSONArray;

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
