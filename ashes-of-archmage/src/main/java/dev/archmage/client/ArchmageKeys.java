package dev.archmage.client;

import dev.archmage.ArchmageMod;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid=ArchmageMod.MOD_ID,value=Dist.CLIENT)
public final class ArchmageKeys {
    private static final KeyMapping.Category CATEGORY=KeyMapping.Category.register(Identifier.fromNamespaceAndPath(ArchmageMod.MOD_ID,"magic"));
    private static final KeyMapping PALACE=new KeyMapping("key.archmage.palace",InputConstants.Type.KEYBOARD,InputConstants.KEY_M,CATEGORY);
    private static final KeyMapping PULSE=new KeyMapping("key.archmage.pulse",InputConstants.Type.KEYBOARD,InputConstants.KEY_R,CATEGORY);
    private static final KeyMapping LOOK=new KeyMapping("key.archmage.look",InputConstants.Type.KEYBOARD,InputConstants.KEY_V,CATEGORY);
    @SubscribeEvent public static void layers(EntityRenderersEvent.AddLayers event) {
        for(var type:event.getModelTypes()) {
            AvatarRenderer<AbstractClientPlayer> renderer=event.getPlayerRenderer(type);
            if(renderer!=null)renderer.addLayer(new ArchmageOutfitLayer(renderer));
        }
    }
    @SubscribeEvent public static void keys(RegisterKeyMappingsEvent e) {e.register(PALACE);e.register(PULSE);e.register(LOOK);}
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent.Post e) {
        QuasClientVisuals.tick();
        var mc=Minecraft.getInstance();
        if(mc.player==null||mc.level==null||mc.gui.screen()!=null) return;
        while(PALACE.consumeClick()) mc.player.connection.sendCommand("archmage palace");
        while(LOOK.consumeClick()) ArchmageAppearance.toggle();
        while(PULSE.consumeClick()) mc.player.connection.sendCommand("archmage pulse");
    }
}
