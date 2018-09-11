package fr.hironic.moodtracker.tools;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Gaalrauch
 * Get "today number", number of days since January 1st, 1970
 *
 */

public abstract class GetTodayNumber {

    /**
     * Calculate the "day number"
     * Create a calendar with default time zone to get the date in user's TimeZone
     * Create another calendar using GMT TimeZone
     * Set this new calendar to the date of the first calendar
     * Get the number of days since 1970 for this GMT calendar and return it.
     * @return day number
     */
    public static int getTodayNumber() {
        Calendar userCalendar = Calendar.getInstance(); // Get date from our TimeZone
        userCalendar.set(userCalendar.get(Calendar.YEAR),
                userCalendar.get(Calendar.MONTH),
                userCalendar.get(Calendar.DAY_OF_MONTH),
                12,
                0,
                0);
        Calendar gmtCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT")); // Get date from GMT TimeZone
        // Set gmtCalendar to the same date as userCalendar
        gmtCalendar.set(userCalendar.get(Calendar.YEAR),
                userCalendar.get(Calendar.MONTH),
                userCalendar.get(Calendar.DAY_OF_MONTH),
                12,
                0,
                0);

        return (int) (gmtCalendar.getTimeInMillis() / 86400000);
    }
}
