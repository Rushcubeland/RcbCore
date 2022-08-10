package fr.rushcubeland.commons.permissions;

public enum PermissionsUnit {

    ALL("rcbapi.all"),
    REPORT("rcbapi.report"),
    GAMEMODE("rcbapi.gamemode"),
    BAN("rcbapi.ban"),
    TELEPORT("rcbapi.tp"),
    KICK("rcbapi.kick"),
    MODERATOR_MOD("rcbapi.mod"),
    MUTE("rcbapi.mute"),
    SANCTION_GUI("rcbapi.ap"),
    SANCTION_GUI_MSG("rcbapi.apmsg"),
    STAFF_CHAT("rcbapi.staffchat"),
    UNBAN("rcbapi.unban"),
    UNMUTE("rcbapi.unmute"),
    WHOIS("rcbapi.whois"),
    MAINTENANCE("rcbapi.maintenance"),
    STAFF_LIST("rcbapi.stafflist"),
    LIST_PLAYERS("rcbapi.playerslist");

    private final String permission;

    PermissionsUnit(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
