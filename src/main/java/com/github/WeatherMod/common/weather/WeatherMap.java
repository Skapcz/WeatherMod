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
    int numberOfLayers = 3;
    float minScale = 0.001f;  // konec rozsahu
    float maxScale = 0.1f;   // začátek rozsahu

    private CloudLayer[] layer = new CloudLayer[numberOfLayers];











    private float difuze = 0.05f;



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


        for (int i = 0; i < numberOfLayers; i++) {
            layer[i] = new CloudLayer();
            layer[i].map = new float[width][height];
            layer[i].scale = (float) Math.pow(10, -i)*0.05f;
            layer[i].windX = (float) scale  * 0.1f *(float)(Math.random()*i);
            layer[i].windY = 0.01f;
            layer[i].weight = 0.5f + 0.25f*i;
            layer[i].offsetX = 0f; // začíná na nule
            layer[i].offsetY = 0f;

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

    public float[][] getCloudinessMap() {
        float[][] data = new float[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                data[x][y] = grid[x][y].cloudiness;
            }
        }
        return data;
    }



// převod gridu na textovou reprezentaci (ASCII mapu)
//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        for (int y = 0; y < height; y++) {
//            for (int x = 0; x < width; x++) {
//                WeatherCell cell = grid[x][y];
//                int colorNumb = Math.round(cell.cloudiness*10 + 32);
//
//                final String Color = "\u001b["+colorNumb;
//                String symbol;
//
//                if (cell.precipitation > 0.8f) {
//                    symbol = "\u001b[0;41m  \u001B[0m"; // liják
//                } else if (cell.precipitation > 0.6f) {
//                    symbol = "\u001b[0;43m  \u001B[0m"; // silný déšť
//                } else if (cell.precipitation > 0.3f) {
//                    symbol = "\u001b[0;42m  \u001B[0m"; // déšť
//                } else if (cell.precipitation > 0.1f) {
//                    symbol = "\u001b[0;44m  \u001B[0m"; // mrholení
//                } else {
//                    // bez deště -> oblačnost
//                    if (cell.cloudiness > 0.7f) {
//                        symbol = "\u001b[0;46m  \u001B[0m"; // zataženo
//                    } else if (cell.cloudiness > 0.4f) {
//                        symbol = "\u001b[0;47m  \u001B[0m"; // částečně oblačno
//                    } else {
//                        symbol = "\u001b[0;40m  \u001B[0m"; // jasno
//                    }
//                }
//
//                sb.append(symbol).append(" ");
//            }
//            sb.append("\n");
//        }
//        return sb.toString();
//    }

}
