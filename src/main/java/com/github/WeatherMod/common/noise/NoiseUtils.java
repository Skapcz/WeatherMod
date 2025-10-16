package com.github.WeatherMod.common.noise;

public class NoiseUtils {

    public static double fractalNoise (OpenSimplexNoise noise, int layers, double x, double y, double lacunarity, double gain, int time){
        double result = 0;
        double frequency = 1;
        double maxAmplitude = 0;
        double amplitude = 1.0;


        for (int i = 0; i < layers; i++){
            result += noise.eval(x * frequency, y * frequency, time * 0.01) * amplitude;



            frequency *= lacunarity;
            amplitude *= gain;
            maxAmplitude += amplitude;

        }
        return result / maxAmplitude;
    }
}
