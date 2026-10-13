package dev.archmage.client;

import dev.archmage.game.ArchmageItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import java.util.ArrayList;
import java.util.List;

/** Only the orbiting Quas and its own trail hide in first-person; spell particles are untouched. */
public final class QuasClientVisuals {
    private static final List<Particle> TRAIL=new ArrayList<>();
    private static ClientLevel lastLevel;
    private QuasClientVisuals() {}
    public static boolean isOrb(Entity entity) {
        return entity instanceof Display.ItemDisplay display && display.getSlot(0).get().is(ArchmageItems.QUAS_ORB.get());
    }
    public static boolean hideInFirstPerson(Entity entity) {
        var mc=Minecraft.getInstance();
        return mc.hasSingleplayerServer() && mc.options.getCameraType().isFirstPerson() && isOrb(entity);
    }
    public static void tick() {
        var mc=Minecraft.getInstance();
        if(mc.level!=lastLevel) { clear();lastLevel=mc.level; }
        if(mc.level==null||mc.player==null||!mc.hasSingleplayerServer()||!mc.player.isAlive()||mc.options.getCameraType().isFirstPerson()) {clear();return;}
        if(mc.isPaused())return;
        TRAIL.removeIf(p->!p.isAlive());
        for(var orb:mc.level.getEntitiesOfClass(Display.ItemDisplay.class,mc.player.getBoundingBox().inflate(4),QuasClientVisuals::isOrb)) {
            for(int i=0;i<2;i++) emit(mc,new DustParticleOptions(0x85e6ff,0.65f),orb,0.045);
            if(mc.player.tickCount%3==0)emit(mc,ParticleTypes.SNOWFLAKE,orb,0.05);
        }
    }
    private static void emit(Minecraft mc,ParticleOptions options,Entity orb,double spread) {
        var r=java.util.concurrent.ThreadLocalRandom.current();
        var particle=mc.particleEngine.createParticle(options,orb.getX()+(r.nextDouble()-.5)*spread*2,orb.getY()+(r.nextDouble()-.5)*spread*2,orb.getZ()+(r.nextDouble()-.5)*spread*2,0,0,0);
        if(particle!=null)TRAIL.add(particle);
    }
    private static void clear() { TRAIL.forEach(Particle::remove);TRAIL.clear(); }
}
