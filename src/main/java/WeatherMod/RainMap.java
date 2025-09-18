package WeatherMod;
import java.util.Random;

public class RainMap {
    private final int width;
    private final int height;
    private final long seed;
    private int[][] grid;

    public RainMap(int width, int height, long seed) {
        this.width = width;
        this.height = height;
        this.seed = seed;
        generate();
    }

    // vygeneruje mapu 0–4 náhodně
    public void generate() {
        Random random = new Random(seed);
        grid = new int[width][height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid[x][y] = random.nextInt(5); // hodnoty 0–4

            }
        }
    }

    public int[][] getGrid() {
        return grid;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                sb.append(grid[x][y]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
