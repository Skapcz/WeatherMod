package com.github.WeatherMod.common.weather;

import com.github.WeatherMod.common.noise.NoiseUtils;
import com.github.WeatherMod.common.noise.OpenSimplexNoise;

import java.util.Random;

public class WeatherMap {
    private final int width;          // šířka mapy
    private final int height;         // výška mapy
    private final double scale;       // určuje "zoom" do noise mapy
    private final long seed;          // náhodný seed pro noise
    private final OpenSimplexNoise noise;

    private WeatherCell[][] grid;     // 2D pole buněk s počasím
    float globalOffsetX;
    float globalOffsetY;






    public WeatherMap(int width, int height, long seed, double scale) {
        this.width = width;
        this.height = height;
        this.scale = scale;
        this.seed = seed;
        noise = new OpenSimplexNoise(seed);


        // vytvoření prázdných buněk (každá buňka má stav počasí)
        grid = new WeatherCell[width][height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid[x][y] = new WeatherCell();
            }
        }
    }

    // konstruktor s výchozím scale
    public WeatherMap(int width, int height, long seed) {
        this(width, height, seed, 1);
    }


    // aktualizace počasí pro daný čas a vítr
    public void generate() {



        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double nx = x * scale;
                double ny = y * scale;

                double value = NoiseUtils.fractalNoise(noise, 4,nx, ny,2.0, 0.5);
                double normalized = (value + 1.0) / 2.0;

                WeatherCell cell = grid[x][y];
                cell.humidity = (float)normalized;
                cell.cloudiness = (float)normalized;
                cell.precipitation = 0f;
                //nastavování
            }
        }
    }


    // vrací celý grid buněk
    public WeatherCell[][] getGrid() {
        return grid;
    }

    public void tick(float windX, float windY) {
        globalOffsetX += windX;
        globalOffsetY += windY;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                WeatherCell cell = grid[x][y];

                double nx = (x * scale)+ globalOffsetX;
                double ny = (y * scale)+ globalOffsetY;
                double value = NoiseUtils.fractalNoise(noise, 4, nx, ny,2.0, 0.5);

                double normalized = (value + 1.0) / 2.0;
                cell.cloudiness = (float)normalized;

            }
        }
    }
}
