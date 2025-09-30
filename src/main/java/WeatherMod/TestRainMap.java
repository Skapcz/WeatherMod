package WeatherMod;

public class TestRainMap {
    public static void main(String[] args) throws InterruptedException {
        WeatherMap weatherMap = new WeatherMap(40, 40, 12345L, 0.05); // fixní seed
        double globalWindX = 1.0;
        double globalWindY = 0.0;

        for (int t = 0; t < 20; t++) { // 20 "tiků" simulace
            weatherMap.generate(t, globalWindX, globalWindY);

            System.out.println("Time = " + t);
            System.out.println(weatherMap);

            Thread.sleep(500); // půl vteřiny pauza, ať to vypadá jako animace
        }
    }
}
