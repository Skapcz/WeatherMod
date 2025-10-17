package com.github.WeatherMod.common.noise;

public class NoiseUtils {

    public static double fractalNoise (OpenSimplexNoise noise, int layers, double x, double y, double lacunarity, double gain, double warpStrength, double warpScale){
        double warpX = noise.eval(x * warpScale, y * warpScale) * warpStrength;
        double warpY = noise.eval(x * warpScale + 100, y * warpScale + 100) * warpStrength;

        double result = 0;
        double frequency = 1;
        double maxAmplitude = 0;
        double amplitude = 1.0;


        for (int i = 0; i < layers; i++){

            double offsetX = i * 37.17;
            double offsetY = i * 91.83;
            result += noise.eval((x + warpX + offsetX) * frequency, (y + warpY + offsetY) * frequency) * amplitude;
            frequency *= lacunarity;
            amplitude *= gain;
            maxAmplitude += amplitude;

        }
        return result / maxAmplitude;
    }
}
