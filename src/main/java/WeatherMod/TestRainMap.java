package WeatherMod;

public class TestRainMap {
    public static void main(String[] args) {
        long seed = System.currentTimeMillis();
        RainMap rainMap = new RainMap(100, 100, seed);

        System.out.println("Seed: " + seed);
        System.out.println(rainMap);
    }
}
