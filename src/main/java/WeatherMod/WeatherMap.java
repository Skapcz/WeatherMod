package WeatherMod;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.sum;
import static java.lang.Math.floor;

public class WeatherMap {
    private final int width;          // šířka mapy
    private final int height;         // výška mapy
    private final double scale;       // určuje "zoom" do noise mapy
    private final long seed;          // náhodný seed pro noise
    private WeatherCell[][] grid;     // 2D pole buněk s počasím


    private float difuze = 0.05f;



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
    public void generate() {
        OpenSimplexNoise noise = new OpenSimplexNoise(seed);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                WeatherCell cell = grid[x][y];

                double nx = x * scale;
                double ny = y * scale;
                double value = noise.eval(nx, ny);
                double normalized = (value + 1.0) / 2.0;

                // inicializace počátečních hodnot, např.
                cell.humidity = (float) normalized;
                cell.cloudiness = 0f;
                cell.precipitation = 0f;
            }
        }


    }

    // vrací celý grid buněk
    public WeatherCell[][] getGrid() {
        return grid;
    }

    //projede celou mřížku a udělá základní operace
    public void tick(float windX, float windY) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                WeatherCell cell = grid[x][y];

                cell.humidity = cell.humidity * 0.1f;

                if (cell.humidity > 0.4f){
                    cell.cloudiness += 0.1f;

                }

                if (cell.cloudiness > 0.8f) {
                    cell.precipitation += 0.1f;
                }

                if (cell.precipitation > 0.6f) {

                }

                // omezení všech hodnot do 0 do 1
                cell.humidity = Math.max(0f, Math.min(1f, cell.humidity));
                cell.cloudiness = Math.max(0f, Math.min(1f, cell.cloudiness));
                cell.precipitation = Math.max(0f, Math.min(1f, cell.precipitation));






                // Difúze vlhkosti
                float sum = 0f;
                int count = 0;

                // nahoru
                if (y - 1 >= 0) {
                    sum += grid[x][y-1].humidity;
                    count++;
                }
                // dole
                if (y + 1 < height) {
                    sum += grid[x][y+1].humidity;
                    count++;
                }
                // vlevo
                if (x - 1 >= 0) {
                    sum += grid[x-1][y].humidity;
                    count++;
                }
                // vpravo
                if (x + 1 < width) {
                    sum += grid[x+1][y].humidity;
                    count++;
                }


                //posun mraků
                double newPosX = x + windX;
                double newPosY = y + windY;

                int baseX = (int)Math.floor(newPosX);
                int baseY = (int)Math.floor(newPosY);

                if (baseX < 0 || baseY < 0 || baseX+1 >= width || baseY+1 >= height) {
                    continue; // přeskoč, nebo nastav default
                }


                float fracX = (float) newPosX - (float)baseX;
                float fracY = (float) newPosY - (float)baseY;

                float v00 = grid[baseX][baseY].humidity;
                float v10 = grid[baseX+1][baseY].humidity;
                float v01 = grid[baseX][baseY+1].humidity;
                float v11 = grid[baseX+1][baseY+1].humidity;

                float interpolated =
                          v00 * (1-fracX)*(1-fracY)
                        + v10 * fracX*(1-fracY)
                        + v01 * (1-fracX)*fracY
                        + v11 * fracX*fracY;

                cell.humidity = interpolated;

                cell.offsetX = fracX;
                cell.offsetY = fracY;





                if (count > 0) {  // vždy kontroluj, aby nedošlo k dělení nulou
                    float prumer = sum / count;
                    cell.humidity = cell.humidity + difuze * (prumer - cell.humidity)
                            + (1-difuze) * (interpolated - cell.humidity);

                }

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
                if (cell.cloudiness < 0.25f) {
                    symbol = "⚪";
                } else if (cell.cloudiness < 0.8f) {
                    symbol = "🔵";
                } else if (cell.precipitation < 0.50f) {
                    symbol = "🟢";
                } else if (cell.precipitation < 0.95f) {
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
