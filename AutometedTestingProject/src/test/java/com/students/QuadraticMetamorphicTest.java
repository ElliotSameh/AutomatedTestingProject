
package com.students;

import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class QuadraticMetamorphicTest {

    private static long startTime; 

    @BeforeAll
    public static void init() {
        startTime = System.nanoTime();
    }

    @Test
    public void testMetamorphic() throws NotEnoughPrecisionException {
        double a1 = 1, b1 = -3, c1 = 2;
        String result1 = getOutput(a1, b1, c1);
        System.out.println("Result for a=1, b=-3, c=2: " + result1);

        double a2 = a1 + 1, b2 = b1 + 2, c2 = c1 + 3;
        String result2 = getOutput(a2, b2, c2);
        System.out.println("Result for a=2, b=-1, c=5: " + result2);

        assertNotEquals(result1, result2);
    }

    private String getOutput(double a, double b, double c) throws NotEnoughPrecisionException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;  
        PrintStream ps = new PrintStream(outputStream);
        System.setOut(ps);

        Quadratic.solveQuadratic(a, b, c);

        System.setOut(originalOut);  
        return outputStream.toString();
    }

    @Test
    public void testMetamorphicScaling() throws NotEnoughPrecisionException {
        double a1 = 1, b1 = -3, c1 = 2;
        String result1 = getOutput(a1, b1, c1);

        double scale = 2.0;
        String result2 = getOutput(a1 * scale, b1 * scale, c1 * scale);

        System.out.println("Original result: " + result1);
        System.out.println("Scaled result (scale=2): " + result2);

        assertEquals(result1, result2);
    }

    @Test
    public void testMetamorphicScalingNegative() throws NotEnoughPrecisionException {
        double a1 = 1, b1 = -3, c1 = 2;
        String result1 = getOutput(a1, b1, c1);

        double scale = -1.0;
        String result2 = getOutput(a1 * scale, b1 * scale, c1 * scale);

        System.out.println("Original result: " + result1);
        System.out.println("Scaled result (scale=-1): " + result2);

        assertEquals(result1, result2);
    }

    @Test
    public void testMetamorphicScalingComplex() throws NotEnoughPrecisionException {
        double a1 = 1, b1 = 2, c1 = 5;
        String result1 = getOutput(a1, b1, c1);

        double scale = 3.0;
        String result2 = getOutput(a1 * scale, b1 * scale, c1 * scale);

        System.out.println("Original complex result: " + result1);
        System.out.println("Scaled complex result (scale=3): " + result2);

        assertEquals(result1, result2);
    }

    @Test
    public void testMetamorphicScalingZeroC() {
        double a1 = 2, b1 = -4, c1 = 0;
        double scale = 3.5;
        assertThrows(NotEnoughPrecisionException.class, () -> { getOutput(a1, b1, c1); });
        assertThrows(NotEnoughPrecisionException.class, () -> { getOutput(a1 * scale, b1 * scale, c1 * scale); });
    }

     @AfterAll
    public static void tearDown() {
        long totalTime = System.nanoTime() - startTime;
        System.out.println("Total execution time for QuadraticMetamorphicTest: " + totalTime + " ns");
        
        String html = "<html><head><title>Total Execution Time Report</title></head>" +
                      "<body><h1>Total Execution Time for QuadraticMetamorphicTest</h1>" +
                      "<p>Total execution time: " + totalTime + " ns</p></body></html>";
        try (FileWriter writer = new FileWriter("total_execution_time_quadratic_metamorphic.html")) {
            writer.write(html);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}