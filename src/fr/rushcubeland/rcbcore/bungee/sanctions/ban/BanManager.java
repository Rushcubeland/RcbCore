package fr.rushcubeland.rcbcore.bungee.sanctions.ban;

import fr.rushcubeland.commons.data.sql.DatabaseManager;
import fr.rushcubeland.commons.data.sql.SQL;
import fr.rushcubeland.commons.utils.UUIDFetcher;
import fr.rushcubeland.rcbcore.bungee.sanctions.SanctionHistory;
import fr.rushcubeland.rcbcore.bungee.utils.TimeUnit;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import javax.annotation.Nullable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BanManager {

    private HashMap<String, String[]> databan = new HashMap<>();
    private final ArrayList<String> dataunban = new ArrayList<>();
    private ArrayList<String> uuidbansbdd = new ArrayList<>();

    public void ban(UUID uuid, long durationSeconds, String reason, String modName){
        long start = System.currentTimeMillis();
        long end = System.currentTimeMillis()+(durationSeconds*1000);
        if(durationSeconds == -1L) {
            end = -1L;
            SanctionHistory.addSanctionHistory(uuid, "BAN", System.currentTimeMillis(), null, modName);
        }
        else
        {
            SanctionHistory.addSanctionHistory(uuid, "TEMP_BAN", System.currentTimeMillis(), end, modName);
        }
        String[] data = {String.valueOf(start), String.valueOf(end), reason, modName};
        this.databan.put(uuid.toString(), data);

        if(ProxyServer.getInstance().getPlayer(uuid) != null) {
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uuid);
            ProxyServer.getInstance().broadcast(new TextComponent("§6Le joueur §e" + target.getName() + " §6a été §cbanni §6pour §e" + reason));
            target.disconnect(new TextComponent("§cVous avez été banni !\n \n \n§eRaison : §f" + reason + "\n \n §aTemps restant : §f" +
                    getTimeLeft(uuid)));
        }
        else
        {
            ProxyServer.getInstance().broadcast(new TextComponent("§6Le joueur §e" + UUIDFetcher.getNameFromUUID(uuid) + " §6a été §cbanni §6pour §e" + reason));
        }

        this.dataunban.remove(uuid.toString());


    }

    public boolean isBanned(UUID uuid){
        return this.databan.containsKey(uuid.toString());
    }

    public long getEnd(UUID uuid){
        if(isBanned(uuid)){
            String[] data = this.databan.get(uuid.toString());
            return Long.parseLong(data[1]);
        }
        return 0L;
    }

    public String getModerator(UUID uuid){
        if(isBanned(uuid)){
            String[] data = this.databan.get(uuid.toString());
            return String.valueOf(data[3]);
        }
        return null;
    }

    public void unban(UUID uuid, @Nullable String modName){
        if(isBanned(uuid)){
            this.databan.remove(uuid.toString());
            this.dataunban.add(uuid.toString());
            SanctionHistory.addSanctionHistory(uuid, "UNBAN", System.currentTimeMillis(), null, modName);
        }
    }

    public void checkDuration(UUID uuid) {
        if(!isBanned(uuid))
            return;
        if(getEnd(uuid) == -1L)
            return;
        if(getEnd(uuid) < System.currentTimeMillis()) {
            unban(uuid, "CONSOLE");
        }
    }

    public String getReason(UUID uuid){
        if(isBanned(uuid)){
            String[] data = this.databan.get(uuid.toString());
            return data[2];
        }
        return null;
    }

    public String getTimeLeft(UUID uuid) {
        if (!isBanned(uuid)) return "§cNon banni";
        if (getEnd(uuid) == -1L) {
            return "§cPermanent";
        }
        long tempsRestant = (getEnd(uuid) - System.currentTimeMillis()) / 1000L;
        int annees = 0;
        int mois = 0;
        int jours = 0;
        int heures = 0;
        int minutes = 0;
        int seconds = 0;

        while (tempsRestant >= TimeUnit.ANNEES.getToSecond()){
            annees++;
            tempsRestant -= TimeUnit.ANNEES.getToSecond();
        }
        while (tempsRestant >= TimeUnit.MOIS.getToSecond()) {
            mois++;
            tempsRestant -= TimeUnit.MOIS.getToSecond();
        }
        while (tempsRestant >= TimeUnit.JOUR.getToSecond()) {
            jours++;
            tempsRestant -= TimeUnit.JOUR.getToSecond();
        }
        while (tempsRestant >= TimeUnit.HEURE.getToSecond()) {
            heures++;
            tempsRestant -= TimeUnit.HEURE.getToSecond();
        }
        while (tempsRestant >= TimeUnit.MINUTE.getToSecond()) {
            minutes++;
            tempsRestant -= TimeUnit.MINUTE.getToSecond();
        }
        while (tempsRestant >= TimeUnit.SECONDE.getToSecond()) {
            seconds++;
            tempsRestant -= TimeUnit.SECONDE.getToSecond();
        }
        if(annees != 0){
            return annees + " " + TimeUnit.ANNEES.getName() + ", " + mois + " " + TimeUnit.MOIS.getName() + ", " + jours + " " + TimeUnit.JOUR.getName() + ", " + heures + " " + TimeUnit.HEURE.getName() + ", " + minutes + " " + TimeUnit.MINUTE.getName();
        }
        else if(mois != 0){
            return mois + " " + TimeUnit.MOIS.getName() + ", " + jours + " " + TimeUnit.JOUR.getName() + ", " + heures + " " + TimeUnit.HEURE.getName() + ", " + minutes + " " + TimeUnit.MINUTE.getName();
        }
        else if(jours != 0){
            return jours + " " + TimeUnit.JOUR.getName() + ", " + heures + " " + TimeUnit.HEURE.getName() + ", " + minutes + " " + TimeUnit.MINUTE.getName();
        }
        else if(heures != 0){
            return heures + " " + TimeUnit.HEURE.getName() + ", " + minutes + " " + TimeUnit.MINUTE.getName();
        }
        else if(minutes != 0){
            return minutes + " " + TimeUnit.MINUTE.getName();
        }
        else {
            return seconds + " " + TimeUnit.SECONDE.getName();
        }
    }

    private void getUUIDOfBanFromMySQL(){

        ArrayList<String> uuidBans = new ArrayList<>();

        try {
            SQL.query(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), "SELECT uuid FROM Bans", rs -> {

                try {
                    if(rs.next()){
                        uuidBans.add(rs.getString("uuid"));
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        this.uuidbansbdd = uuidBans;
    }

    private HashMap<String, String[]> getDataOfBanFromMySQL(){

        HashMap<String, String[]> Mapdata = new HashMap<>();

        for(String uuid : uuidbansbdd){
            try {
                SQL.query(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("SELECT * FROM Bans WHERE uuid='%s'", uuid), rs -> {

                    try {
                        if(rs.next()){

                            long start = Long.parseLong(rs.getString("start"));
                            long end = Long.parseLong(rs.getString("end"));
                            String reason = rs.getString("reason");
                            String modName = rs.getString("moderator");
                            String[] data = {String.valueOf(start), String.valueOf(end), String.valueOf(reason), String.valueOf(modName)};
                            Mapdata.put(uuid, data);
                        }
                    } catch (SQLException exception) {
                        exception.printStackTrace();
                    }
                });
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
        return Mapdata;
    }

    private void sendDataofBanToMySQL(){
        for(String uuid : this.dataunban){
            try {
                SQL.update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("DELETE FROM Bans WHERE uuid='%s'",
                        uuid));

            } catch (SQLException exception) {
                exception.printStackTrace();
            }

        }
        for(Map.Entry entry : this.databan.entrySet()){

            String uuid = (String) entry.getKey();
            String[] data = (String[]) entry.getValue();
            long start = Long.parseLong(data[0]);
            long end = Long.parseLong(data[1]);
            String reason = data[2];
            String modName = data[3];

            try {
                SQL.query(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("SELECT * FROM Bans WHERE uuid='%s'", uuid
                ), rs -> {
                    try {
                        if(rs.next()){
                            try {
                                SQL.update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("UPDATE Bans SET start='%s', end='%s', reason='%s', moderator='%s' WHERE uuid='%s'",
                                        start, end, reason, modName, uuid));

                            } catch (SQLException exception) {
                                exception.printStackTrace();
                            }
                        }
                        else
                        {
                            try {
                                SQL.update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("INSERT INTO Bans (uuid, start, end, reason, moderator) VALUES ('%s', '%s', '%s', '%s', '%s')",
                                        uuid, start, end, reason, modName));
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

    private void getData(){
       getUUIDOfBanFromMySQL();
       this.databan = getDataOfBanFromMySQL();
    }

    private void sendData(){
        sendDataofBanToMySQL();
    }

    public void update(){
        sendData();
    }

    public void load(){
        getData();
    }

}
