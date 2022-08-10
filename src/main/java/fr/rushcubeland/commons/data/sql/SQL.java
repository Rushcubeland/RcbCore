package fr.rushcubeland.commons.data.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.function.Function;

public class SQL {

    public static void update(Connection connection, String qry) throws SQLException {
        PreparedStatement s = connection.prepareStatement(qry);
        s.executeUpdate();
        s.close();
        connection.close();
    }

    public static Object query(Connection connection, String qry, Function<ResultSet, Object> consumer) throws SQLException {
        PreparedStatement s = connection.prepareStatement(qry);
        ResultSet rs = s.executeQuery();
        s.close();
        connection.close();
        return consumer.apply(rs);
    }

    public static void query(Connection connection, String qry, Consumer<ResultSet> consumer) throws SQLException {
        PreparedStatement s = connection.prepareStatement(qry);
        ResultSet rs = s.executeQuery();
        consumer.accept(rs);
        s.close();
        connection.close();
    }

    public static void createTables() {

        try {

            update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), "CREATE TABLE IF NOT EXISTS StatsDAC (" +
                    "`id` INT NOT NULL AUTO_INCREMENT, " +
                    "uuid VARCHAR(255) PRIMARY KEY, " +
                    "wins INT, " +
                    "loses INT, " +
                    "nbParties INT, " +
                    "nbSortsUsed INT, " +
                    "nbJumps INT, " +
                    "nbSuccessJumps INT, " +
                    "nbFailJumps INT, " +
                    "INDEX (id));");

            update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), "CREATE TABLE IF NOT EXISTS MaintenanceList (" +
                    "`id` INT NOT NULL AUTO_INCREMENT, " +
                    "uuid VARCHAR(255) PRIMARY KEY, " +
                    "INDEX (id));");

            update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), "CREATE TABLE IF NOT EXISTS Maintenance (" +
                    "`id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    "state BOOLEAN)");

            update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), "CREATE TABLE IF NOT EXISTS Cosmetics (" +
                    "`id` INT NOT NULL AUTO_INCREMENT, " +
                    "uuid VARCHAR(255) PRIMARY KEY, " +
                    "current_particles VARCHAR(255), " +
                    "current_pet VARCHAR(255), "+
                    "INDEX (id));");

            update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), "CREATE TABLE IF NOT EXISTS Stats (" +
                    "`id` INT NOT NULL AUTO_INCREMENT, " +
                    "uuid VARCHAR(255) PRIMARY KEY, " +
                    "parcours_timer BIGINT, " +
                    "firstconnection_ms BIGINT, " +
                    "lastconnection_ms BIGINT, " +
                    "INDEX (id));");

            update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), "CREATE TABLE IF NOT EXISTS Accounts (" +
                    "`id` INT NOT NULL AUTO_INCREMENT, " +
                    "uuid VARCHAR(255) PRIMARY KEY, " +
                    "primaryRank VARCHAR(255), " +
                    "secondaryRank VARCHAR(255), " +
                    "primaryRank_end BIGINT, " +
                    "secondaryRank_end BIGINT, " +
                    "coins BIGINT, " +
                    "INDEX (id));");

            update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), "CREATE TABLE IF NOT EXISTS Friends (" +
                    "`id` INT NOT NULL AUTO_INCREMENT, " +
                    "uuid VARCHAR(255), " +
                    "friend VARCHAR(255), " +
                    "PRIMARY KEY(uuid, friend), " +
                    "INDEX (id));");

            update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), "CREATE TABLE IF NOT EXISTS Options (" +
                    "`id` INT NOT NULL AUTO_INCREMENT, " +
                    "uuid VARCHAR(255) PRIMARY KEY, " +
                    "state_party_invite VARCHAR(16), " +
                    "state_friend_requests VARCHAR(16), " +
                    "state_chat VARCHAR(16), " +
                    "state_friends_statut_notif VARCHAR(16), " +
                    "state_private_msg VARCHAR(16), " +
                    "INDEX (id));");

            update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), "CREATE TABLE IF NOT EXISTS Player_permissions (" +
                    "`id` INT NOT NULL AUTO_INCREMENT, " +
                    "uuid VARCHAR(255), " +
                    "permission VARCHAR(255), " +
                    "PRIMARY KEY(uuid, permission), " +
                    "INDEX (id));");

            update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), "CREATE TABLE IF NOT EXISTS Rank_permissions (" +
                    "`id` INT NOT NULL AUTO_INCREMENT, " +
                    "grade VARCHAR(255), " +
                    "permission VARCHAR(255)," +
                    "PRIMARY KEY(grade, permission), " +
                    "INDEX (id));");

            update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), "CREATE TABLE IF NOT EXISTS Bans (" +
                    "`id` INT NOT NULL AUTO_INCREMENT, " +
                    "uuid VARCHAR(255) PRIMARY KEY, " +
                    "start BIGINT, " +
                    "end BIGINT, " +
                    "reason VARCHAR(64), " +
                    "moderator VARCHAR(32), " +
                    "INDEX (id));");

            update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), "CREATE TABLE IF NOT EXISTS Sanctions_history (" +
                    "`id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    "uuid VARCHAR(255), " +
                    "type VARCHAR(20), " +
                    "date_time LONG, " +
                    "duration LONG, " +
                    "moderator VARCHAR(32), " +
                    "INDEX (id));");

            update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), "CREATE TABLE IF NOT EXISTS Mute (" +
                    "`id` INT NOT NULL AUTO_INCREMENT, " +
                    "uuid VARCHAR(255) PRIMARY KEY, " +
                    "start BIGINT, " +
                    "end BIGINT, " +
                    "reason VARCHAR(64), " +
                    "INDEX (id));");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
