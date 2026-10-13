package dev.archmage.debug.testmixin;

import net.minecraft.gametest.framework.GameTestServer;
import net.minecraft.server.packs.repository.PackRepository;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import java.util.Collection;

/** Development launch only: Forge 66.0.10 selects no optional mod packs in GameTestServer. */
@Mixin(value=GameTestServer.class,remap=false)
public abstract class GameTestPackSelectionMixin {
    @Redirect(method="create",at=@At(value="INVOKE",target="Lnet/minecraft/server/packs/repository/PackRepository;getSelectedIds()Ljava/util/Collection;"),require=1,remap=false)
    private static Collection<String> archmage$includeTestPacks(PackRepository packs) {
        return packs.getAvailableIds();
    }

    // Vanilla GameTestServer intentionally bakes an empty dimension registry.
    // Use the loaded data-pack dimensions in this development test harness only.
    @Redirect(method="lambda$create$1",at=@At(value="INVOKE",target="Lnet/minecraft/world/level/levelgen/WorldDimensions;bake(Lnet/minecraft/core/Registry;)Lnet/minecraft/world/level/levelgen/WorldDimensions$Complete;"),require=1,remap=false)
    private static net.minecraft.world.level.levelgen.WorldDimensions.Complete archmage$includeDimensions(
        net.minecraft.world.level.levelgen.WorldDimensions dimensions,
        net.minecraft.core.Registry<net.minecraft.world.level.dimension.LevelStem> ignored,
        net.minecraft.world.level.LevelSettings settings,
        net.minecraft.server.WorldLoader.DataLoadContext context) {
        return dimensions.bake(context.datapackDimensions().lookupOrThrow(net.minecraft.core.registries.Registries.LEVEL_STEM));
    }
}
