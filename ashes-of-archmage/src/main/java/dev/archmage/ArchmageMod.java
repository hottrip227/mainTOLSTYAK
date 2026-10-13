package dev.archmage;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

/** Single-player prototype entry point. */
@Mod(ArchmageMod.MOD_ID)
public final class ArchmageMod {
    public static final String MOD_ID = "ashes_of_archmage";
    private static final Logger LOGGER = LogUtils.getLogger();

    public ArchmageMod(FMLJavaModLoadingContext context) {
        dev.archmage.game.ArchmageItems.register(context.getModBusGroup());
        dev.archmage.game.ArchmageGameplay.register();
        dev.archmage.debug.ArchmageGameTests.register(context.getModBusGroup());
        FMLCommonSetupEvent.getBus(context.getModBusGroup()).addListener(this::onCommonSetup);
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Ashes of Archmage 0.3.1: Quas sphere and archmage appearance prototype loaded.");
    }
}
