package fr.rushcubeland.rcbcore.bungee.sanctions.mute;

import fr.rushcubeland.commons.data.sql.DatabaseManager;
import fr.rushcubeland.commons.data.sql.SQL;
import fr.rushcubeland.rcbcore.bungee.BungeeSend;
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

public class MuteManager {

    private HashMap<String, String[]> datamute = new HashMap<>();
    private final ArrayList<String> dataunmute = new ArrayList<>();
    private ArrayList<String> uuidmutesbdd = new ArrayList<>();


    public void mute(UUID uuid, long durationSeconds, String reason, String modName){
        long start = System.currentTimeMillis();
        long end = System.currentTimeMillis()+(durationSeconds*1000);
        if (durationSeconds == -1L) {
            end = -1L;
            SanctionHistory.addSanctionHistory(uuid, "MUTE", System.currentTimeMillis(), null, modName);
        }
        else
        {
            SanctionHistory.addSanctionHistory(uuid, "TEMP_MUTE", System.currentTimeMillis(), end, modName);
        }
        String[] data = {String.valueOf(start), String.valueOf(end), reason};
        this.datamute.put(uuid.toString(), data);

        BungeeSend.sendMuteDataAdd(uuid.toString());

        if(ProxyServer.getInstance().getPlayer(uuid) != null) {
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uuid);
            target.sendMessage(new TextComponent("§6Vous avez été §cmute ! §eRaison : §f" + reason));
            target.sendMessage(new TextComponent("§aTemps restant : §f"+
                    getTimeLeft(uuid)));
        }

        this.dataunmute.remove(uuid.toString());

    }

    public boolean isMuted(UUID uuid){
        return this.datamute.containsKey(uuid.toString());
    }

    public long getEnd(UUID uuid){
        if(isMuted(uuid)){
            String[] data = this.datamute.get(uuid.toString());
            return Long.parseLong(data[1]);
        }
        return 0L;
    }

    public void unmute(UUID uuid, @Nullable String modName){
        if(isMuted(uuid)){
            this.datamute.remove(uuid.toString());
            this.dataunmute.add(uuid.toString());
            BungeeSend.sendMuteDataRemove(uuid.toString());
            SanctionHistory.addSanctionHistory(uuid, "UNMUTE", System.currentTimeMillis(), null, modName);
        }
    }

    public void checkDuration(UUID uuid) {
        if(!isMuted(uuid))
            return;
        if(getEnd(uuid) == -1L)
            return;
        if(getEnd(uuid) < System.currentTimeMillis()) {
            unmute(uuid, "CONSOLE");
        }
    }

    public String getReason(UUID uuid){
        if(isMuted(uuid)){
            String[] data = this.datamute.get(uuid.toString());
            return data[2];
        }
        return null;
    }

    public String getTimeLeft(UUID uuid) {
        if (!isMuted(uuid)) return "§cNon mute !";
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

    private void getUUIDOfMuteFromMySQL(){

        ArrayList<String> uuidMutes = new ArrayList<>();

        try {
            SQL.query(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), "SELECT uuid FROM Mute", rs -> {

                try {
                    if(rs.next()){
                        uuidMutes.add(rs.getString("uuid"));
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        this.uuidmutesbdd = uuidMutes;
    }

    private HashMap<String, String[]> getDataOfMuteFromMySQL(){

        HashMap<String, String[]> Mapdata = new HashMap<>();

        for(String uuid : uuidmutesbdd){
            try {
                SQL.query(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("SELECT * FROM Mute WHERE uuid='%s'", uuid), rs -> {

                    try {
                        if(rs.next()){

                            long start = Long.parseLong(rs.getString("start"));
                            long end = Long.parseLong(rs.getString("end"));
                            String reason = rs.getString("reason");
                            String[] data = {String.valueOf(start), String.valueOf(end), String.valueOf(reason)};
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

    private void sendDataofMuteToMySQL(){
        for(String uuid : this.dataunmute){
            try {
                SQL.update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("DELETE FROM Mute WHERE uuid='%s'",
                        uuid));

            } catch (SQLException exception) {
                exception.printStackTrace();
            }

        }
        for(Map.Entry entry : this.datamute.entrySet()){

            String uuid = (String) entry.getKey();
            String[] data = (String[]) entry.getValue();
            long start = Long.parseLong(data[0]);
            long end = Long.parseLong(data[1]);
            String reason = data[2];

            try {
                SQL.query(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("SELECT * FROM Mute WHERE uuid='%s'", uuid
                ), rs -> {
                    try {
                        if(rs.next()){
                            try {
                                SQL.update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("UPDATE Mute SET start='%s', end='%s', reason='%s' WHERE uuid='%s'",
                                        start, end, reason, uuid));

                            } catch (SQLException exception) {
                                exception.printStackTrace();
                            }
                        }
                        else
                        {
                            try {
                                SQL.update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("INSERT INTO Mute (uuid, start, end, reason) VALUES ('%s', '%s', '%s', '%s')",
                                        uuid, start, end, reason));
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
        getUUIDOfMuteFromMySQL();
        this.datamute = getDataOfMuteFromMySQL();
    }

    private void sendData(){
        sendDataofMuteToMySQL();
    }

    public void update(){
        sendData();
    }

    public void load(){
        getData();
    }
}
