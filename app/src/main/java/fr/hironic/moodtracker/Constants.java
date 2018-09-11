package fr.hironic.moodtracker;

public class Constants {

    // SharedPreferences keys
    public static final String PREF_KEY_TODAY_NUMBER = "LAST_DATE";
    public static final String PREF_KEY_TODAY_MOOD = "TODAY_MOOD";
    public static final String PREF_KEY_TODAY_COMMENT = "TODAY_COMMENT";
    public static final String PREF_MOOD_HISTORY = "MOOD_HISTORY";

    // Default mood value, on first launch or new day
    public static final int DEFAULT_MOOD_VALUE = 3;
    // Moods background colors
    public static final int[] MOOD_COLORS = { R.color.faded_red,
            R.color.warm_grey,
            R.color.cornflower_blue_65,
            R.color.light_sage,
            R.color.banana_yellow
    };

    public static final int[] MOOD_DRAWABLES = { R.drawable.smiley_sad,
            R.drawable.smiley_disappointed,
            R.drawable.smiley_normal,
            R.drawable.smiley_happy,
            R.drawable.smiley_super_happy
    };

}
