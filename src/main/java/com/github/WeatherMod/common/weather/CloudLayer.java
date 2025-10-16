package com.github.WeatherMod.common.weather;

public class CloudLayer {
    public float scale;
    public float weight;
    public float windX;
    public float windY;
    public float offsetX;
    public float offsetY;
    float[][] map;

    public CloudLayer() {
        this.scale = 0;
        this.weight = 0;
        this.windX = 0;
        this.windY = 0;
        this.offsetX = 0;
        this.offsetY = 0;

    }
}
