package com.students;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;




    public class DateConverter {

        public enum DateFormats {
            D_YYMMDD("yy-MM-dd"), D_DDMMyy("dd-MM-yy"),
            D_YYMMDD_N("yy-MMM-dd"), D_DDMMyy_N("dd-MMM-yy"),
            D_YYMMDDHHMMA_N("yy-MMM-dd, hh:mma"), D_DDMMyyHHMMA_N("dd-MMM-yy, hh:mma"),
            S_YYMMDD("yy/MM/dd"), S_DDMMyy("dd/MM/yy"),
            S_YYMMDDHHMMA("yy/MM/dd, hh:mma"), S_DDMMyyHHMMA("dd/MM/yy, hh:mma"),
            S_YYMMDDHHMMA_N("yy/MMM/dd, hh:mma"), S_DDMMyyHHMMA_N("dd/MMM/yy, hh:mma"),
            D_YYYYMMDD("yyyy-MM-dd"), D_DDMMYYYY("dd-MM-yyyy"),
            D_YYYYMMDDHHMMA("yyyy-MM-dd, hh:mma"), D_DDMMYYYYHHMMA("dd-MM-yyyy, hh:mma"),
            D_YYYYMMDD_N("yyyy-MMM-dd"), D_DDMMYYYY_N("dd-MMM-yyyy"),
            D_YYYYMMDDHHMMA_N("yyyy-MMM-dd, hh:mma"), D_DDMMYYYYHHMMA_N("dd-MMM-yyyy, hh:mma"),
            S_YYYYMMDD("yyyy/MM/dd"), S_DDMMYYYY("dd/MM/yyyy"),
            S_YYYYMMDDHHMMA("yyyy/MM/dd, hh:mma"), S_DDMMYYYYHHMMA("dd/MM/yyyy, hh:mma"),
            S_YYYYMMDDHHMMA_N("yyyy/MMM/dd, hh:mma"), S_DDMMYYYYHHMMA_N("dd/MMM/yyyy, hh:mma"),
            D_YYMMDDHHMMSSA_N("yy-MMM-dd, hh:mm:ssa"), D_DDMMyyHHMMSSA_N("dd-MMM-yy, hh:mm:ssa"),
            S_YYMMDDHHMMSSA("yy/MM/dd, hh:mm:ssa"), S_DDMMyyHHMMSSA("dd/MM/yy, hh:mm:ssa"),
            S_YYMMDDHHMMSSA_N("yy/MMM/dd, hh:mm:ssa"), S_DDMMyyHHMMSSA_N("dd/MM/yy, hh:mm:ssa"),
            D_YYYYMMDDHHMMSSA("yyyy-MM-dd, hh:mm:ssa"), D_DDMMYYYYHHMMSSA("dd-MM-yyyy, hh:mm:ssa"),
            D_YYYYMMDDHHMMSSA_N("yyyy-MMM-dd, hh:mm:ssa"), D_DDMMYYYYHHMMSSA_N("dd-MMM-yyyy, hh:mm:ssa"),
            S_YYYYMMDDHHMMSSA("yyyy/MM/dd, hh:mm:ssa"), S_DDMMYYYYHHMMSSA("dd/MM/yyyy, hh:mm:ssa"),
            S_YYYYMMDDHHMMSSA_N("yyyy/MMM/dd, hh:mm:ssa"), S_DDMMYYYYHHMMSSA_N("dd/MMM/yyyy, hh:mm:ssa"),
            HHMMA("hh:mma"), HHMM("hh:mm"), HHMMSSA("hh:mm:ssa"), HHMMSS("hh:mm:ss");
        
            private String dateFormat;
        
            DateFormats(String dateFormat) {
                this.dateFormat = dateFormat;
            }
        
            public String getDateFormat() {
                return dateFormat;
            }
        }
        
        // Custom replacement for Android DateUtils.isToday()
        public static boolean isToday(long timestamp) {
            Calendar current = Calendar.getInstance();
            Calendar given = Calendar.getInstance();
            given.setTimeInMillis(timestamp);
            return current.get(Calendar.YEAR) == given.get(Calendar.YEAR) &&
                current.get(Calendar.DAY_OF_YEAR) == given.get(Calendar.DAY_OF_YEAR);
        }
        
        /**
         * Returns time in "hh:mm a" if the timestamp is today, or in "dd MMM hh:mm a" otherwise.
         */
        public static String prettifyDate(long timestamp) {
            SimpleDateFormat dateFormat;
            if (isToday(timestamp)) {
                dateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            } else {
                dateFormat = new SimpleDateFormat("dd MMM hh:mm a", Locale.getDefault());
            }
            return dateFormat.format(new Date(timestamp));
        }
        
        /**
         * Overloaded method that accepts a string representing the timestamp.
         */
        public static String prettifyDate(String timestamp) {
            long time = 0;
            try {
                time = Long.parseLong(timestamp);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return prettifyDate(time);
        }
        
        /**
         * Parses a date string in "dd/MM/yyyy" format and returns the corresponding timestamp.
         */
        public static long getDateOnly(String date) {
            SimpleDateFormat sample = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            try {
                return sample.parse(date).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 0;
        }
        
        /**
         * Returns the date in "dd/MM/yyyy" format for the given timestamp.
         */
        public static String getDateOnly(long time) {
            return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date(time));
        }
        
        /**
         * Returns the date and time in "dd/MM/yyyy, hh:mm a" format for the given timestamp.
         */
        public static String getDateAndTime(long time) {
            SimpleDateFormat sample = new SimpleDateFormat("dd/MM/yyyy, hh:mm a", Locale.getDefault());
            return sample.format(new Date(time));
        }
        
        /**
         * Returns the date and time in "dd/MM/yyyy, hh:mm a" format from a string timestamp.
         */
        public static String getDateAndTime(String time) {
            SimpleDateFormat sample = new SimpleDateFormat("dd/MM/yyyy, hh:mm a", Locale.getDefault());
            try {
                long t = Long.parseLong(time);
                return sample.format(new Date(t));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return "";
            }
        }
        
        /**
         * Returns the time in "hh:mm a" format for the given timestamp.
         */
        public static String getTimeOnly(long time) {
            SimpleDateFormat sample = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            return sample.format(new Date(time));
        }
        
        /**
         * Returns today's date with time in "dd/MM/yyyy HH:mm:ss" format.
         */
        public static String getTodayWithTime() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
            return dateFormat.format(new Date());
        }
        
        /**
         * Returns today's date in "dd/MM/yyyy" format.
         */
        public static String getToday() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return dateFormat.format(new Date());
        }
        
        /**
         * Returns tomorrow's date in "dd/MM/yyyy" format.
         */
        public static String getTomorrow() {
            try {
                Calendar calendar = Calendar.getInstance();
                Date today = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(getToday());
                calendar.setTime(today);
                calendar.add(Calendar.DATE, 1);
                Date tomorrow = calendar.getTime();
                return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(tomorrow);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
        
        /**
         * Returns the number of days between the two given dates (in the given format).
         */
        public static Long getDaysBetweenTwoDate(String old, String newDate, DateFormats dateFormats) {
            SimpleDateFormat myFormat = new SimpleDateFormat(dateFormats.getDateFormat(), Locale.getDefault());
            try {
                Date date1 = myFormat.parse(old);
                Date date2 = myFormat.parse(newDate);
                long diff = date1.getTime() - date2.getTime();
                return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
        
        /**
         * Returns the number of hours between the two given dates (in the given format).
         */
        public static Long getHoursBetweenTwoDate(String old, String newDate, DateFormats dateFormats) {
            SimpleDateFormat myFormat = new SimpleDateFormat(dateFormats.getDateFormat(), Locale.getDefault());
            try {
                Date date1 = myFormat.parse(old);
                Date date2 = myFormat.parse(newDate);
                long diff = date1.getTime() - date2.getTime();
                return TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
        
        /**
         * Returns the number of minutes between the two given dates (in the given format).
         */
        public static Long getMinutesBetweenTwoDates(String old, String newDate, DateFormats dateFormats) {
            SimpleDateFormat myFormat = new SimpleDateFormat(dateFormats.getDateFormat(), Locale.getDefault());
            try {
                Date date1 = myFormat.parse(old);
                Date date2 = myFormat.parse(newDate);
                long diff = date1.getTime() - date2.getTime();
                return TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
        
        /**
         * Tries to parse the given date string using all available formats.
         * @return the timestamp in milliseconds if successful, or 0 otherwise.
         */
        public static long parseAnyDate(String date) {
            long time = 0;
            for (DateFormats formats : DateFormats.values()) {
                try {
                    SimpleDateFormat format = new SimpleDateFormat(formats.getDateFormat(), Locale.getDefault());
                    time = format.parse(date).getTime();
                    return time;
                } catch (ParseException e) {
                    // continue trying other formats
                }
            }
            return time;
        }
        
        public static long parseDate(String date, DateFormats dateFormats) {
            SimpleDateFormat format = new SimpleDateFormat(dateFormats.getDateFormat(), Locale.getDefault());
            try {
                return format.parse(date).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 0;
        }
        
        public static String getDesiredFormat(DateFormats formats) {
            SimpleDateFormat format = new SimpleDateFormat(formats.getDateFormat(), Locale.getDefault());
            return format.format(new Date());
        }
        
        public static String getDesiredFormat(DateFormats formats, long date) {
            SimpleDateFormat format = new SimpleDateFormat(formats.getDateFormat(), Locale.getDefault());
            return format.format(new Date(date));
        }
        
        /**
         * Since Android-specific date and time picker dialogs are not available in standard Java,
         * the corresponding methods have been removed.
         */
        
        public static String getDateFromDays(int numOfDays) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, numOfDays);
            return getDesiredFormat(DateFormats.D_DDMMyy_N, cal.getTimeInMillis());
        }
        
        // A main method for testing purposes
        public static void main(String[] args) {
            System.out.println("Today is: " + getToday());
            System.out.println("Tomorrow is: " + getTomorrow());
            System.out.println("Now with time: " + getTodayWithTime());
            long timestamp = System.currentTimeMillis();
            System.out.println("Prettified date: " + prettifyDate(timestamp));
        }
    }    

