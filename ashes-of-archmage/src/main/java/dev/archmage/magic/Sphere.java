package dev.archmage.magic;

/** Stable IDs, independent of translated names and Minecraft registration. */
public enum Sphere {
    QUAS("quas", 1), WEX("wex", 1), EXORT("exort", 1),
    SANGUIS("sanguis", 2), UMBRA("umbra", 2), MORTIS("mortis", 2),
    ANIMA("anima", 3), CHRONOS("chronos", 3), GRAVITAS("gravitas", 3), NIHIL("nihil", 4);

    private final String id;
    private final int tier;

    Sphere(String id, int tier) { this.id = id; this.tier = tier; }
    public String id() { return id; }
    public int tier() { return tier; }
}
