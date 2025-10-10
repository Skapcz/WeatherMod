package com.github.WeatherMod;


import com.github.WeatherMod.common.weather.WeatherMap;

public class TestRainMap {
    public static void main(String[] args) throws InterruptedException {
        WeatherMap weatherMap = new WeatherMap(30, 30, 12345L, 0.00001); // fixní seed
        float globalWindX = 0.01f;
        float globalWindY = 0.001f;

        weatherMap.generate();
        for (int t = 0; t < 100; t++){
            weatherMap.tick(globalWindX, globalWindY);

            System.out.println("Time = " + t);
            System.out.println(weatherMap);

            Thread.sleep(500); // půl vteřiny pauza, ať to vypadá jako animace
        }
    }
}
