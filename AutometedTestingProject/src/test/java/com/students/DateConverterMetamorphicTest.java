package com.students;


import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;




public class DateConverterMetamorphicTest {
    
    private static long startTime;

    @BeforeAll
    public static void init() {
        startTime = System.nanoTime();
    }

    @Test
    public void testParseAnyDateWithExtraSpaces() {
        long ts = DateConverter.parseAnyDate(" 2021-04-05 ");
        assertNotEquals(0L, ts);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formatted = sdf.format(new Date(ts));
        assertEquals("2021-04-05", formatted);
    }

    @Test
    public void testGetDesiredFormatConsistency() throws ParseException, InterruptedException {
        String format1 = DateConverter.getDesiredFormat(DateConverter.DateFormats.D_YYYYMMDD);
        Thread.sleep(10);
        String format2 = DateConverter.getDesiredFormat(DateConverter.DateFormats.D_YYYYMMDD);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date d1 = sdf.parse(format1);
        Date d2 = sdf.parse(format2);
        assertEquals(sdf.format(d1), sdf.format(d2));
    }

    @Test
    public void testGetDateFromDaysNegative() {
        String result = DateConverter.getDateFromDays(-10);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testGetDateFromDaysMonotonicity() {
        String date5 = DateConverter.getDateFromDays(5);
        String date6 = DateConverter.getDateFromDays(6);
        assertNotEquals(date5, date6);
    }

    @Test
    public void testGetDesiredFormatWithTimestamp() {
        long fixedTimestamp = 1622505600000L; // 1 June 2021
        String formatted = DateConverter.getDesiredFormat(DateConverter.DateFormats.D_YYYYMMDD, fixedTimestamp);
        SimpleDateFormat sdf = new SimpleDateFormat(DateConverter.DateFormats.D_YYYYMMDD.getDateFormat(), Locale.getDefault());
        String expected = sdf.format(new Date(fixedTimestamp));
        assertEquals(expected, formatted);
    }


@Test
public void testPrettifyDateString() {
    long now = System.currentTimeMillis();
    String nowStr = String.valueOf(now);
    String result = DateConverter.prettifyDate(nowStr);
    assertEquals(DateConverter.prettifyDate(now), result);
}

@Test
public void testGetTomorrow() {
    String tomorrow = DateConverter.getTomorrow();
    try {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date todayDate = sdf.parse(DateConverter.getToday());
        Calendar cal = Calendar.getInstance();
        cal.setTime(todayDate);
        cal.add(Calendar.DATE, 1);
        String expectedTomorrow = sdf.format(cal.getTime());
        assertEquals(expectedTomorrow, tomorrow);
    } catch (ParseException e) {
        fail("ParseException thrown: " + e.getMessage());
    }
}

@Test
public void testGetDaysBetweenTwoDate() {
    String date1 = "2020-01-01";
    String date2 = "2020-01-02";
    Long daysDiff = DateConverter.getDaysBetweenTwoDate(date1, date2, DateConverter.DateFormats.D_YYYYMMDD);
    Long daysDiffReverse = DateConverter.getDaysBetweenTwoDate(date2, date1, DateConverter.DateFormats.D_YYYYMMDD);
    assertEquals(-1L, daysDiff.longValue());
    assertEquals(1L, daysDiffReverse.longValue());
}

@Test
public void testGetHoursBetweenTwoDate() {
    String date1 = "2020-01-01, 12:00AM";
    String date2 = "2020-01-02, 12:00AM";
    Long hoursDiff = DateConverter.getHoursBetweenTwoDate(date1, date2, DateConverter.DateFormats.D_YYYYMMDDHHMMA);
    Long hoursDiffReverse = DateConverter.getHoursBetweenTwoDate(date2, date1, DateConverter.DateFormats.D_YYYYMMDDHHMMA);
    assertEquals(-24L, hoursDiff.longValue());
    assertEquals(24L, hoursDiffReverse.longValue());
}

@Test
public void testGetMinutesBetweenTwoDates() {
    String date1 = "2020-01-01, 12:00AM";
    String date2 = "2020-01-01, 12:01AM";
    Long minutesDiff = DateConverter.getMinutesBetweenTwoDates(date1, date2, DateConverter.DateFormats.D_YYYYMMDDHHMMA);
    Long minutesDiffReverse = DateConverter.getMinutesBetweenTwoDates(date2, date1, DateConverter.DateFormats.D_YYYYMMDDHHMMA);
    assertEquals(-1L, minutesDiff.longValue());
    assertEquals(1L, minutesDiffReverse.longValue());
}

@Test
public void testParseAnyDate() {
    String validDate = "2021-04-05";
    long parsedTime = DateConverter.parseAnyDate(validDate);
    assertNotEquals(0, parsedTime);

    String formatted = DateConverter.getDesiredFormat(DateConverter.DateFormats.D_YYYYMMDD);
    long parsedFormatted = DateConverter.parseAnyDate(formatted);
    assertNotEquals(0, parsedFormatted);
}

@Test
public void testParseDate() {
    String dateStr = "2021-12-31";
    long parsedTime = DateConverter.parseDate(dateStr, DateConverter.DateFormats.D_YYYYMMDD);
    SimpleDateFormat sdf = new SimpleDateFormat(DateConverter.DateFormats.D_YYYYMMDD.getDateFormat(), Locale.getDefault());
    String reformatted = sdf.format(new Date(parsedTime));
    assertEquals(dateStr, reformatted);
}

@Test
public void testGetDateFromDays() {
    SimpleDateFormat sdf = new SimpleDateFormat(DateConverter.DateFormats.D_DDMMyy_N.getDateFormat(), Locale.getDefault());
    String todayFormatted = sdf.format(new Date());
    String resultToday = DateConverter.getDateFromDays(0);
    assertEquals(todayFormatted, resultToday);

    String tomorrowExpected;
    try {
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date today = sdf2.parse(DateConverter.getToday());
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.DATE, 1);
        Date tomorrow = cal.getTime();
        tomorrowExpected = sdf.format(tomorrow);
    } catch (ParseException e) {
        tomorrowExpected = "";
    }
    String resultTomorrow = DateConverter.getDateFromDays(1);
    assertEquals(tomorrowExpected, resultTomorrow);

    String yesterdayExpected;
    try {
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date today = sdf2.parse(DateConverter.getToday());
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.DATE, -1);
        Date yesterday = cal.getTime();
        yesterdayExpected = sdf.format(yesterday);
    } catch (ParseException e) {
        yesterdayExpected = "";
    }
    String resultYesterday = DateConverter.getDateFromDays(-1);
    assertEquals(yesterdayExpected, resultYesterday);
    }


    @Test
    public void testPrettifyDateStringErrorLogging() {
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(errContent));
        try {
            String result = DateConverter.prettifyDate("non-numeric");
            assertEquals(DateConverter.prettifyDate(0L), result);
            assertFalse(errContent.toString().isEmpty(), "Expected error output from NumberFormatException");
        } finally {
            System.setErr(originalErr);
        }
    }

    @Test
    public void testGetDateOnlyStringErrorLogging() {
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(errContent));
        try {
            long result = DateConverter.getDateOnly("invalid-date-format");
            assertEquals(0L, result);
            assertFalse(errContent.toString().isEmpty(), "Expected error output from ParseException in getDateOnly");
        } finally {
            System.setErr(originalErr);
        }
    }

    @Test
    public void testGetDateAndTimeStringErrorLogging() {
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(errContent));
        try {
            String result = DateConverter.getDateAndTime("non-numeric");
            assertEquals("", result);
            assertFalse(errContent.toString().isEmpty(), "Expected error output from NumberFormatException in getDateAndTime");
        } finally {
            System.setErr(originalErr);
        }
    }

    @AfterAll
    public static void tearDown() {
        long totalTime = System.nanoTime() - startTime;
        System.out.println("Total execution time for DateConverterMetamorphicTest: " + totalTime + " ns");
        StringBuilder html = new StringBuilder();
        html.append("<html><head><title>Total Execution Time Report</title></head><body>")
            .append("<h1>Total Execution Time for DateConverterMetamorphicTest</h1>")
            .append("<p>Total execution time: ")
            .append(totalTime)
            .append(" ns</p>")
            .append("</body></html>");

        try (FileWriter writer = new FileWriter("total_execution_time_metamorphic.html")) {
            writer.write(html.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
