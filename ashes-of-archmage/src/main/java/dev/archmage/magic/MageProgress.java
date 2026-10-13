package dev.archmage.magic;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Domain prototype. Does NOT save to a Minecraft player/world yet.
 * Essences and learned spells are data, not inventory items.
 */
public final class MageProgress {
    public static final int MAX_LOADOUT_SLOTS = 20;
    private final EnumMap<Sphere, Integer> ranks = new EnumMap<>(Sphere.class);
    private final Map<String, Integer> essences = new HashMap<>();
    private final Set<String> learned = new HashSet<>();
    private final String[] loadout;

    public MageProgress(int slots) {
        if (slots < 1 || slots > MAX_LOADOUT_SLOTS) throw new IllegalArgumentException("Invalid slot count");
        loadout = new String[slots];
    }

    public int rank(Sphere sphere) { return ranks.getOrDefault(Objects.requireNonNull(sphere), 0); }

    public void setRank(Sphere sphere, int rank) {
        Objects.requireNonNull(sphere);
        if (rank < 0) throw new IllegalArgumentException("Negative rank");
        if (rank == 0) ranks.remove(sphere); else ranks.put(sphere, rank);
    }

    public void addEssence(String id, int amount) {
        requireId(id);
        if (amount <= 0) throw new IllegalArgumentException("Essence grant must be positive");
        essences.put(id, Math.addExact(essence(id), amount));
    }

    public int essence(String id) { requireId(id); return essences.getOrDefault(id, 0); }
    public boolean knows(String spellId) { requireId(spellId); return learned.contains(spellId); }

    /** Returns false without spending anything when already learned or requirements fail. */
    public boolean learn(String spellId, List<Sphere> formula, String essenceId, int cost) {
        requireId(spellId);
        requireId(essenceId);
        Objects.requireNonNull(formula);
        if (formula.size() != 3 || formula.stream().anyMatch(Objects::isNull))
            throw new IllegalArgumentException("A formula needs exactly three spheres");
        if (cost <= 0) throw new IllegalArgumentException("Cost must be positive");
        if (learned.contains(spellId) || essence(essenceId) < cost || formula.stream().anyMatch(s -> rank(s) == 0))
            return false;
        int remaining = essence(essenceId) - cost;
        if (remaining == 0) essences.remove(essenceId); else essences.put(essenceId, remaining);
        learned.add(spellId);
        return true;
    }

    public void equip(int slot, String spellId) {
        checkSlot(slot);
        if (spellId != null && !knows(spellId)) throw new IllegalArgumentException("Unknown spell");
        loadout[slot] = spellId;
    }

    public String equipped(int slot) { checkSlot(slot); return loadout[slot]; }
    public int slots() { return loadout.length; }
    public Set<String> learnedSpells() { return Set.copyOf(learned); }
    public Map<Sphere, Integer> sphereRanks() { return Collections.unmodifiableMap(new EnumMap<>(ranks)); }

    private void checkSlot(int slot) {
        if (slot < 0 || slot >= loadout.length) throw new IndexOutOfBoundsException("Invalid spell slot: " + slot);
    }

    private static void requireId(String id) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("Empty identifier");
    }
}
