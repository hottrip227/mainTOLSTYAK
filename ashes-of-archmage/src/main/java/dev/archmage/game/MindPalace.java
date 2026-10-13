package dev.archmage.game;

import dev.archmage.ArchmageMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.Relative;
import java.util.Set;

public final class MindPalace {
    public static final ResourceKey<Level> LEVEL = ResourceKey.create(Registries.DIMENSION,Identifier.fromNamespaceAndPath(ArchmageMod.MOD_ID,"mind_palace"));
    public static final BlockPos ALTAR=new BlockPos(0,160,0);
    private MindPalace() {}
    public static boolean isSolo(MinecraftServer server) {
        return !server.isDedicatedServer() && !server.isPublished() && server.getPlayerCount()==1;
    }
    public static boolean isInside(ServerPlayer player) { return player.level().dimension().equals(LEVEL); }
    public static boolean isActive(MinecraftServer server) {
        return isSolo(server) && server.getPlayerList().getPlayers().stream().anyMatch(MindPalace::isInside);
    }
    public static boolean freeze(ServerLevel level) { return !level.dimension().equals(LEVEL) && isActive(level.getServer()); }
    public static boolean toggle(ServerPlayer player) {
        if (isInside(player)) return leave(player);
        var server=player.level().getServer();
        if (!isSolo(server)) { player.sendSystemMessage(Component.translatable("archmage.solo_only")); return false; }
        if (player.isPassenger() || player.isSleeping()) { player.sendSystemMessage(Component.translatable("archmage.leave_vehicle")); return false; }
        var target=server.getLevel(LEVEL);
        if (target==null) { player.sendSystemMessage(Component.translatable("archmage.missing_dimension")); return false; }
        buildTemple(target);
        AltarHints.ensure(target);
        var anchor=new CompoundTag();
        anchor.putString("dimension",player.level().dimension().identifier().toString());
        anchor.putDouble("x",player.getX()); anchor.putDouble("y",player.getY()); anchor.putDouble("z",player.getZ());
        anchor.putFloat("yaw",player.getYRot()); anchor.putFloat("pitch",player.getXRot());
        PlayerMagicData.root(player).put("return_anchor",anchor);
        boolean moved=player.teleportTo(target,0.5,160.1,7.5,Set.<Relative>of(),180,0,true);
        if (!moved) { PlayerMagicData.root(player).remove("return_anchor"); return false; }
        player.resetFallDistance();
        player.sendSystemMessage(Component.translatable("archmage.palace_enter"));
        return true;
    }
    public static boolean leave(ServerPlayer player) {
        if (!isInside(player)) return false;
        var server=player.level().getServer(); var root=PlayerMagicData.root(player);
        var anchor=root.getCompoundOrEmpty("return_anchor");
        Identifier id=Identifier.tryParse(anchor.getStringOr("dimension","minecraft:overworld"));
        ServerLevel target=id==null?null:server.getLevel(ResourceKey.create(Registries.DIMENSION,id));
        if (target==null || target.dimension().equals(LEVEL)) target=server.overworld();
        double x=anchor.getDoubleOr("x",0.5), y=anchor.getDoubleOr("y",100), z=anchor.getDoubleOr("z",0.5);
        if (!Double.isFinite(x)||!Double.isFinite(y)||!Double.isFinite(z)) { x=0.5;y=100;z=0.5; }
        boolean moved=player.teleportTo(target,x,y,z,Set.<Relative>of(),anchor.getFloatOr("yaw",0),anchor.getFloatOr("pitch",0),true);
        if (moved) { root.remove("return_anchor"); player.resetFallDistance(); player.sendSystemMessage(Component.translatable("archmage.palace_exit")); }
        return moved;
    }
    public static void rescueFromVoid(ServerPlayer p) {
        if (isInside(p) && p.getY()<145) { p.teleportTo(0.5,160.1,7.5); p.resetFallDistance(); }
    }
    public static void buildTemple(ServerLevel level) {
        if (level.getBlockState(ALTAR).is(Blocks.CHISELED_QUARTZ_BLOCK)) return;
        for(int x=-12;x<=12;x++) for(int z=-12;z<=12;z++) {
            double r=Math.sqrt(x*x+z*z);
            if(r<=12) {
                level.setBlock(new BlockPos(x,158,z),Blocks.OBSIDIAN.defaultBlockState(),2);
                var floor=r>10.5?Blocks.POLISHED_BLACKSTONE:((x==0||z==0)&&r<9?Blocks.GOLD_BLOCK:Blocks.QUARTZ_BRICKS);
                level.setBlock(new BlockPos(x,159,z),floor.defaultBlockState(),2);
            }
            if(r<8) level.setBlock(new BlockPos(x,157,z),Blocks.CRYING_OBSIDIAN.defaultBlockState(),2);
        }
        int[][] pillars={{-8,-8,7},{8,-8,10},{-8,8,5},{8,8,8}};
        for(var a:pillars) for(int h=0;h<a[2];h++) level.setBlock(new BlockPos(a[0],160+h,a[1]),Blocks.QUARTZ_PILLAR.defaultBlockState(),2);
        level.setBlock(ALTAR,Blocks.CHISELED_QUARTZ_BLOCK.defaultBlockState(),2);
        level.setBlock(ALTAR.above(),Blocks.AMETHYST_CLUSTER.defaultBlockState(),2);
        for(int side:new int[]{-1,1}) {
            int cx=side*19, cz=side*5;
            for(int x=-3;x<=3;x++) for(int z=-3;z<=3;z++) if(x*x+z*z<=12) {
                level.setBlock(new BlockPos(cx+x,158,cz+z),Blocks.OBSIDIAN.defaultBlockState(),2);
                level.setBlock(new BlockPos(cx+x,159,cz+z),Blocks.QUARTZ_BRICKS.defaultBlockState(),2);
            }
            for(int x=11;x<=18;x++) for(int z=-1;z<=1;z++) level.setBlock(new BlockPos(side*x,159,cz+z),Blocks.POLISHED_BLACKSTONE_BRICKS.defaultBlockState(),2);
            level.setBlock(new BlockPos(cx,160,cz),Blocks.BUDDING_AMETHYST.defaultBlockState(),2);
        }
    }
}
