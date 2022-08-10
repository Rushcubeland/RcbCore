package fr.rushcubeland.rcbcore.bungee.sanctions;

import fr.rushcubeland.commons.data.sql.DatabaseManager;
import fr.rushcubeland.commons.data.sql.SQL;

import javax.annotation.Nullable;
import java.sql.SQLException;
import java.util.UUID;

public class SanctionHistory {

    public static void addSanctionHistory(UUID uuid, String type, long time, @Nullable Long duration, String modName){
        try {
            SQL.update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("INSERT INTO Sanctions_history (uuid, type, date_time, duration, moderator) VALUES ('%s', '%s', '%s', '%s', '%s')",
                    uuid.toString(), type, time, duration, modName));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
