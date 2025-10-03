package WeatherMod;

public class WeatherMap {
    private final int width;          // šířka mapy
    private final int height;         // výška mapy
    private final double scale;       // určuje "zoom" do noise mapy
    private final long seed;          // náhodný seed pro noise
    private WeatherCell[][] grid;     // 2D pole buněk s počasím

    public WeatherMap(int width, int height, long seed, double scale) {
        this.width = width;
        this.height = height;
        this.scale = scale;
        this.seed = seed;

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
    public void generate(int time, double windX, double windY) {
        OpenSimplexNoise noise = new OpenSimplexNoise(seed);

        // projdi všechny buňky mapy
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                WeatherCell cell = grid[x][y];

                // vypočítáme souřadnice pro noise
                double nx = (x + windX * time) * scale;
                double ny = (y + windY * time) * scale;

                // noise vrací hodnotu -1..1, převedeme na 0..1
                double value = noise.eval(nx, ny);
                double normalized = (value + 1.0) / 2.0;

                // nastavíme základní hodnoty buňky




                // určíme, kam by vítr odnesl část oblačnosti/vlhkosti
                int targetX = (int) Math.round(x + windX);
                int targetY = (int) Math.round(y + windY);

                // pokud cíl existuje v mapě, přeneseme část hodnot
                if (targetX >= 0 && targetX < width && targetY >= 0 && targetY < height) {
                    WeatherCell target = grid[targetX][targetY];
                    target.cloudiness += cell.cloudiness * 0.2;
                    target.humidity += cell.humidity * 0.2;
                }
            }
        }
    }

    // vrací celý grid buněk
    public WeatherCell[][] getGrid() {
        return grid;
    }

    //projede celou mřížku a udělá základní operace
    public void tick() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                WeatherCell cell = grid[x][y];

                if (cell.humidity > 0.6f){
                    cell.cloudiness += 0.002f;
                }
                if (cell.cloudiness > 0.8f){
                    cell.precipitation += 0.1f;
                }
                if (cell.precipitation > 0.6f) {
                    cell.humidity -= 0.05f;
                }
                // omezení všech hodnot do 0 do 1
                cell.humidity = Math.max(0f, Math.min(1f, cell.humidity));
                cell.cloudiness = Math.max(0f, Math.min(1f, cell.cloudiness));
                cell.precipitation = Math.max(0f, Math.min(1f, cell.precipitation));

            }
        }





    }


    // převod gridu na textovou reprezentaci (ASCII mapu)
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                WeatherCell cell = grid[x][y];
                String symbol;

                // zobrazení srážek podle intenzity
                if (cell.precipitation < 0.6) {
                    symbol = "⚪";
                } else if (cell.precipitation < 0.75) {
                    symbol = "🔵";
                } else if (cell.precipitation < 0.85) {
                    symbol = "🟢";
                } else if (cell.precipitation < 0.95) {
                    symbol = "🟡";
                } else {
                    symbol = "🔴";
                }

                sb.append(symbol).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
