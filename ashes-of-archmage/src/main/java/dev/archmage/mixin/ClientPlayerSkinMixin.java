package dev.archmage.mixin;

import dev.archmage.client.ArchmageAppearance;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.player.PlayerSkin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value=AbstractClientPlayer.class,remap=false)
public abstract class ClientPlayerSkinMixin {
    @Inject(method="getSkin",at=@At("HEAD"),cancellable=true,require=1,remap=false)
    private void archmage$localSkin(CallbackInfoReturnable<PlayerSkin> callback) {
        if(ArchmageAppearance.isEnabledFor((AbstractClientPlayer)(Object)this))callback.setReturnValue(ArchmageAppearance.SKIN);
    }
}
