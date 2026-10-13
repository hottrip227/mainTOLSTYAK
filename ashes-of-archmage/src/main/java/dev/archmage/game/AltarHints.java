package dev.archmage.game;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.storage.TagValueInput;
import java.util.Map;
import java.util.HashMap;

/** Non-saved tutorial label; no duplicate holograms after reloading a world. */
public final class AltarHints {
    private static final Map<ServerLevel,Display.TextDisplay> LABELS=new HashMap<>();
    private AltarHints() {}
    public static void ensure(ServerLevel level) {
        var old=LABELS.get(level);if(old!=null&&!old.isRemoved())return;
        var label=new Display.TextDisplay(EntityTypes.TEXT_DISPLAY,level) {
            @Override public boolean shouldBeSaved() { return false; }
        };
        var tag=new CompoundTag();
        tag.put("text",ComponentSerialization.CODEC.encodeStart(NbtOps.INSTANCE,Component.translatable("archmage.altar_label").withStyle(ChatFormatting.AQUA)).getOrThrow());
        tag.putString("billboard","center");tag.putInt("line_width",230);tag.putInt("background",0x70140c22);tag.putBoolean("shadow",true);
        var light=new CompoundTag();light.putInt("block",15);light.putInt("sky",15);tag.put("brightness",light);
        label.load(TagValueInput.create(ProblemReporter.DISCARDING,level.registryAccess(),tag));
        label.setPos(0.5,162.8,0.5);level.addFreshEntity(label);LABELS.put(level,label);
    }
    public static void clear() { LABELS.values().forEach(l->{if(!l.isRemoved())l.discard();});LABELS.clear(); }
}
