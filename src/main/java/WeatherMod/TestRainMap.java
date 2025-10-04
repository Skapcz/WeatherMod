package WeatherMod;

public class TestRainMap {
    public static void main(String[] args) throws InterruptedException {
        WeatherMap weatherMap = new WeatherMap(40, 40, 12345L, 0.1); // fixní seed
        float globalWindX = 1.0f;
        float globalWindY = 0.0f;

        weatherMap.generate();
        for (int t = 0; t < 300; t++) { // 20 "tiků" simulace
            weatherMap.tick(globalWindX, globalWindY);

            System.out.println("Time = " + t);
            System.out.println(weatherMap);

            Thread.sleep(500); // půl vteřiny pauza, ať to vypadá jako animace
        }
    }
}
