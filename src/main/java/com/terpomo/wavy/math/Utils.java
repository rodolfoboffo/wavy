package com.terpomo.wavy.math;

import java.util.stream.Stream;

public class Utils {

    public static int getNearestPowerOfTwo(int n) {
        int result = 1;
        while(result < n) result *= 2;
        return result;
    }

    public static Complex[] toArrayOfComplex(Float[] x) {
        Complex[] complexArray = Stream.of(x).map(Complex::new).toArray(Complex[]::new);
        return complexArray;
    }

    public static Float[] toArrayOfAbsValues(Complex[] x) {
        Float[] floatARray = Stream.of(x).map(Complex::abs).toArray(Float[]::new);
        return floatARray;
    }

}
