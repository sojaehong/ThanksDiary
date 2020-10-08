package com.ssostudio.thanksdiary.utility;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateManager {

    public static String getNowDateToString(){
        Date currentDate = Calendar.getInstance().getTime();

        SimpleDateFormat weekdayFormat = new SimpleDateFormat("EE", Locale.getDefault());
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

        String weekDay = weekdayFormat.format(currentDate);
        String year = yearFormat.format(currentDate);
        String month = monthFormat.format(currentDate);
        String day = dayFormat.format(currentDate);

        String date = year + "." + month + "." + day + ". " + weekDay +"요일";
        return date;
    }

    public static int[] getTodayDate(){
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int[] date = {year, month, day};

        return date;
    }

    public static int[] getYesterdayDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int[] date = {year, month, day};

        return date;
    }

    public static int[] dateToIntArray(Calendar date){
        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH) + 1;
        int day = date.get(Calendar.DAY_OF_MONTH);

        int[] dates = {year, month, day};

        return dates;
    }

    public static int[] calendarDayToIntArray(CalendarDay date){
        int year = date.getYear();
        int month = date.getMonth() + 1;
        int day = date.getDay();

        int[] dates = {year, month, day};

        return dates;
    }

    public static String dateTimeZoneFormat(int[] date){
        Calendar calendar = Calendar.getInstance();

        DateFormat format = DateFormat.getDateInstance(DateFormat.DEFAULT);
        format.setTimeZone(calendar.getTimeZone());
        calendar.set(date[0], date[1] - 1, date[2]);
        String formatDate = format.format(calendar.getTime());

        return formatDate;
    }

    public static String dateTimeZoneFullFormat(int[] date){
        Calendar calendar = Calendar.getInstance();

        DateFormat format = DateFormat.getDateInstance(DateFormat.FULL);
        format.setTimeZone(calendar.getTimeZone());
        calendar.set(date[0], date[1] - 1, date[2]);
        String formatDate = format.format(calendar.getTime());

        return formatDate;
    }

    public static long getTimestamp(){
        Calendar calendar = Calendar.getInstance();

        Date date = calendar.getTime();
        long timestamp = date.getTime();

        return timestamp;
    }

    public static int[] timestampToIntArray(long timestamp){
        Calendar calendar = Calendar.getInstance();

        DateFormat format = DateFormat.getDateInstance(DateFormat.DEFAULT);
        format.setTimeZone(calendar.getTimeZone());

        Date date = new Date(timestamp);
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int[] dates = {year, month, day};

        return dates;
    }

    public static long intArrayToTimestamp(int[] dates){
        Calendar calendar = Calendar.getInstance();
        // ????
//        DateFormat format = DateFormat.getDateInstance(DateFormat.DEFAULT);
//        format.setTimeZone(calendar.getTimeZone());
        //
        calendar.set(dates[0], dates[1] - 1, dates[2]);
        Date date = calendar.getTime();
        long timestamp = date.getTime();

        return timestamp;
    }

    public static int[] getThisWeekMonday(){
        Calendar calendar = Calendar.getInstance();

        DateFormat format = DateFormat.getDateInstance(DateFormat.DEFAULT);
        format.setTimeZone(calendar.getTimeZone());
        calendar.add(Calendar.DATE, 1 - calendar.get(Calendar.DAY_OF_WEEK));

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int[] dates = {year, month, day};

        return dates;
    }

    public static int[] getThisWeekSunday(){
        Calendar calendar = Calendar.getInstance();

        DateFormat format = DateFormat.getDateInstance(DateFormat.DEFAULT);
        format.setTimeZone(calendar.getTimeZone());
        calendar.add(Calendar.DATE, 7 - calendar.get(Calendar.DAY_OF_WEEK));

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int[] dates = {year, month, day};

        return dates;
    }

    public static int[] getThisMonthLastDay(){
        Calendar calendar = Calendar.getInstance();

        DateFormat format = DateFormat.getDateInstance(DateFormat.DEFAULT);
        format.setTimeZone(calendar.getTimeZone());

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int[] date = {year, month, day};

        return date;
    }

    public static int[] getLastDays(int lastDay){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -lastDay);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int[] date = {year, month, day};

        return date;
    }

    public static long getFreeDateTimestamp(int freeDay){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, freeDay);

        Date date = calendar.getTime();
        long timestamp = date.getTime();

        return timestamp;
    }

}
