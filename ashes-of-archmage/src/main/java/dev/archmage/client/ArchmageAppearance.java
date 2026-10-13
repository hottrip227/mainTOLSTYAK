package dev.archmage.client;

import dev.archmage.ArchmageMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.ClientAsset;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.entity.player.PlayerSkin;

/** Local prototype appearance only. Does not change the user's account skin. */
public final class ArchmageAppearance {
    public static final Identifier SKIN_TEXTURE=Identifier.fromNamespaceAndPath(ArchmageMod.MOD_ID,"textures/entity/archmage.png");
    public static final Identifier OUTFIT_TEXTURE=Identifier.fromNamespaceAndPath(ArchmageMod.MOD_ID,"textures/entity/archmage_outfit.png");
    public static final PlayerSkin SKIN=new PlayerSkin(new ClientAsset.ResourceTexture(Identifier.fromNamespaceAndPath(ArchmageMod.MOD_ID,"archmage"),SKIN_TEXTURE),null,null,PlayerModelType.WIDE,false);
    private static boolean enabled=true;
    private ArchmageAppearance() {}
    public static boolean isEnabledFor(AbstractClientPlayer player) {
        var mc=Minecraft.getInstance();return enabled&&mc.hasSingleplayerServer()&&mc.player==player;
    }
    public static boolean isEnabledFor(int entityId) {
        var mc=Minecraft.getInstance();return enabled&&mc.hasSingleplayerServer()&&mc.player!=null&&mc.player.getId()==entityId;
    }
    public static void toggle() {
        enabled=!enabled;
        var mc=Minecraft.getInstance();
        if(mc.player!=null)mc.player.sendSystemMessage(Component.translatable(enabled?"archmage.look_on":"archmage.look_off"));
    }
}
