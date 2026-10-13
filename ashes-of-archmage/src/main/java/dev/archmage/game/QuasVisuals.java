package dev.archmage.game;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.TagValueInput;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** A real item-display mesh follows the player. Dust is only its trailing frost. */
public final class QuasVisuals {
    public static final String TAG="archmage_quas_visual";
    private static final Map<UUID,Display.ItemDisplay> ORBS=new HashMap<>();
    private QuasVisuals() {}
    public static void tick(ServerPlayer player,boolean unlocked) {
        if(!unlocked||!player.isAlive()||!MindPalace.isSolo(player.level().getServer())) { remove(player.getUUID());return; }
        var level=(ServerLevel)player.level();
        var orb=ORBS.get(player.getUUID());
        double angle=player.tickCount*0.055;
        double x=player.getX()+Math.cos(angle)*1.6;
        double y=player.getY()+1.52+Math.sin(player.tickCount*0.085)*0.08;
        double z=player.getZ()+Math.sin(angle)*1.6;
        if(orb==null||orb.isRemoved()||orb.level()!=level) {
            remove(player.getUUID());
            orb=new Display.ItemDisplay(EntityTypes.ITEM_DISPLAY,level) {
                @Override public boolean shouldBeSaved() { return false; }
            };
            var data=new CompoundTag();
            data.putInt("teleport_duration",2);
            data.putString("item_display","fixed");
            data.putFloat("view_range",0.5f);
            data.putFloat("width",1f);data.putFloat("height",1f);
            var brightness=new CompoundTag();brightness.putInt("block",15);brightness.putInt("sky",15);data.put("brightness",brightness);
            orb.load(TagValueInput.create(ProblemReporter.DISCARDING,level.registryAccess(),data));
            orb.getSlot(0).set(new ItemStack(ArchmageItems.QUAS_ORB.get()));
            orb.setNoGravity(true);orb.addTag(TAG);orb.setPos(x,y,z);
            level.addFreshEntity(orb);ORBS.put(player.getUUID(),orb);
        }
        orb.setPos(x,y,z);orb.setYRot(player.tickCount*1.5f);
        // Its frost trail is client-local so first-person can hide it without hiding spell effects.
    }
    public static Display.ItemDisplay current(ServerPlayer player) { return ORBS.get(player.getUUID()); }
    public static void remove(UUID owner) { var orb=ORBS.remove(owner);if(orb!=null&&!orb.isRemoved())orb.discard(); }
    public static void clear() { ORBS.values().forEach(o->{if(!o.isRemoved())o.discard();});ORBS.clear(); }
}
