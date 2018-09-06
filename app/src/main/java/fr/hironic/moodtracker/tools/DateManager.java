package fr.hironic.moodtracker.tools;

import java.util.Calendar;

public abstract class DateManager {

    public static String GetTodayDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR) + "_" + (calendar.get(Calendar.MONTH) + 1) + "_" + calendar.get(Calendar.DAY_OF_MONTH);
    }

    private static Calendar GenerateCalendar(String date) {
        String[] array = date.split("_");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(array[0]),
                Integer.parseInt(array[1]),
                Integer.parseInt(array[2]),
                0,
                0,
                0);

        return calendar;
    }

    public static int GetDaysBetweenTwoDates(String date1, String date2) {
        Calendar calendar1 = GenerateCalendar(date1);
        Calendar calendar2 = GenerateCalendar(date2);
        long msDiff = calendar1.getTimeInMillis() - calendar2.getTimeInMillis();
        return Math.round((float) msDiff / 86400000);
    }

}
