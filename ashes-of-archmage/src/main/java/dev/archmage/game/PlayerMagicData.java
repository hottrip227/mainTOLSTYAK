package dev.archmage.game;

import dev.archmage.magic.MageProgress;
import dev.archmage.magic.Sphere;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.EnumMap;

/** Stores non-material progress under Forge's respawn-persistent player tag. */
public final class PlayerMagicData {
    private PlayerMagicData() {}
    public static CompoundTag root(ServerPlayer player) {
        var persistent = player.getPersistentData();
        var root = persistent.getCompoundOrEmpty(ServerPlayer.PERSISTED_NBT_TAG);
        persistent.put(ServerPlayer.PERSISTED_NBT_TAG, root);
        var ours = root.getCompoundOrEmpty("ashes_of_archmage");
        root.put("ashes_of_archmage", ours);
        return ours;
    }
    public static MageProgress load(ServerPlayer p) { return decode(root(p).getCompoundOrEmpty("magic")); }
    public static void save(ServerPlayer p, MageProgress state) { root(p).put("magic", encode(state)); }

    public static CompoundTag encode(MageProgress state) {
        var out = new CompoundTag();
        out.putInt("schema", 1);
        var ranks = new CompoundTag();
        state.sphereRanks().forEach((s,r) -> ranks.putInt(s.id(), r));
        out.put("spheres", ranks);
        var essences = new CompoundTag();
        state.essenceBalances().forEach(essences::putInt);
        out.put("essences", essences);
        var learned = new CompoundTag();
        state.learnedSpells().forEach(id -> learned.putBoolean(id, true));
        out.put("learned", learned);
        out.putInt("slot_count", state.slots());
        var slots = new CompoundTag();
        for (int i=0; i<state.slots(); i++) if (state.equipped(i)!=null) slots.putString(Integer.toString(i),state.equipped(i));
        out.put("slots", slots);
        return out;
    }
    public static MageProgress decode(CompoundTag in) {
        var ranks = new EnumMap<Sphere,Integer>(Sphere.class);
        var rankTag = in.getCompoundOrEmpty("spheres");
        for (var s : Sphere.values()) { int rank = rankTag.getIntOr(s.id(),0); if (rank>0) ranks.put(s,rank); }
        var essences = new HashMap<String,Integer>();
        var essenceTag = in.getCompoundOrEmpty("essences");
        for (var id:essenceTag.keySet()) { int n=essenceTag.getIntOr(id,0); if (!id.isBlank() && n>0) essences.put(id,n); }
        var learned = new HashSet<String>();
        var learnedTag = in.getCompoundOrEmpty("learned");
        for (var id:learnedTag.keySet()) if (!id.isBlank() && learnedTag.getBooleanOr(id,false)) learned.add(id);
        int count=Math.clamp(in.getIntOr("slot_count",10),1,MageProgress.MAX_LOADOUT_SLOTS);
        var slots=new ArrayList<String>(); var slotTag=in.getCompoundOrEmpty("slots");
        for (int i=0;i<count;i++) { var id=slotTag.getStringOr(Integer.toString(i),""); slots.add(learned.contains(id)?id:null); }
        return MageProgress.fromSnapshot(new MageProgress.Snapshot(ranks,essences,learned,slots));
    }
}
