package com.students;

import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class DateConverterCategoryPartitionTest {

    private static long startTime;

    @BeforeAll
    public static void init() {
        startTime = System.nanoTime();
    }

    @Test
    public void testIsToday() {
        long now = System.currentTimeMillis();
        assertTrue(DateConverter.isToday(now));

        long yesterday = now - 24 * 60 * 60 * 1000L;
        assertFalse(DateConverter.isToday(yesterday));
    }

    @Test
    public void testPrettifyDateLong() {
        long now = System.currentTimeMillis();
        String resultToday = DateConverter.prettifyDate(now);
        assertTrue(resultToday.matches("\\d{2}:\\d{2} [AP]M"));

        long notToday = now - 24 * 60 * 60 * 1000L;
        String resultNotToday = DateConverter.prettifyDate(notToday);
        assertTrue(resultNotToday.matches("\\d{2} [A-Za-z]{3} \\d{2}:\\d{2} [AP]M"));
    }

    @Test
    public void testGetDateOnlyString() {
        String validDate = "05/10/2020";
        long timestamp = DateConverter.getDateOnly(validDate);
        String outputDate = DateConverter.getDateOnly(timestamp);
        assertEquals(validDate, outputDate);

        String invalidDate = "not-a-date";
        long timeInvalid = DateConverter.getDateOnly(invalidDate);
        assertEquals(0, timeInvalid);
    }

    @Test
    public void testGetDateOnlyLong() {
        String today = DateConverter.getToday();
        long todayTimestamp = DateConverter.getDateOnly(today);
        assertEquals(today, DateConverter.getDateOnly(todayTimestamp));
    }

    @Test
    public void testGetDateAndTimeLong() {
        long now = System.currentTimeMillis();
        String dateTime = DateConverter.getDateAndTime(now);
        assertNotNull(dateTime);
        assertFalse(dateTime.isEmpty());
    }

    @Test
    public void testGetDateAndTimeString() {
        long now = System.currentTimeMillis();
        String nowStr = String.valueOf(now);
        String dateTime = DateConverter.getDateAndTime(nowStr);
        assertNotNull(dateTime);
        assertFalse(dateTime.isEmpty());

        String invalid = "notanumber";
        String invalidOutput = DateConverter.getDateAndTime(invalid);
        assertEquals("", invalidOutput);
    }

    @Test
    public void testGetTimeOnly() {
        long now = System.currentTimeMillis();
        String timeOnly = DateConverter.getTimeOnly(now);
        assertNotNull(timeOnly);
        assertFalse(timeOnly.isEmpty());
        assertTrue(timeOnly.matches("\\d{2}:\\d{2} [AP]M"));
    }

    @Test
    public void testGetTodayWithTime() {
        String todayWithTime = DateConverter.getTodayWithTime();
        assertNotNull(todayWithTime);
        assertFalse(todayWithTime.isEmpty());
        assertTrue(todayWithTime.matches("\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}:\\d{2}"));
    }

    @Test
    public void testGetToday() {
        String today = DateConverter.getToday();
        assertNotNull(today);
        assertFalse(today.isEmpty());
        assertTrue(today.matches("\\d{2}/\\d{2}/\\d{4}"));
    }

    @Test
    public void testGetDesiredFormat() {
        long now = System.currentTimeMillis();
        String formattedCurrent = DateConverter.getDesiredFormat(DateConverter.DateFormats.D_YYYYMMDD, now);
        SimpleDateFormat sdf = new SimpleDateFormat(DateConverter.DateFormats.D_YYYYMMDD.getDateFormat(), Locale.getDefault());
        String expected = sdf.format(new Date(now));
        assertEquals(expected, formattedCurrent);

        String formattedNow = DateConverter.getDesiredFormat(DateConverter.DateFormats.D_YYYYMMDD);
        assertNotNull(formattedNow);
        assertFalse(formattedNow.isEmpty());
    }

    @Test
    public void testIsTodayEdge() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long startOfToday = cal.getTimeInMillis();
        assertTrue(DateConverter.isToday(startOfToday));

        long endOfYesterday = startOfToday - 1;
        assertFalse(DateConverter.isToday(endOfYesterday));
    }

    @Test
    public void testParseDateInvalid() {
        long ts = DateConverter.parseDate("not-a-date", DateConverter.DateFormats.D_YYYYMMDD);
        assertEquals(0L, ts);
    }

    @Test
    public void testPrettifyDateEdge() {
        String formattedEpoch = DateConverter.prettifyDate(0L);
        assertTrue(formattedEpoch.matches("\\d{2} [A-Za-z]{3} \\d{2}:\\d{2} [AP]M"));
    }

    @Test
    public void testPrettifyDateEmptyOrWhitespaceString() {
        String resultEmpty = DateConverter.prettifyDate("");
        String expectedForZero = DateConverter.prettifyDate(0L);
        assertEquals(expectedForZero, resultEmpty);

        String resultSpaces = DateConverter.prettifyDate("   ");
        assertEquals(expectedForZero, resultSpaces);
    }

    @Test
    public void testGetDateOnlyLeapYear() {
        String leapDay = "29/02/2020";
        long ts = DateConverter.getDateOnly(leapDay);
        String formatted = DateConverter.getDateOnly(ts);
        assertEquals(leapDay, formatted);
    }

    @Test
    public void testGetDateOnlyInvalidDate() {
        String invalidDate = "31/04/2021";
        long ts = DateConverter.getDateOnly(invalidDate);
        assertNotEquals(0L, ts);
        String outputDate = DateConverter.getDateOnly(ts);
        assertEquals("01/05/2021", outputDate);
    }

    @Test
    public void testGetDateAndTimeNegativeString() {
        String negative = "-1000000";
        String result = DateConverter.getDateAndTime(negative);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testGetDateAndTimeStringWithSpaces() {
        String spaced = "  1000  ";
        String result = DateConverter.getDateAndTime(spaced);
        assertEquals("", result);
    }

    @Test
    public void testGetTimeOnlyKnownValue() {
        long fixedTimestamp = 1609459200000L; // 2021-01-01 00:00:00 GMT
        String timeOnly = DateConverter.getTimeOnly(fixedTimestamp);
        assertNotNull(timeOnly);
        assertTrue(timeOnly.matches("\\d{2}:\\d{2} [AP]M"));
    }

    @Test
    public void testParseAnyDateDifferentFormat() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd", Locale.getDefault());
        String dateStr = sdf.format(new Date(1609459200000L));
        long ts = DateConverter.parseAnyDate(dateStr);
        assertNotEquals(0, ts);
    }

    @Test
    public void testMainMethodOutput() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        try {
            DateConverter.main(new String[]{});
            String output = outContent.toString();
            assertTrue(output.contains("Today is: "));
            assertTrue(output.contains("Tomorrow is: "));
            assertTrue(output.contains("Now with time: "));
            assertTrue(output.contains("Prettified date: "));
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    public void testGetDaysBetweenSameDate() {
        String date = "2020-01-01";
        Long diff = DateConverter.getDaysBetweenTwoDate(date, date, DateConverter.DateFormats.D_YYYYMMDD);
        assertNotNull(diff);
        assertEquals(0L, diff.longValue());
    }

    @Test
    public void testGetHoursBetweenSameDate() {
        String date = "2020-01-01, 12:00AM";
        Long diff = DateConverter.getHoursBetweenTwoDate(date, date, DateConverter.DateFormats.D_YYYYMMDDHHMMA);
        assertNotNull(diff);
        assertEquals(0L, diff.longValue());
    }

    @Test
    public void testGetMinutesBetweenSameDate() {
        String date = "2020-01-01, 12:00AM";
        Long diff = DateConverter.getMinutesBetweenTwoDates(date, date, DateConverter.DateFormats.D_YYYYMMDDHHMMA);
        assertNotNull(diff);
        assertEquals(0L, diff.longValue());
    }

    @Test
    public void testGetDateFromDaysLargePositiveOffset() {
        String result = DateConverter.getDateFromDays(100);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testGetDateFromDaysLargeNegativeOffset() {
        String result = DateConverter.getDateFromDays(-100);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @AfterAll
    public static void tearDown() {
        long totalTime = System.nanoTime() - startTime;
        System.out.println("Total execution time for DateConverterCategoryPartitionTest: " + totalTime + " ns");
        StringBuilder html = new StringBuilder();
        html.append("<html><head><title>Total Execution Time Report</title></head><body>")
            .append("<h1>Total Execution Time for DateConverterCategoryPartitionTest</h1>")
            .append("<p>Total execution time: ")
            .append(totalTime)
            .append(" ns</p>")
            .append("</body></html>");

        try (FileWriter writer = new FileWriter("total_execution_time_category.html")) {
            writer.write(html.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
