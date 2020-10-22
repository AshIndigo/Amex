package com.ashindigo.amex;

import java.util.Arrays;

public class Helpers {

    public static int[] fillArray(int[] array, int value) {
        Arrays.fill(array, value);
        return array;
    }

    public static int[] fillArray(int length, int value) {
        int[] array = new int[length];
        Arrays.fill(array, value);
        return array;
    }
}
