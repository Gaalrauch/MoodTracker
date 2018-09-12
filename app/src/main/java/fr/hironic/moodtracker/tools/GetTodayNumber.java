package fr.hironic.moodtracker.tools;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by Gaalrauch
 * Get "today number", number of days since January 1st, 1970
 *
 */

public abstract class GetTodayNumber {

    /**
     * Calculate the "day number, number of days since January 1st 1970
     * Create a calendar with default time zone to get the date in user's TimeZone
     * Get GMT offset
     * Set calendar hour to midday (to prevent random 1 missing millisecond)
     * Get time in millis and add GMT Offset
     * Calculate number of day
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
