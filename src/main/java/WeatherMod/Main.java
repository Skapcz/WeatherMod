package WeatherMod;

import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Main.MODID)
public class Main {
    public static final String MODID = "weather_mod";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Main() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();


        WeatherMap weatherMap = new WeatherMap(20, 20, System.currentTimeMillis());
        LOGGER.info("\n" + weatherMap);
    }

}
