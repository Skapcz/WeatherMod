package WeatherMod;

public class RainMap {
    private final int width;
    private final int height;
    private static final double scale = 0.1; // fixní hodnota místo configu
    private final long seed;
    private int[][] grid;

    // Konstruktor bere i scale
    public RainMap(int width, int height, long seed, double scale) {
        this.width = width;
        this.height = height;
        this.seed = seed;
        generate();
    }

    // Pokud chceš kompatibilitu se starým voláním, přidej přetížený konstruktor s defaultní scale
    public RainMap(int width, int height, long seed) {
        this(width, height, seed, 0.1); // default scale
    }

    // Vygeneruje mapu pomocí OpenSimplexNoise (předpokládáme, že třída OpenSimplexNoise existuje)
    public void generate() {
        OpenSimplexNoise noise = new OpenSimplexNoise(seed);
        grid = new int[width][height];


        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double value = noise.eval(x * this.scale, y * this.scale);

                // Normalizace z <-1,1> na <0,1>
                double normalized = (value + 1.0) / 2.0;

                // Threshold mapping (líp rozloží 0-4 než přímé škálování)
                int rainLevel;
                if (normalized < 0.6) {
                    rainLevel = 0;
                } else if (normalized < 0.75) {
                    rainLevel = 1;
                } else if (normalized < 0.85) {
                    rainLevel = 2;
                } else if (normalized < 0.95) {
                    rainLevel = 3;
                } else {
                    rainLevel = 4;
                }

                grid[x][y] = rainLevel;
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
