package com.ashindigo.amex;

import spinnery.widget.api.Position;

import java.util.Arrays;

public class HelperMethods {

    public static float[] fillArray(float val, int size) {
        float[] vals = new float[size];
        Arrays.fill(vals, val);
        return vals;
    }

    public static boolean positionsXYSame(Position pos1, Position pos2) {
        if (pos1.getX() == pos2.getX()) {
            return pos1.getY() == pos2.getY();
        }
        return false;
    }
}
