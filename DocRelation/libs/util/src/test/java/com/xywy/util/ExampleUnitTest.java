package com.xywy.util;

import android.util.Log;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test_converter() throws Exception {
        int resu = Converter.StringToInt("123aab", 1);
        Log.d("converter", "resu = " + resu);
    }
}