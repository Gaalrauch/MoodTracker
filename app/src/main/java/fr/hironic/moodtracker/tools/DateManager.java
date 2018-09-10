package fr.hironic.moodtracker.tools;

import java.util.Calendar;
import java.util.TimeZone;

public abstract class DateManager {

    /**
     * Return the number of days since 1st January 1970
     * Create a calendar with default time zone to get the date in user's TimeZone
     * Create another calendar using GMT TimeZone
     * Set this new calendar to the date of the first calendar
     * Return the number of days.
     * @return
     */
    public static int getTodayNumber() {
        Calendar calendar = Calendar.getInstance(); // Get date from our TimeZone
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                12,
                0,
                0);
        Calendar gmtCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT")); // Get date from GMT TimeZone
        // Set gmtCalendar to the same date calendar
        gmtCalendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                12,
                0,
                0);

        return (int) (gmtCalendar.getTimeInMillis() / 86400000);
    }
}
