package dev.archmage.mixin;

import dev.archmage.game.MindPalace;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.function.BooleanSupplier;

@Mixin(value=ServerLevel.class,remap=false)
public abstract class ServerLevelFreezeMixin {
    @Inject(method="tick",at=@At("HEAD"),cancellable=true,require=1,remap=false)
    private void archmage$freezeOutside(BooleanSupplier haveTime,CallbackInfo ci) {
        if(MindPalace.freeze((ServerLevel)(Object)this)) ci.cancel();
    }
}
