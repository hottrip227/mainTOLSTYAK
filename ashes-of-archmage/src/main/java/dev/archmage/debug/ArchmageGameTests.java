package dev.archmage.debug;

import dev.archmage.game.MindPalace;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import java.util.function.Consumer;

/** Test functions are registered explicitly: 26.3 uses data-driven test instances. */
public final class ArchmageGameTests {
    public static void register(BusGroup group) {
        var tests=DeferredRegister.<Consumer<GameTestHelper>>create(Registries.TEST_FUNCTION,"ashes_of_archmage");
        tests.register("dimension_registration",()->ArchmageGameTests::dimensionAndCommandRegistration);
        tests.register("temple_build",()->ArchmageGameTests::templeBuildIsStable);
        tests.register("player_flow",()->ArchmageGameTests::playerFlowAndFrozenTime);
        tests.register(group);
    }
    public static void dimensionAndCommandRegistration(GameTestHelper helper) {
        var server=helper.getLevel().getServer();
        var palace=server.getLevel(MindPalace.LEVEL);
        helper.assertTrue(palace!=null,"Mind Palace dimension must load from data pack");
        helper.assertTrue(server.getCommands().getDispatcher().getRoot().getChild("archmage")!=null,"archmage commands must register");
        helper.assertTrue(!MindPalace.freeze(helper.getLevel()),"Dedicated test server must never be frozen");
        helper.succeed();
    }
    public static void templeBuildIsStable(GameTestHelper helper) {
        var palace=helper.getLevel().getServer().getLevel(MindPalace.LEVEL);
        helper.assertTrue(palace!=null,"Missing Mind Palace");
        MindPalace.buildTemple(palace);
        helper.assertTrue(palace.getBlockState(MindPalace.ALTAR).is(Blocks.CHISELED_QUARTZ_BLOCK),"Altar missing");
        helper.assertTrue(palace.getBlockState(MindPalace.ALTAR.above()).is(Blocks.AMETHYST_CLUSTER),"Crystal missing");
        helper.assertTrue(palace.getBlockState(new BlockPos(0,159,7)).is(Blocks.GOLD_BLOCK),"Arrival floor missing");
        helper.assertTrue(palace.getBlockState(new BlockPos(0,150,0)).isAir(),"Temple must float over void");
        var marker=new BlockPos(3,160,3);
        palace.setBlock(marker,Blocks.DIAMOND_BLOCK.defaultBlockState(),2);
        MindPalace.buildTemple(palace);
        helper.assertTrue(palace.getBlockState(marker).is(Blocks.DIAMOND_BLOCK),"Second visit rebuilt existing temple");
        palace.removeBlock(marker,false);
        helper.succeed();
    }
    @SuppressWarnings("removal")
    public static void playerFlowAndFrozenTime(GameTestHelper helper) {
        var server=helper.getLevel().getServer();
        var player=helper.makeMockServerPlayerInLevel();
        var pos=helper.absolutePos(new BlockPos(1,2,1));
        player.teleportTo(pos.getX()+0.5,pos.getY(),pos.getZ()+0.5);
        double x=player.getX(),y=player.getY(),z=player.getZ();
        var state=new dev.archmage.magic.MageProgress(10);
        state.setRank(dev.archmage.magic.Sphere.QUAS,1);
        state.addEssence(dev.archmage.game.ArchmageGameplay.ESSENCE,1);
        var q=dev.archmage.magic.Sphere.QUAS;
        dev.archmage.game.PlayerMagicData.save(player,state);
        try {
            helper.assertTrue(MindPalace.toggle(player),"Entering the palace failed");
            helper.assertTrue(MindPalace.isInside(player),"Player did not change dimension");
            helper.assertTrue(dev.archmage.game.ArchmageGameplay.learnTest(player),"Spell unlock failed in palace");
            state.learn(dev.archmage.game.ArchmageGameplay.TEST_SPELL,java.util.List.of(q,q,q),dev.archmage.game.ArchmageGameplay.ESSENCE,1);
            state.equip(0,dev.archmage.game.ArchmageGameplay.TEST_SPELL);
            helper.assertTrue(dev.archmage.game.ArchmageGameplay.learnTest(player),"Repeated unlock must stay usable");
            helper.assertTrue(dev.archmage.game.PlayerMagicData.load(player).essence(dev.archmage.game.ArchmageGameplay.ESSENCE)==0,"Essence spent incorrectly");
            dev.archmage.game.QuasVisuals.tick(player,true);
            var orb=dev.archmage.game.QuasVisuals.current(player);
            helper.assertTrue(orb!=null&&!orb.shouldBeSaved(),"Quas must be a non-saved visual entity");
            helper.assertTrue(orb.getSlot(0).get().is(dev.archmage.game.ArchmageItems.QUAS_ORB.get()),"Quas item model missing");
            dev.archmage.game.QuasVisuals.tick(player,true);
            helper.assertTrue(dev.archmage.game.QuasVisuals.current(player)==orb,"Quas duplicated during ticking");
            helper.assertTrue(MindPalace.freeze(server.overworld()),"Outer world must be frozen");
            helper.assertTrue(!MindPalace.freeze(server.getLevel(MindPalace.LEVEL)),"Palace itself must keep ticking");
        } catch (Throwable ex) {server.getPlayerList().remove(player);throw ex;}
        long frozenTime=server.overworld().getGameTime();
        helper.runAfterDelay(5,()->{
            try {
                helper.assertTrue(server.overworld().getGameTime()==frozenTime,"Outer world clock advanced in palace");
                helper.assertTrue(MindPalace.leave(player),"Returning from palace failed");
                helper.assertTrue(player.level().dimension().equals(helper.getLevel().dimension()),"Wrong return dimension");
                helper.assertTrue(Math.abs(player.getX()-x)<0.01&&Math.abs(player.getY()-y)<0.01&&Math.abs(player.getZ()-z)<0.01,"Return coordinates changed");
                var clone=(net.minecraft.server.level.ServerPlayer)helper.makeMockServerPlayer(net.minecraft.world.level.GameType.SURVIVAL);
                clone.connection=player.connection;
                clone.restoreFrom(player,false);
                helper.assertTrue(dev.archmage.game.PlayerMagicData.load(clone).snapshot().equals(state.snapshot()),"Progress lost in respawn clone");
                helper.assertTrue(!MindPalace.isActive(server),"Outer time stayed frozen after exit");
                var oldOrb=dev.archmage.game.QuasVisuals.current(player);
                dev.archmage.game.QuasVisuals.tick(player,true);
                helper.assertTrue(dev.archmage.game.QuasVisuals.current(player).level()==player.level()&&oldOrb.isRemoved(),"Quas did not follow dimension change cleanly");
                var target=net.minecraft.world.entity.EntityTypes.ZOMBIE.create(player.level(),net.minecraft.world.entity.EntitySpawnReason.COMMAND);
                target.setPos(x,y,z+2);player.level().addFreshEntity(target);
                float before=target.getHealth();
                helper.assertTrue(dev.archmage.game.ArchmageGameplay.pulse(player),"First pulse failed");
                helper.assertTrue(target.getHealth()<before&&target.hasEffect(net.minecraft.world.effect.MobEffects.SLOWNESS),"Pulse damage/slow failed");
                helper.assertTrue(!dev.archmage.game.ArchmageGameplay.pulse(player),"Cooldown did not block a second pulse");
                target.discard();
                helper.succeed();
            } finally {dev.archmage.game.QuasVisuals.remove(player.getUUID());server.getPlayerList().remove(player);}
        });
    }
}
