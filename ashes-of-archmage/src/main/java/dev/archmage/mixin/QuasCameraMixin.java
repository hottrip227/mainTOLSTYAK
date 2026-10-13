package dev.archmage.mixin;

import dev.archmage.client.QuasClientVisuals;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value=EntityRenderDispatcher.class,remap=false)
public abstract class QuasCameraMixin {
    @Inject(method="shouldRender",at=@At("HEAD"),cancellable=true,require=1,remap=false)
    private void archmage$hideFirstPersonOrb(Entity entity,Frustum frustum,double camX,double camY,double camZ,float partialTicks,CallbackInfoReturnable<Boolean> callback) {
        if(QuasClientVisuals.hideInFirstPerson(entity))callback.setReturnValue(false);
    }
}
