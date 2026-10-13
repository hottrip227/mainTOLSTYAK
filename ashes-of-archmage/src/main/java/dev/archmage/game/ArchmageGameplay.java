package dev.archmage.game;

import dev.archmage.magic.Sphere;
import com.mojang.brigadier.Command;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import java.util.List;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;

public final class ArchmageGameplay {
    public static final String TEST_SPELL="frost_pulse_test";
    public static final String ESSENCE="frost_echo_test";
    private ArchmageGameplay() {}
    public static void register() {
        RegisterCommandsEvent.BUS.addListener(ArchmageGameplay::commands);
        PlayerInteractEvent.RightClickBlock.BUS.addListener(ArchmageGameplay::altar);
        TickEvent.PlayerTickEvent.Post.BUS.addListener(ArchmageGameplay::tick);
        PlayerEvent.PlayerLoggedOutEvent.BUS.addListener(e->QuasVisuals.remove(e.getEntity().getUUID()));
        ServerStoppingEvent.BUS.addListener(e->{QuasVisuals.clear();AltarHints.clear();});
    }
    private static void commands(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("archmage")
            .then(Commands.literal("test_start").executes(c -> {
                var p=c.getSource().getPlayerOrException();
                if(!MindPalace.isSolo(p.level().getServer())) { c.getSource().sendFailure(Component.translatable("archmage.solo_only")); return 0; }
                var state=PlayerMagicData.load(p);
                state.setRank(Sphere.QUAS,Math.max(1,state.rank(Sphere.QUAS)));
                if(!state.knows(TEST_SPELL)&&state.essence(ESSENCE)==0) state.addEssence(ESSENCE,1);
                PlayerMagicData.save(p,state);
                p.sendSystemMessage(Component.translatable("archmage.test_start")); return Command.SINGLE_SUCCESS;
            }))
            .then(Commands.literal("learn_test").executes(c->learnTest(c.getSource().getPlayerOrException())?1:0))
            .then(Commands.literal("status").executes(c->{ status(c.getSource().getPlayerOrException());return 1;}))
            .then(Commands.literal("palace").executes(c->MindPalace.toggle(c.getSource().getPlayerOrException())?1:0))
            .then(Commands.literal("back").executes(c->MindPalace.leave(c.getSource().getPlayerOrException())?1:0))
            .then(Commands.literal("pulse").executes(c->pulse(c.getSource().getPlayerOrException())?1:0)));
    }
    private static void status(ServerPlayer p) {
        var s=PlayerMagicData.load(p);
        p.sendSystemMessage(Component.translatable("archmage.status",s.rank(Sphere.QUAS),s.essence(ESSENCE),s.knows(TEST_SPELL)?"✓":"—",s.slots()));
    }
    private static void altar(PlayerInteractEvent.RightClickBlock e) {
        if(e.getHand()!=InteractionHand.MAIN_HAND || !(e.getEntity() instanceof ServerPlayer p) || !MindPalace.isInside(p)) return;
        if(!e.getPos().equals(MindPalace.ALTAR)&&!e.getPos().equals(MindPalace.ALTAR.above())) return;
        learnTest(p);
    }
    public static boolean learnTest(ServerPlayer p) {
        if(!MindPalace.isInside(p)) {p.sendSystemMessage(Component.translatable("archmage.learn_inside"));return false;}
        var state=PlayerMagicData.load(p);
        if(state.knows(TEST_SPELL)) {
            state.equip(0,TEST_SPELL);PlayerMagicData.save(p,state);
            p.sendSystemMessage(Component.translatable("archmage.already_learned"));return true;
        }
        if(!state.learn(TEST_SPELL,List.of(Sphere.QUAS,Sphere.QUAS,Sphere.QUAS),ESSENCE,1)) {
            p.sendSystemMessage(Component.translatable("archmage.need_essence"));return false;
        }
        state.equip(0,TEST_SPELL);PlayerMagicData.save(p,state);
        p.level().sendParticles(ParticleTypes.SNOWFLAKE,0.5,161.6,0.5,45,0.35,0.5,0.35,0.02);
        p.sendSystemMessage(Component.translatable("archmage.learned"));return true;
    }
    private static void tick(TickEvent.PlayerTickEvent.Post e) {
        if(!(e.player() instanceof ServerPlayer p)) return;
        MindPalace.rescueFromVoid(p);
        var root=PlayerMagicData.root(p);
        int cooldown=root.getIntOr("pulse_cooldown",0);
        if(cooldown>0 && !MindPalace.isInside(p)) root.putInt("pulse_cooldown",cooldown-1);
        QuasVisuals.tick(p,PlayerMagicData.load(p).rank(Sphere.QUAS)>0);
    }
    public static boolean pulse(ServerPlayer p) {
        if(MindPalace.isInside(p)) { p.sendSystemMessage(Component.translatable("archmage.cast_outside"));return false; }
        if(!PlayerMagicData.load(p).knows(TEST_SPELL)) {p.sendSystemMessage(Component.translatable("archmage.not_learned"));return false;}
        var root=PlayerMagicData.root(p);
        if(root.getIntOr("pulse_cooldown",0)>0) {p.sendOverlayMessage(Component.translatable("archmage.cooldown"));return false;}
        var level=(ServerLevel)p.level();
        var mobs=level.getEntitiesOfClass(Mob.class,p.getBoundingBox().inflate(6),m->m.isAlive()&&m.distanceToSqr(p)<=36);
        for(var mob:mobs) { mob.addEffect(new MobEffectInstance(MobEffects.SLOWNESS,80,3));mob.hurtServer(level,level.damageSources().magic(),2); }
        level.sendParticles(ParticleTypes.SNOWFLAKE,p.getX(),p.getY()+1,p.getZ(),45,1.2,0.5,1.2,0.1);
        for(int i=0;i<40;i++) { double a=i*Math.PI*2/40;level.sendParticles(ParticleTypes.SNOWFLAKE,p.getX()+Math.cos(a)*3,p.getY()+0.6,p.getZ()+Math.sin(a)*3,1,0,0,0,0.03); }
        root.putInt("pulse_cooldown",80);
        p.sendOverlayMessage(Component.translatable("archmage.pulse",mobs.size()));
        return true;
    }
}
