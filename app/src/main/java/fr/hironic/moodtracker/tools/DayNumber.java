package fr.hironic.moodtracker.tools;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Gaalrauch
 * Manipulate "day number", number of days since January 1st, 1970
 * The day numbers are used to manage history (check if the date changed, count days)
 *
 */

public abstract class DayNumber {

    /**
     * Calculate today's "day number"
     * Create a calendar with default time zone to get the date in user's TimeZone
     * Get GMT offset
     * Set calendar hour to midday (to prevent random 1 missing millisecond)
     * Get time in millis and add GMT offset
     * Calculate number of days
     * @return day number
     */
    public static int getTodayNumber() {
        Calendar calendar = Calendar.getInstance();
        TimeZone timeZone = calendar.getTimeZone();
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                12,
                0,
                0);
        long millis = calendar.getTimeInMillis() + timeZone.getRawOffset();
        return (int) (millis / 86400000);
    }
}
