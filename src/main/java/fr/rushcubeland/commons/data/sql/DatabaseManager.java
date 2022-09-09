package fr.rushcubeland.commons.data.sql;

public enum DatabaseManager {

    Main_BDD(new DatabaseCredentials("192.168.1.2", "****", "*******", "**", 3306));

    private final DatabaseAccess databaseAccess;

    DatabaseManager(DatabaseCredentials credentials){
        this.databaseAccess = new DatabaseAccess(credentials);
    }

    public DatabaseAccess getDatabaseAccess() {
        return databaseAccess;
    }

    public static void initAllDatabaseConnections() {
        for(DatabaseManager databaseManager : values()) {
            databaseManager.databaseAccess.initPool();
        }

    }

    public static void closeAllDatabaseConnection() {
        for(DatabaseManager databaseManager : values()) {
            databaseManager.databaseAccess.closePool();
        }

    }
}
