package dev.archmage.game;

import dev.archmage.magic.MageProgress;
import dev.archmage.magic.Sphere;
import net.minecraft.nbt.CompoundTag;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PlayerMagicDataTest {
    @Test void nbtRoundTripPreservesRanksEssencesPermanentSpellsAndEmptySlots() {
        var state=new MageProgress(10);
        state.setRank(Sphere.QUAS,3); state.setRank(Sphere.WEX,2);
        state.addEssence("frost",5);
        assertTrue(state.learn("pulse",List.of(Sphere.QUAS,Sphere.QUAS,Sphere.QUAS),"frost",2));
        state.equip(0,"pulse");state.equip(9,"pulse");
        var restored=PlayerMagicData.decode(PlayerMagicData.encode(state));
        assertEquals(state.snapshot(),restored.snapshot());
        assertNull(restored.equipped(1)); assertEquals(3,restored.essence("frost"));
        assertFalse(restored.learn("pulse",List.of(Sphere.QUAS,Sphere.QUAS,Sphere.QUAS),"frost",2));
        assertEquals(3,restored.essence("frost"));
    }
    @Test void emptyNbtStartsWithTenEmptySlotsAndNoPower() {
        var state=PlayerMagicData.decode(new CompoundTag());
        assertEquals(10,state.slots());assertEquals(0,state.rank(Sphere.QUAS));
        assertTrue(state.learnedSpells().isEmpty());assertNull(state.equipped(0));
    }
    @Test void damagedSaveClampsSlotsAndIgnoresInvalidReferences() {
        var tag=new CompoundTag();tag.putInt("slot_count",1000);
        var ranks=new CompoundTag();ranks.putInt("quas",-10);tag.put("spheres",ranks);
        var essences=new CompoundTag();essences.putInt("bad",-4);tag.put("essences",essences);
        var slots=new CompoundTag();slots.putString("0","not_learned");tag.put("slots",slots);
        var state=PlayerMagicData.decode(tag);
        assertEquals(20,state.slots());assertEquals(0,state.rank(Sphere.QUAS));
        assertEquals(0,state.essence("bad"));assertNull(state.equipped(0));
    }
}
