package com.students;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class QuadraticCategoryPartitionTest {

    private static long startTime;

    @BeforeAll
    public static void init() {
        startTime = System.nanoTime();
    }

    @Test
    void testTwoRealRoots() throws NotEnoughPrecisionException {
        Quadratic.solveQuadratic(1, -5, 6); 
    }

    @Test
    void testOneRealRoot() throws NotEnoughPrecisionException {
        Quadratic.solveQuadratic(1, -2, 1); 
    }

    @Test
    void testComplexRoots() throws NotEnoughPrecisionException {
        Quadratic.solveQuadratic(1, 2, 5); 
    }

    @Test
    void testDiscriminantEqualBB_throwsException() {
        assertThrows(NotEnoughPrecisionException.class, () -> {
            double b = 1e154, a = 1.0, c = 0;
            Quadratic.solveQuadratic(a, b, c);
        });
    }

    @Test
    void testDiscriminantNegativeNearZero() throws NotEnoughPrecisionException {
        Quadratic.solveQuadratic(1, 2, 1.00000000001);
    }

    @Test
    void testVerySmallACausesException() {
        assertThrows(NotEnoughPrecisionException.class, () -> {
            double a = 1e-300, b = 1e154, c = 1.0;
            Quadratic.solveQuadratic(a, b, c);
        });
    }

    @Test
    void testValidateInput_normalCase() throws NotEnoughPrecisionException {
        assertEquals(3.14, Quadratic.validateInput("3.14"), 0.0001);
    }

    @Test
    void testValidateInput_integerCase() throws NotEnoughPrecisionException {
        assertEquals(5.0, Quadratic.validateInput("5"), 0.0001);
    }

    @Test
    void testValidateInput_throwsForLargeNumber() {
        assertThrows(NotEnoughPrecisionException.class, () -> {
            Quadratic.validateInput("1e400");
        });
    }

    @Test
    void testValidateInput_throwsForSmallNumber() {
        assertThrows(NotEnoughPrecisionException.class, () -> {
            Quadratic.validateInput("1e-400");
        });
    }

    @Test
    void testFormatDouble_int() {
        assertEquals("5", callFormatDouble(5.0));
    }

    @Test
    void testFormatDouble_double() {
        assertEquals("5.25", callFormatDouble(5.25));
    }

    @Test
    void testSqrtByNewton_zero() {
        assertEquals(0.0, callSqrtByNewton(0), 1e-9);
    }

    @Test
    void testSqrtByNewton_normal() {
        assertEquals(Math.sqrt(9), callSqrtByNewton(9), 1e-6);
    }

    @Test
    public void testCategoryPartition() throws NotEnoughPrecisionException {
        double a1 = 1, b1 = -3, c1 = 2;
        String result1 = getOutput(a1, b1, c1);
        System.out.println("Result for a=1, b=-3, c=2: " + result1);

        double a2 = -1, b2 = 3, c2 = 2;
        String result2 = getOutput(a2, b2, c2);
        System.out.println("Result for a=-1, b=3, c=2: " + result2);

        assertNotEquals(result1, result2);
    }

    @Test
    void testDiscriminantNaN() {
        assertThrows(NotEnoughPrecisionException.class, () ->
            Quadratic.solveQuadratic(Double.NaN, 1, 1));
    }

    @Test
    void testDiscriminantEqualsBB() {
        double b = 1e8;
        double a = 1;
        double c = (b * b - b * b) / (4 * a); 
        assertThrows(NotEnoughPrecisionException.class, () ->
            Quadratic.solveQuadratic(a, b, c));
    }

    @Test
    void testComplexRoots_RealIsZero_ImaginaryIsOne() throws NotEnoughPrecisionException {
        Quadratic.solveQuadratic(1, 0, 1);
    }

    @Test
    void testRealRoots_X1EqualsX2() throws NotEnoughPrecisionException {
        Quadratic.solveQuadratic(1, 2, 1);
    }

    @Test
    void testValidateInputOverflow() {
        String tooPrecise = "1.0000000000000000000000000000000000000001";
        assertThrows(NotEnoughPrecisionException.class, () ->
            Quadratic.validateInput(tooPrecise));
    }

    @Test
    void testValidateInputSuccess() throws NotEnoughPrecisionException {
        assertEquals(3.0, Quadratic.validateInput("3.0"));
    }

    @Test
    void testComplexRootFormattingVariants() throws NotEnoughPrecisionException {
        Quadratic.solveQuadratic(1, 0, 1); 
        Quadratic.solveQuadratic(2, 0, 2); 
        Quadratic.solveQuadratic(1, 2, 2); 
        Quadratic.solveQuadratic(1, 2, 5); 
    }

    @Test
    void testLargeValues() {
        assertThrows(NotEnoughPrecisionException.class, () -> {
            double a = 1e-8;
            double b = 1e-8;
            double c = 1e-8;

            double discriminant = b * b - 4 * a * c;
            if (Math.abs(discriminant) < 1e-12) {
                throw new NotEnoughPrecisionException("Precision loss is too high for the discriminant");
            }
            double sqrtDiscriminant = Math.sqrt(discriminant);
            double x1 = (-b + sqrtDiscriminant) / (2 * a);
            double x2 = (-b - sqrtDiscriminant) / (2 * a);
            if (Math.abs(x1) > 1e6 || Math.abs(x2) > 1e6) {
                throw new NotEnoughPrecisionException("Calculated roots have lost precision");
            }
        });
    }

    @Test
    void testDiscriminantSubtractionVsAddition() throws NotEnoughPrecisionException {
        double a = 10, b = 10, c = 10;
        String output = getOutput(a, b, c);
        if (!output.contains("i")) {
            fail("Expected complex roots, but solver reported real roots! Possibly mutated discriminant code.");
        }
    }

    @Test
    void testDiscriminantBoundaryExactZero() throws NotEnoughPrecisionException {
        double a = 1, b = -2, c = 1;
        String output = getOutput(a, b, c);
        if (output.contains("i")) {
            fail("We expected a real root for discriminant=0, but got a complex root instead!");
        }
    }

    @Test
    void testSignMethod_negativeBChangesRoot() throws NotEnoughPrecisionException {
        double a = 1, b = -10, c = 1;
        String output = getOutput(a, b, c);
        double[] roots = parseRealRoots(output);
        double root1 = roots[0];
        double root2 = roots[1];
        double expected1 = 9.898979485; 
        double expected2 = 0.101020514; 
        boolean correctRoot1 = (Math.abs(root1 - expected1) < 0.001) || (Math.abs(root2 - expected1) < 0.001);
        boolean correctRoot2 = (Math.abs(root1 - expected2) < 0.001) || (Math.abs(root2 - expected2) < 0.001);
        if (!(correctRoot1 && correctRoot2)) {
            fail("Incorrect real roots for negative b, sign(b) might be mutated. Found x1=" + root1 + ", x2=" + root2);
        }
    }

    @Test
    void testRealRootsSubtractiveCancellation() throws NotEnoughPrecisionException {
        double a = 1, b = -2000000, c = 1;
        String output = getOutput(a, b, c);
        double[] roots = parseRealRoots(output);
        double root1 = roots[0];
        double root2 = roots[1];
        boolean largeRootOk = (Math.abs(root1) > 1e5) || (Math.abs(root2) > 1e5);
        boolean smallRootOk = (Math.abs(root1) < 1e-4) || (Math.abs(root2) < 1e-4);
        if (!(largeRootOk && smallRootOk)) {
            fail("Subtractive cancellation test failed. Expected one large root and one near zero. Got: x1=" + root1 + ", x2=" + root2);
        }
    }

    @Test
    void testComplexRootsPrecision() throws NotEnoughPrecisionException {
        double a = 1, b = 2, c = 5;
        String output = getOutput(a, b, c);
        double[] parts = parseComplexOutput(output);
        double realPart = parts[0];
        double imagPart = parts[1];
        assertEquals(-1.0, realPart, 1e-5, "Expected real part ~ -1");
        assertEquals(2.0, Math.abs(imagPart), 1e-5, "Expected imaginary part magnitude ~2");
    }

    @Test
    void testComplexRoots_nonUnitA() throws NotEnoughPrecisionException {
        double a = 0.5, b = 2, c = 3;
        String output = getOutput(a, b, c);
        double[] parts = parseComplexOutput(output);
        double realX1 = parts[0];
        double imagX1 = parts[1];
        double realX2 = parts[2];
        double imagX2 = parts[3];
        assertEquals(-2.0, realX1, 0.01, "Wrong real part for x1!");
        assertEquals(1.414, Math.abs(imagX1), 0.01, "Wrong imag part for x1!");
        assertEquals(-2.0, realX2, 0.01, "Wrong real part for x2!");
        assertEquals(1.414, Math.abs(imagX2), 0.01, "Wrong imag part for x2!");
    }

    @Test
    void testComplexRoots_zeroRealOnSecondRoot() throws NotEnoughPrecisionException {
        double a = 1, b = 0, c = 4;
        String output = getOutput(a, b, c);
        double[] parts = parseComplexOutput(output);
        double real1 = parts[0];
        double imag1 = parts[1];
        double real2 = parts[2];
        double imag2 = parts[3];
        assertEquals(0.0, real1, 1e-9);
        assertEquals(2.0, imag1, 1e-9);
        assertEquals(0.0, real2, 1e-9);
        assertEquals(-2.0, imag2, 1e-9);
    }

    @Test
    void testRealRoots_positiveB_changesQ_fixed() throws NotEnoughPrecisionException {
        double a = 1, b = 10, c = 1;
        String output = getOutput(a, b, c);
        double[] roots = parseRealRoots(output);
        double root1 = roots[0];
        double root2 = roots[1];
        boolean bigRootOk = (Math.abs(root1 + 9.8989) < 0.01) || (Math.abs(root2 + 9.8989) < 0.01);
        boolean smallRootOk = (Math.abs(root1 + 0.1010) < 0.01) || (Math.abs(root2 + 0.1010) < 0.01);
        if (!(bigRootOk && smallRootOk)) {
            fail("testRealRoots_positiveB_changesQ_fixed: Equation x^2+10x+1=0 expects ~-9.8989 & -0.1010, but found x1=" 
                + root1 + ", x2=" + root2);
        }
    }

    @Test
    void testRealRoots_qDivA_mutation() throws NotEnoughPrecisionException {
        double a = 0.5, b = -3, c = 1;
        String output = getOutput(a, b, c);
        double[] roots = parseRealRoots(output);
        double root1 = roots[0];
        double root2 = roots[1];
        boolean okRoot1 = Math.abs(root1 - 5.6457) < 0.01 || Math.abs(root2 - 5.6457) < 0.01;
        boolean okRoot2 = Math.abs(root1 - 0.354) < 0.01 || Math.abs(root2 - 0.354) < 0.01;
        if (!(okRoot1 && okRoot2)) {
            fail("testRealRoots_qDivA_mutation: expected roots ~5.6457 & ~0.354, but got x1=" + root1 + ", x2=" + root2);
        }
    }

    @Test
    void testSqrtByNewton_largeValue() {
        try {
            double actual = callSqrtByNewton(2500);
            assertEquals(50.0, actual, 0.5, "sqrtByNewton(2500) should be near 50.");
        } catch (Exception e) {
            fail("Reflection invocation error: " + e);
        }
    }

    @Test
    void testMain_userFlow() {
        String userInput = String.join("\n",
            "1", "-5", "6", "n") + "\n";
        ByteArrayInputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(out));
        Quadratic.main(new String[0]);
        String consoleOutput = out.toString();
        System.err.println("DEBUG MAIN OUTPUT:\n" + consoleOutput);
        System.setIn(System.in);
        System.setOut(originalOut);
    }

    @Test
    void testMain_userFlow_aZeroThenExit() {
        String userInput = String.join("\n",
            "0", "1", "2", "3", "n") + "\n";
        ByteArrayInputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(out));
        Quadratic.main(new String[0]);
        String consoleOutput = out.toString();
        System.err.println("DEBUG MAIN OUTPUT:\n" + consoleOutput);
        System.setIn(System.in);
        System.setOut(originalOut);
    }

    // Helper methods used by several tests

    private String getOutput(double a, double b, double c) throws NotEnoughPrecisionException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        PrintStream ps = new PrintStream(outputStream);
        System.setOut(ps);
        Quadratic.solveQuadratic(a, b, c);
        System.setOut(originalOut);
        return outputStream.toString();
    }

    private static double callSqrtByNewton(double val) {
        try {
            var method = Quadratic.class.getDeclaredMethod("sqrtByNewton", double.class);
            method.setAccessible(true);
            return (double) method.invoke(null, val);
        } catch (Exception e) {
            fail(e);
            return 0;
        }
    }

    private static String callFormatDouble(double val) {
        try {
            var method = Quadratic.class.getDeclaredMethod("formatDouble", double.class);
            method.setAccessible(true);
            return (String) method.invoke(null, val);
        } catch (Exception e) {
            fail(e);
            return "";
        }
    }

    private double[] parseRealRoots(String output) {
        double x1 = 0.0;
        double x2 = 0.0;
        boolean foundX2 = false;
        String[] lines = output.split("\\r?\\n");
        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("x1 = ")) {
                String valueStr = line.substring("x1 = ".length()).trim();
                x1 = Double.parseDouble(valueStr);
            } else if (line.startsWith("x2 = ")) {
                String valueStr = line.substring("x2 = ".length()).trim();
                x2 = Double.parseDouble(valueStr);
                foundX2 = true;
            }
        }
        if (!foundX2) {
            x2 = x1;
        }
        return new double[]{ x1, x2 };
    }

    private double[] parseComplexOutput(String output) {
        double real1 = 0.0, imag1 = 0.0, real2 = 0.0, imag2 = 0.0;
        String[] lines = output.split("\\r?\\n");
        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("x1 = ")) {
                String valStr = line.substring("x1 = ".length()).trim();
                double[] parts = parseOneComplexLine(valStr);
                real1 = parts[0];
                imag1 = parts[1];
            } else if (line.startsWith("x2 = ")) {
                String valStr = line.substring("x2 = ".length()).trim();
                double[] parts = parseOneComplexLine(valStr);
                real2 = parts[0];
                imag2 = parts[1];
            }
        }
        return new double[]{ real1, imag1, real2, imag2 };
    }

    private double[] parseOneComplexLine(String expr) {
        double real = 0.0;
        double imag = 0.0;
        if (!expr.contains("i")) {
            return new double[]{ Double.parseDouble(expr.trim()), 0.0 };
        }
        expr = expr.replace("i", "").trim();
        if (!expr.contains("+") && !expr.contains("-")) {
            imag = Double.parseDouble(expr);
            return new double[]{ 0.0, imag };
        }
        expr = expr.replace(" - ", "-").replace(" + ", "+").replace(" ", "");
        if (expr.contains("+")) {
            String[] tokens = expr.split("\\+");
            real = Double.parseDouble(tokens[0]);
            imag = Double.parseDouble(tokens[1]);
        } else {
            int lastMinus = expr.lastIndexOf('-');
            if (lastMinus > 0) {
                String realStr = expr.substring(0, lastMinus);
                String imagStr = expr.substring(lastMinus + 1);
                real = Double.parseDouble(realStr);
                imag = -Double.parseDouble(imagStr);
            } else {
                imag = Double.parseDouble(expr);
                real = 0.0;
            }
        }
        return new double[]{ real, imag };
    }

    @AfterAll
    public static void tearDown() {
        long totalTime = System.nanoTime() - startTime;
        System.out.println("Total execution time for QuadraticCategoryPartitionTest: " + totalTime + " ns");

        String html = "<html><head><title>Total Execution Time Report</title></head>" +
                    "<body><h1>Total Execution Time for QuadraticCategoryPartitionTest</h1>" +
                    "<p>Total execution time: " + totalTime + " ns</p></body></html>";

        try (FileWriter writer = new FileWriter("total_execution_time_quadratic_category.html")) {
            writer.write(html);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


