package fr.rushcubeland.commons.data.sql;

public class DatabaseCredentials {

    private final String host;
    private final String user;
    private final String pass;
    private final String dbName;
    private final int port;

    public DatabaseCredentials(String host, String user, String pass, String dbName, int port){
        this.host = host;
        this.user = user;
        this.pass = pass;
        this.dbName = dbName;
        this.port = port;
    }

    public String toURI(){
        final StringBuilder sb = new StringBuilder();

        sb.append("jdbc:mysql://")
                .append(host)
                .append(":")
                .append(port)
                .append("/")
                .append(dbName);

        return sb.toString();
    }

    public String getUser() {
        return user;
    }

    public String getHost() {
        return host;
    }

    public String getPass() {
        return pass;
    }

    public int getPort() {
        return port;
    }

    public String getDbName() {
        return dbName;
    }
}
