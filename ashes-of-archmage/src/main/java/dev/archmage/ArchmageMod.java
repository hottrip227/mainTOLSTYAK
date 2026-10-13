package dev.archmage;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

/** Bootstrap only: no campaign, spells, or dimension are implemented yet. */
@Mod(ArchmageMod.MOD_ID)
public final class ArchmageMod {
    public static final String MOD_ID = "ashes_of_archmage";
    private static final Logger LOGGER = LogUtils.getLogger();

    public ArchmageMod(FMLJavaModLoadingContext context) {
        FMLCommonSetupEvent.getBus(context.getModBusGroup()).addListener(this::onCommonSetup);
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Ashes of Archmage 0.1.0: bootstrap loaded; gameplay is not implemented yet.");
    }
}
