package fr.rushcubeland.rcbcore.bungee.maintenance;

import fr.rushcubeland.commons.data.sql.DatabaseManager;
import fr.rushcubeland.commons.data.sql.SQL;
import fr.rushcubeland.rcbcore.bungee.utils.UUIDFetcher;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class Maintenance {

    private ArrayList<UUID> whitelist = new ArrayList<>();
    private MaintenanceMode maintenanceMode;

    public static Maintenance INSTANCE;
    private static boolean present;
    private static final String reason = "§cUne maintenance est en cours ! \n \n §cMerci de réessayer plus tard";

    public Maintenance() {
        INSTANCE = this;
    }

    public MaintenanceMode getMaintenanceMode() {
        return maintenanceMode;
    }

    public void setMaintenanceMode(MaintenanceMode maintenanceMode) {
        this.maintenanceMode = maintenanceMode;
    }

    public ArrayList<UUID> getWhitelist() {
        return whitelist;
    }

    public void addPlayer(String playerName){
        String suuid = UUIDFetcher.getUUIDFromName(playerName);
        if(suuid != null){
            UUID uuid = UUID.fromString(suuid);
            if(!whitelist.contains(uuid)){
                whitelist.add(uuid);
            }
        }
    }

    public void addPlayer(ProxiedPlayer player){
        if(player != null){
            UUID uuid = player.getUniqueId();
            if(!whitelist.contains(uuid)){
                whitelist.add(uuid);
            }
        }
    }

    public void addPlayer(UUID uuid){
        if(uuid != null && !whitelist.contains(uuid)){
            whitelist.add(uuid);
        }
    }

    public boolean isIn(UUID uuid){
        if(uuid != null){
            return whitelist.contains(uuid);
        }
        return false;
    }

    public boolean isIn(String playerName){
        String suuid = UUIDFetcher.getUUIDFromName(playerName);
        if(suuid != null){
            UUID uuid = UUID.fromString(suuid);
            return whitelist.contains(uuid);
        }
        return false;
    }

    public boolean isIn(ProxiedPlayer player){
        if(player != null){
            UUID uuid = player.getUniqueId();
            return whitelist.contains(uuid);
        }
        return false;
    }

    public void removePlayer(String playerName){
        String suuid = UUIDFetcher.getUUIDFromName(playerName);
        if(suuid != null){
            UUID uuid = UUID.fromString(suuid);
            whitelist.remove(uuid);
        }
    }

    public void removePlayer(ProxiedPlayer player){
        if(player != null){
            UUID uuid = player.getUniqueId();
            whitelist.remove(uuid);
        }
    }

    public void removePlayer(UUID uuid){
        if(uuid != null){
            whitelist.remove(uuid);
        }
    }

    public void setWhitelist(ArrayList<UUID> whitelist) {
        this.whitelist = whitelist;
    }

    public static void initMaintenanceSystem(){
        new Maintenance();
        Maintenance.INSTANCE.setWhitelist(initPlayers());
        if(getMaintenanceStateFromDB()){
            Maintenance.INSTANCE.setMaintenanceMode(MaintenanceMode.ON);
        }
        else
        {
            Maintenance.INSTANCE.setMaintenanceMode(MaintenanceMode.OFF);
        }
    }

    public static void stopMaintenanceSystem(){
        if(present){

            try {
                SQL.update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("UPDATE Maintenance SET state='%s' WHERE id='%s'",
                        Maintenance.INSTANCE.getMaintenanceMode().getValue(), 1));

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else
        {
            try {
                SQL.update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("INSERT INTO Maintenance (state) VALUES ('%s')",
                        Maintenance.INSTANCE.getMaintenanceMode().getValue()));

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        savePlayers();
    }

    private static ArrayList<UUID> initPlayers(){
        ArrayList<String> list = getAllDataFromDB();
        ArrayList<UUID> value = new ArrayList<>();
        for(String s : list){
            value.add(UUID.fromString(s));
        }
        return value;
    }

    private static void savePlayers(){
        ArrayList<String> value = getAllDataFromDB();
        for(String s : value){
            UUID uuid = UUID.fromString(s);
            if(!Maintenance.INSTANCE.getWhitelist().contains(uuid)){
                removeFromDB(uuid);
            }
        }
        for(UUID uuid : Maintenance.INSTANCE.getWhitelist()){
            if(!isInDB(uuid)){
                addToDB(uuid);
            }
        }
    }

    private static void addToDB(UUID uuid){

        try {

            SQL.update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("INSERT INTO MaintenanceList (uuid) VALUES ('%s')",
                    uuid));

        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    private static void removeFromDB(UUID uuid){

        try {

            SQL.update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("DELETE FROM MaintenanceList WHERE uuid='%s'",
                    uuid));

        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    private static ArrayList<String> getAllDataFromDB(){
        ArrayList<String> value = new ArrayList<>();

        try {

            final Connection connection = DatabaseManager.Main_BDD.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM MaintenanceList");

            preparedStatement.executeQuery();

            final ResultSet rs = preparedStatement.getResultSet();

            while (rs.next()){

                value.add(rs.getString("uuid"));

            }

            connection.close();

        } catch (SQLException e){
            e.printStackTrace();
        }

        return value;
    }

    private static boolean isInDB(UUID uuid){

        boolean value = false;

        try {

            final Connection connection = DatabaseManager.Main_BDD.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM MaintenanceList WHERE uuid=?");

            preparedStatement.setString(1, uuid.toString());
            preparedStatement.executeQuery();

            final ResultSet rs = preparedStatement.getResultSet();

            if(rs.next()){

                value = true;

            }

            connection.close();

        } catch (SQLException e){
            e.printStackTrace();
        }

        return value;
    }

    private static void putUUIDToDB(UUID uuid){

        try {
            SQL.update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("INSERT INTO MaintenanceList (uuid) VALUES ('%s')",
                    uuid.toString()));

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    private static boolean getMaintenanceStateFromDB() {

        boolean state = false;

        try {

            final Connection connection = DatabaseManager.Main_BDD.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Maintenance WHERE id=?");

            preparedStatement.setInt(1, 1);
            preparedStatement.executeQuery();

            final ResultSet rs = preparedStatement.getResultSet();

            if(rs.next()){

                state = rs.getBoolean("state");
                present = true;

            }
            else
            {
                present = false;
            }
            preparedStatement.close();
            connection.close();

        } catch (SQLException e){
            e.printStackTrace();
        }

        return state;
    }

    public static String getReason() {
        return reason;
    }
}
