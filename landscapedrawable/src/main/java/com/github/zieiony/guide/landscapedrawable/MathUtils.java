package com.github.zieiony.guide.landscapedrawable;

import java.util.Random;

final class MathUtils {

    private MathUtils() {
    }

    public static float constrain(float amount, float low, float high) {
        return amount < low ? low : (amount > high ? high : amount);
    }

    public static float dist(float x1, float y1, float x2, float y2) {
        final float x = (x2 - x1);
        final float y = (y2 - y1);
        return (float) Math.hypot(x, y);
    }

    public static float lerp(float start, float stop, float amount) {
        return start + (stop - start) * amount;
    }

    // TODO: maxStart and maxStop were switched
    public static float map(float minStart, float minStop, float maxStart, float maxStop, float value) {
        return maxStart + (maxStop - maxStart) * ((value - minStart) / (minStop - minStart));
    }

    private static Random random = new Random();

    public static float randomSize(float avgValue) {
        return random.nextFloat() * avgValue + avgValue / 2;
    }

    public static float randomForce(float maxValue) {
        return random.nextFloat() * maxValue * 2 - maxValue;
    }

}