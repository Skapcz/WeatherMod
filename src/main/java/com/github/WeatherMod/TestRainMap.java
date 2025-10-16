package com.github.WeatherMod;

import com.github.WeatherMod.common.weather.WeatherMap;
import com.github.WeatherMod.common.noise.NoiseViewer;

public class TestRainMap {
    static int width = 512;
    static int height = 512;
    public static void main(String[] args) throws InterruptedException {
        WeatherMap weatherMap = new WeatherMap(width, height, 12345L, 0.02f); // seed a scale

        float globalWindX = 0.01f;
        float globalWindY = 0.01f;

        // vytvoření vieweru (jedno okno)
        NoiseViewer viewer = new NoiseViewer(weatherMap.getGrid().length, weatherMap.getGrid()[0].length, 1);

        weatherMap.generate();


        float[][] data = new float[width][height]; // jen jednou před smyčkou

        for (int t = 0; t < 10000; t++) {
            weatherMap.tick(globalWindX, globalWindY, t);



            for (int x = 0; x < weatherMap.getGrid().length; x++) {
                for (int y = 0; y < weatherMap.getGrid()[0].length; y++) {
                    data[x][y] = weatherMap.getGrid()[x][y].cloudiness;
                }
            }

            // aktualizace okna
            viewer.update(data);


            Thread.sleep(100);
        }
    }
}
