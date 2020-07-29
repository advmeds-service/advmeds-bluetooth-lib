package com.example.myapplication;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        String test = "DDCSZY059971453330$";

        String result = test.substring(7, 10);

        System.out.println(result);
        System.out.println("" + (1.0-0.9));

        BigDecimal bd = new BigDecimal(result);
        BigDecimal bd2 = new BigDecimal("0.18");

        System.out.println("" + Math.round(bd.multiply(bd2).doubleValue()));

        assertEquals(4, 2 + 2);
    }
}