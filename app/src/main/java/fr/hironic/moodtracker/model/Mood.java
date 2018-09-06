package fr.hironic.moodtracker.model;

import android.graphics.drawable.Drawable;

public class Mood {


    private int color;
    private Drawable icon;

    public Mood (int color, Drawable icon) {
        this.color = color;
        this.icon = icon;
    }

    public int getColor() {
        return color;
    }

    public Drawable getIcon() {
        return icon;
    }
}
