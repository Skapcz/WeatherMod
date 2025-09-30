package WeatherMod;

public class WeatherMap {
    private final int width;
    private final int height;
    private final double scale;
    private final long seed;
    private WeatherCell[][] grid;

    public WeatherMap(int width, int height, long seed, double scale) {
        this.width = width;
        this.height = height;
        this.scale = scale;
        this.seed = seed;
        grid = new WeatherCell[width][height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid[x][y] = new WeatherCell();
            }
        }
    }

    public WeatherMap(int width, int height, long seed) {
        this(width, height, seed, 0.1);
    }

    public void generate(int time, double windX, double windY) {
        OpenSimplexNoise noise = new OpenSimplexNoise(seed);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                WeatherCell cell = grid[x][y];

                double nx = x * scale + windX * time;
                double ny = y * scale + windY * time;
                double value = noise.eval(nx, ny);
                double normalized = (value + 1.0) / 2.0;

                // Update stavu buňky
                cell.cloudiness = normalized;
                cell.humidity = 0.5 + normalized * 0.5;
                if (cell.cloudiness > 0.6 && cell.humidity > 0.6) {
                    cell.precipitation = normalized;
                } else {
                    cell.precipitation = 0;
                }

                // Přenos vlhkosti podle větru
                double offsetX = x + windX * time;
                double offsetY = y + windY * time;

                int targetX = (int) Math.round(offsetX);
                int targetY = (int) Math.round(offsetY);


                if (targetX >= 0 && targetX < width && targetY >= 0 && targetY < height) {
                    WeatherCell target = grid[targetX][targetY];
                    target.cloudiness += cell.cloudiness * 0.2;
                    target.humidity += cell.humidity * 0.2;
                }
            }
        }
    }

    public WeatherCell[][] getGrid() {
        return grid;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                WeatherCell cell = grid[x][y];
                String symbol;

                // převod precipitation -> symbol
                if (cell.precipitation == 0) symbol = "⚪";
                else if (cell.precipitation < 0.3) symbol = "🔵";
                else if (cell.precipitation < 0.6) symbol = "🟢";
                else if (cell.precipitation < 0.9) symbol = "🟡";
                else symbol = "🔴";

                sb.append(symbol).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
