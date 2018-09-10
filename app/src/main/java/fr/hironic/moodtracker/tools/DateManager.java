package fr.hironic.moodtracker.tools;

import java.util.Calendar;
import java.util.TimeZone;

public abstract class DateManager {

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

        return (int) (calendar.getTimeInMillis() / 86400000);
    }

    public static int GetDaysBetweenTwoDates(int dayNumber1, int dayNumber2) {
        return dayNumber1 - dayNumber2;
    }

}
