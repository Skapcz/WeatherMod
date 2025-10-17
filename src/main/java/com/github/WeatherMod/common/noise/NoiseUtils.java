package com.github.WeatherMod.common.noise;

import org.joml.Vector2d;

public class NoiseUtils {

    public static double fractalNoise(OpenSimplexNoise noise, int layers, double x, double y, double lacunarity, double gain) {
        double result = 0;
        double frequency = 1;
        double maxAmplitude = 0;
        double amplitude = 1.0;

        for (int i = 0; i < layers; i++) {
            result += noise.eval(x * frequency, y * frequency) * amplitude;
            frequency *= lacunarity;
            amplitude *= gain;
            maxAmplitude += amplitude;
        }

        return result / maxAmplitude;
    }


    public static Vector2d warpCoords(OpenSimplexNoise noise, Vector2d p, double strength, double scale) {

        double warpX = noise.eval(p.x * scale, p.y * scale + 13.5);
        double warpY = noise.eval(p.x * scale + 7.3, p.y * scale);

        // posuneme souřadnice podle síly warpu
        return new Vector2d(
                p.x + warpX * strength,
                p.y + warpY * strength
        );
    }
}
