package dev.archmage.mixin;

import dev.archmage.game.MindPalace;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.clock.ServerClockManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value=MinecraftServer.class,remap=false)
public abstract class ServerClockFreezeMixin {
    @Redirect(method="tickChildren",at=@At(value="INVOKE",target="Lnet/minecraft/world/clock/ServerClockManager;tick()V"),require=1,remap=false)
    private void archmage$freezeClocks(ServerClockManager clock) {
        if(!MindPalace.isActive((MinecraftServer)(Object)this)) clock.tick();
    }
}
