package fr.rushcubeland.rcbcore.bukkit.utils;

public enum TimeUnit {

    SECONDE("Seconde(s)", "sec", 1L),
    MINUTE("Minute(s)", "min", 60L),
    HEURE("Heure(s)", "h", 3600L),
    JOUR("Jour(s)", "j", 86400L),
    MOIS("Mois", "m", 2592000L),
    ANNEES("Année(s)", "a", 31536000L);

    private String name;
    private String shortcut;

    private long toSecond;

    TimeUnit(String name, String shortcut, long toSecond) {
        this.name = name;
        this.shortcut = shortcut;
        this.toSecond = toSecond;
    }

    public String getName() {
        return this.name;
    }

    public String getShortcut() {
        return this.shortcut;
    }

    public long getToSecond() {
        return this.toSecond;
    }
}

