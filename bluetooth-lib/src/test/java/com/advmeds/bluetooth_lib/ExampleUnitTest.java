package com.advmeds.bluetooth_lib;

import org.junit.Test;

import java.math.BigDecimal;

import timber.log.Timber;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {

        assertEquals(4, 2 + 2);
    }

    public String[] decode(byte[] receiveData) {
        String raw = new String(receiveData);

        Timber.d(raw);

        if (raw.length() != 19 || !raw.contains("DDCSZY")) {
            Timber.d("Raw data error");

            return null;
        }

        String result = raw.substring(6, 9);

        if(result.startsWith("0")) {
            result = result.substring(1);
        }

        BigDecimal bd = new BigDecimal(result);

        BigDecimal bd2 = new BigDecimal("1.8");

        long answer = Math.round(bd.multiply(bd2).doubleValue());

        Timber.d("answer = %s", answer);

        return new String[] {Long.toString(answer)};
    }
}