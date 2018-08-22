package com.xiiilab.calculator.core;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by XIII-th on 22.08.2018
 */
public class CalculatorTest {

    private static final float E = 0.000001F;

    @Test
    public void calculate_sample1() {
        assertEquals(0.1F, new Calculator().calculate(Samples.SAMPLE_1_TOKEN_EXPRESSION.get()), E);
    }

    @Test
    public void calculate_sample2() {
        assertEquals(16F, new Calculator().calculate(Samples.SAMPLE_2_TOKEN_EXPRESSION.get()), E);
    }

    @Test
    public void calculate_sample3() {
        assertEquals(6F, new Calculator().calculate(Samples.SAMPLE_3_TOKEN_EXPRESSION.get()), E);
    }

    @Test
    public void calculate_sample4() {
        assertEquals(-18F, new Calculator().calculate(Samples.SAMPLE_4_TOKEN_EXPRESSION.get()), E);
    }
}