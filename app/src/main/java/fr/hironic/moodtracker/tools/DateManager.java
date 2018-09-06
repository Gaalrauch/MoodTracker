package fr.hironic.moodtracker.tools;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class DateManager {

    public Calendar GenerateCalendar(String date) {

        String[] array = date.split("-");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(array[0]),
                Integer.parseInt(array[1]),
                Integer.parseInt(array[2]),
                0,
                0,
                0);

        return calendar;
    }

    public int GetDaysBetween(Calendar calendar1, Calendar calendar2) {

        long msDiff = calendar1.getTimeInMillis() - calendar2.getTimeInMillis();
        return (int) TimeUnit.MILLISECONDS.toDays(msDiff);
    }

}
