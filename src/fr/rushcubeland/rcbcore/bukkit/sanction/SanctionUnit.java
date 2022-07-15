package fr.rushcubeland.rcbcore.bukkit.sanction;

import fr.rushcubeland.rcbcore.bukkit.utils.TimeUnit;

public enum SanctionUnit {

    MESSAGES_INUTILE(SanctionCategory.MESSAGE, "Message inutile",  "Mute", 10, TimeUnit.MINUTE, TimeUnit.MINUTE.getToSecond()),
    FAUSSE_INFO(SanctionCategory.MESSAGE,"Fausse Information", "Mute", 30, TimeUnit.MINUTE, TimeUnit.MINUTE.getToSecond()),
    FORMATAGE_INCORRECT(SanctionCategory.MESSAGE,"Formatage incorrect", "Mute", 20, TimeUnit.MINUTE, TimeUnit.MINUTE.getToSecond()),
    VENTARDISE(SanctionCategory.MESSAGE,"Ventardise", "Mute", 25, TimeUnit.MINUTE, TimeUnit.MINUTE.getToSecond()),
    FLOOD_SPAM(SanctionCategory.MESSAGE,"Flood/Spam", "Mute", 35, TimeUnit.MINUTE, TimeUnit.MINUTE.getToSecond()),
    MAUVAIS_LANGAGE(SanctionCategory.MESSAGE,"Mauvais langage", "Mute", 40, TimeUnit.MINUTE, TimeUnit.MINUTE.getToSecond()),
    PROVOCATION(SanctionCategory.MESSAGE,"Provocation", "Mute", 30, TimeUnit.MINUTE, TimeUnit.MINUTE.getToSecond()),
    INSULTE(SanctionCategory.MESSAGE,"Insulte", "Mute", 50, TimeUnit.MINUTE, TimeUnit.MINUTE.getToSecond()),
    INCITATION_INFRACTION(SanctionCategory.MESSAGE,"Incitation à l'infraction", "Mute", 1, TimeUnit.HEURE, TimeUnit.HEURE.getToSecond()),
    LIEN_INTERDIT(SanctionCategory.MESSAGE,"Lien interdit", "Mute", 70, TimeUnit.MINUTE, TimeUnit.MINUTE.getToSecond()),
    PUBLICITE(SanctionCategory.MESSAGE,"Publicité", "Mute", 80, TimeUnit.MINUTE, TimeUnit.MINUTE.getToSecond()),
    DDOS_HACK_LIEN(SanctionCategory.MESSAGE,"DDOS/Hack (lien)", "Ban", 7, TimeUnit.JOUR, TimeUnit.JOUR.getToSecond()),
    PSEUDO_INCORRECT(SanctionCategory.MESSAGE,"Pseudo incorrect", "Ban", 1, TimeUnit.MOIS, TimeUnit.JOUR.getToSecond()),
    MENACE_IRL(SanctionCategory.MESSAGE,"Menace IRL", "Ban", 2, TimeUnit.MOIS, TimeUnit.MOIS.getToSecond()),

    TROLL(SanctionCategory.ABUS,"Troll", "Mute", 30, TimeUnit.MINUTE, TimeUnit.MINUTE.getToSecond()),
    ABUS_REPORT(SanctionCategory.ABUS,"Abus de report", "Mute", 20, TimeUnit.MINUTE, TimeUnit.MINUTE.getToSecond()),

    ANTI_KB(SanctionCategory.TRICHE,"Anti-knockback", "Ban", 1, TimeUnit.ANNEES, TimeUnit.ANNEES.getToSecond()),
    KILL_AURA(SanctionCategory.TRICHE,"KillAura", "Ban", 1,  TimeUnit.ANNEES, TimeUnit.MOIS.getToSecond()),
    FAST_PLACE(SanctionCategory.TRICHE,"FastPlace", "Ban", 1, TimeUnit.ANNEES, TimeUnit.ANNEES.getToSecond()),
    MACRO_CLICK(SanctionCategory.TRICHE,"Macro Click", "Ban", 6, TimeUnit.MOIS, TimeUnit.MOIS.getToSecond()),
    REACH(SanctionCategory.TRICHE,"Reach", "Ban", 1, TimeUnit.ANNEES, TimeUnit.ANNEES.getToSecond()),
    FLY(SanctionCategory.TRICHE,"Fly/Glide", "Ban", 1, TimeUnit.ANNEES, TimeUnit.ANNEES.getToSecond()),
    TRICHE_AUTRE(SanctionCategory.TRICHE,"Triche Autre", "Ban", 8, TimeUnit.MOIS, TimeUnit.MOIS.getToSecond()),

    SKIN_INCORRECT(SanctionCategory.GAMEPLAY,"Skin incorrect", "Ban", 7, TimeUnit.JOUR, TimeUnit.JOUR.getToSecond()),
    ALLY(SanctionCategory.GAMEPLAY,"Alliance prohibée", "Ban", 3, TimeUnit.JOUR, TimeUnit.JOUR.getToSecond()),
    ANTI_JEU(SanctionCategory.GAMEPLAY,"Anti-jeu", "BAN", 1, TimeUnit.JOUR, TimeUnit.JOUR.getToSecond());

    private final SanctionCategory category;
    private final String motif;
    private final String sanctionCmd;
    private final long durationD;
    private final int multiplier;
    private final TimeUnit timeUnit;

    private final long durationSeconds;
    

    SanctionUnit(SanctionCategory category, String motif, String sanctionCmd, int multiplier, TimeUnit timeUnit, long durationD){
        this.category = category;
        this.motif = motif;
        this.sanctionCmd = sanctionCmd;
        this.multiplier = multiplier;
        this.timeUnit = timeUnit;
        this.durationD = durationD;
        this.durationSeconds = multiplier*durationD;
    }

    public SanctionCategory getCategory() {
        return category;
    }

    public String getMotif() {
        return motif;
    }

    public String getSanctionCmd() {
        return sanctionCmd;
    }

    public long getDurationD() {
        return durationD;
    }

    public long getDurationSeconds() {
        return durationSeconds;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }
}
