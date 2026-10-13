package dev.archmage.magic;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class MageProgressTest {
    private static final List<Sphere> ICE_FORMULA = List.of(Sphere.QUAS, Sphere.QUAS, Sphere.QUAS);

    @Test void allTenSphereIdsAreUnique() {
        assertEquals(10, Sphere.values().length);
        assertEquals(10, java.util.Arrays.stream(Sphere.values()).map(Sphere::id).distinct().count());
    }
    @Test void aNewMageHasNoPower() {
        var p = new MageProgress(10);
        assertEquals(0, p.rank(Sphere.QUAS));
        assertTrue(p.learnedSpells().isEmpty());
        assertNull(p.equipped(0));
    }
    @Test void learningSpendsEssenceOnlyOnce() {
        var p = new MageProgress(10);
        p.setRank(Sphere.QUAS, 1);
        p.addEssence("test_ice", 2);
        assertTrue(p.learn("test_spell", ICE_FORMULA, "test_ice", 1));
        assertTrue(p.knows("test_spell"));
        assertFalse(p.learn("test_spell", ICE_FORMULA, "test_ice", 1));
        assertEquals(1, p.essence("test_ice"));
    }
    @Test void missingSphereOrEssenceDoesNotConsumeRewards() {
        var p = new MageProgress(10);
        p.addEssence("test_ice", 1);
        assertFalse(p.learn("test_spell", ICE_FORMULA, "test_ice", 1));
        assertEquals(1, p.essence("test_ice"));
        p.setRank(Sphere.QUAS, 1);
        assertFalse(p.learn("test_spell", ICE_FORMULA, "test_ice", 2));
        assertEquals(1, p.essence("test_ice"));
    }
    @Test void equippedSpellsCanChangeWithoutForgetting() {
        var p = new MageProgress(20);
        p.setRank(Sphere.QUAS, 1);
        p.addEssence("test_ice", 1);
        p.learn("test_spell", ICE_FORMULA, "test_ice", 1);
        p.equip(19, "test_spell");
        p.equip(19, null);
        assertTrue(p.knows("test_spell"));
        assertThrows(IllegalArgumentException.class, () -> p.equip(0, "unlearned"));
        assertThrows(IndexOutOfBoundsException.class, () -> p.equip(20, null));
    }
    @Test void invalidInputsAreRejected() {
        var p = new MageProgress(10);
        assertThrows(IllegalArgumentException.class, () -> new MageProgress(21));
        assertThrows(IllegalArgumentException.class, () -> p.setRank(Sphere.QUAS, -1));
        assertThrows(IllegalArgumentException.class, () -> p.addEssence("test_ice", -1));
        assertThrows(IllegalArgumentException.class, () -> p.learn("test", List.of(Sphere.QUAS), "ice", 1));
    }
}
