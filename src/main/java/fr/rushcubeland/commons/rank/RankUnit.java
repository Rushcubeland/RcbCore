package fr.rushcubeland.commons.rank;

import fr.rushcubeland.commons.data.sql.DatabaseManager;
import fr.rushcubeland.commons.data.sql.SQL;
import net.md_5.bungee.api.ChatColor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public enum RankUnit {

    JOUEUR("Joueur", 50, ChatColor.GRAY.toString(), ChatColor.GRAY, false),
    VIP("VIP", 45,  ChatColor.YELLOW + "[VIP] " + ChatColor.WHITE, ChatColor.YELLOW, false),
    VIPP("VIP+", 40,    ChatColor.DARK_PURPLE + "[VIP+] " + ChatColor.WHITE, ChatColor.DARK_PURPLE, false),
    INFLUENCEUR("Influenceur", 38, ChatColor.AQUA + "[Influenceur] " + ChatColor.WHITE, ChatColor.AQUA, true),
    INFLUENCEUSE("Influenceuse", 37, ChatColor.AQUA + "[Influenceuse] " + ChatColor.WHITE, ChatColor.AQUA, true),
    AMI("Ami", 35, ChatColor.DARK_AQUA + "[Ami] " + ChatColor.WHITE, ChatColor.DARK_AQUA, true),
    AMIE("Amie", 30, ChatColor.DARK_AQUA + "[Amie] " + ChatColor.WHITE, ChatColor.DARK_AQUA, true),
    ASSISTANT("Assistant", 20, ChatColor.GREEN +"[Assistant] " + ChatColor.WHITE, ChatColor.GREEN, true),
    ASSISTANTE("Assistante", 15, ChatColor.GREEN + "[Assistante] " + ChatColor.WHITE, ChatColor.GREEN, true),
    DEVELOPPEUR("Développeur", 10, ChatColor.BLUE + "[Développeur] " + ChatColor.WHITE, ChatColor.BLUE, true),
    DEVELOPPEUSE("Développeuse", 9, ChatColor.BLUE + "[Développeuse] " + ChatColor.WHITE, ChatColor.BLUE, true),
    MODERATEUR("Modérateur", 8, ChatColor.GOLD + "[Modérateur] " + ChatColor.WHITE, ChatColor.GOLD, true),
    MODERATRICE("Modératrice", 7, ChatColor.GOLD + "[Modératrice] " + ChatColor.WHITE, ChatColor.GOLD, true),
    RESPMOD("RespMod", 5, ChatColor.GOLD + "[RespMod] " + ChatColor.WHITE, ChatColor.GOLD, true),
    ADMINISTRATEUR("Admin", 0, ChatColor.DARK_RED + "[Admin] " + ChatColor.WHITE, ChatColor.DARK_RED, true);

    private final String name;
    private final int power;
    private final String prefix;
    private final ChatColor color;

    private final boolean isSpecial;
    private ArrayList<String> permissions = new ArrayList<>();

    public static final int FIRST_LEVEL_RANK = 50;
    public static final int FIRST_LEVEL_SPECIAL_RANK = 38;
    public static final int FIRST_LEVEL_STAFF_RANK= 20;


    RankUnit(String name, int power, String prefix, ChatColor color, boolean isSpecial) {
        this.name = name;
        this.power = power;
        this.prefix = prefix;
        this.color = color;
        this.isSpecial = isSpecial;
    }

    public static RankUnit getByName(String name){
        return Arrays.stream(values()).filter(r -> r.getName().equalsIgnoreCase(name)).findAny().orElse(null);
    }

    public static RankUnit getByPower(int power){
        return Arrays.stream(values()).filter(r -> r.getPower() == power).findAny().orElse(null);
    }

    public String getName() {
        return name;
    }

    public Integer getPower() {
        return power;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getColoredName(){
        return color + name;
    }


    public void addPermission(String permission){
        if(!this.permissions.contains(permission)){
            this.permissions.add(permission);
        }
    }

    public void removePermission(String permission){
        this.permissions.remove(permission);
    }

    public boolean hasPermission(String permission){
        return this.permissions.contains(permission);
    }

    public ArrayList<String> getPermissions(){
        return this.permissions;
    }

    public void setPermissions(ArrayList<String> permissions) {
        this.permissions = permissions;
    }

    public boolean isSpecial() {
        return isSpecial;
    }

    public ChatColor getColor() {
        return color;
    }

    private ArrayList<String> getDataofRankPermissionsFromMySQL(){
        ArrayList<String> dataRankperms = new ArrayList<>();
        try {
            SQL.query(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("SELECT permission FROM Rank_permissions WHERE grade='%s'",
                    getName()), rs -> {

                try {
                    while(rs.next()){

                        dataRankperms.add(rs.getString("permission"));

                    }
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            });
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return dataRankperms;
    }

    private void sendDataofRankPermissionsToMySQL(){
        if(!getPermissions().isEmpty()){
            for(String perms : getPermissions()){

                try {
                    SQL.query(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("SELECT permission FROM Rank_permissions WHERE grade='%s' AND permission='%s'",
                            getName(), perms), rs -> {
                        try {
                            if(!rs.next()){
                                try {
                                    SQL.update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("INSERT INTO Rank_permissions (grade, permission) VALUES ('%s', '%s')",
                                            getName(), perms));

                                } catch (SQLException exception) {
                                    exception.printStackTrace();
                                }
                            }
                        } catch (SQLException exception) {
                            exception.printStackTrace();
                        }
                    });
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    private void sendTasks(){
        sendDataofRankPermissionsToMySQL();
    }

    private void getTasks(){
        ArrayList<String> dataRankperms = getDataofRankPermissionsFromMySQL();
        setPermissions(dataRankperms);
    }

    public void onDisable(){
        sendTasks();
    }

    public void onEnable(){
        getTasks();
    }
}
