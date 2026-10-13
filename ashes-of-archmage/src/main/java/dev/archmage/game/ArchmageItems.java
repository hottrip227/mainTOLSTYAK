package dev.archmage.game;

import dev.archmage.ArchmageMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/** Internal visual model; never granted as a loot item or essence. */
public final class ArchmageItems {
    private static final DeferredRegister<Item> ITEMS=DeferredRegister.create(Registries.ITEM,ArchmageMod.MOD_ID);
    public static final RegistryObject<Item> QUAS_ORB=ITEMS.register("quas_orb",()->new Item(new Item.Properties().setId(ResourceKey.create(Registries.ITEM,Identifier.fromNamespaceAndPath(ArchmageMod.MOD_ID,"quas_orb")))));
    private ArchmageItems() {}
    public static void register(BusGroup group) { ITEMS.register(group); }
}
