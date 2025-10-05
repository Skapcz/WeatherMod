package WeatherMod;



public class TestRainMap {
    public static void main(String[] args) throws InterruptedException {
        WeatherMap weatherMap = new WeatherMap(30, 30, 12345L, 0.0001); // fixní seed
        float globalWindX = 0.0f;
        float globalWindY = 0.01f;

        weatherMap.generate();
        for (int t = 0; t < 100; t++){
            weatherMap.tick(globalWindX, globalWindY);

            System.out.println("Time = " + t);
            System.out.println(weatherMap);

            Thread.sleep(500); // půl vteřiny pauza, ať to vypadá jako animace
        }
    }
}
