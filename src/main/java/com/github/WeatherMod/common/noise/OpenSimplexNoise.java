package com.github.WeatherMod.common.noise;


public class OpenSimplexNoise {
    private static final int PERM_SIZE = 256;
    private short[] perm;

    public OpenSimplexNoise(long seed) {
        perm = new short[PERM_SIZE];
        java.util.Random rand = new java.util.Random(seed);
        for (int i = 0; i < PERM_SIZE; i++) {
            perm[i] = (short) i;
        }
        for (int i = 0; i < PERM_SIZE; i++) {
            int j = rand.nextInt(PERM_SIZE);
            short temp = perm[i];
            perm[i] = perm[j];
            perm[j] = temp;
        }
    }

    // 2D noise
    public double eval(double x, double y) {
        int xi = (int)Math.floor(x) & 255;
        int yi = (int)Math.floor(y) & 255;

        double xf = x - Math.floor(x);
        double yf = y - Math.floor(y);

        double u = fade(xf);
        double v = fade(yf);

        int aa = perm[(perm[xi] + yi) & 255];
        int ab = perm[(perm[xi] + yi + 1) & 255];
        int ba = perm[(perm[xi + 1] + yi) & 255];
        int bb = perm[(perm[xi + 1] + yi + 1) & 255];

        double x1 = lerp(grad(aa, xf, yf), grad(ba, xf - 1, yf), u);
        double x2 = lerp(grad(ab, xf, yf - 1), grad(bb, xf - 1, yf - 1), u);

        return lerp(x1, x2, v);
    }

    private double fade(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    private double lerp(double a, double b, double t) {
        return a + t * (b - a);
    }

    private double grad(int hash, double x, double y) {
        switch (hash & 3) {
            case 0: return  x + y;
            case 1: return -x + y;
            case 2: return  x - y;
            case 3: return -x - y;
            default: return 0;
        }
    }
}
