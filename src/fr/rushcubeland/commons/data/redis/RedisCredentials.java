package fr.rushcubeland.commons.data.redis;

public class RedisCredentials {

    private final String ip;
    private final String password;
    private final int port;
    private final String clientName;

    public RedisCredentials(String ip, String password, int port, String clientName) {
        this.ip = ip;
        this.password = password;
        this.port = port;
        this.clientName = clientName;
    }

    public RedisCredentials(String ip, String password, int port) {
        this(ip, password, port, "Redis_bungee");
    }

    public String getIp() {
        return ip;
    }

    public String getPassword() {
        return password;
    }

    public int getPort() {
        return port;
    }

    public String getClientName() {
        return clientName;
    }

    public String toRedisURL(){
        return "redis://" + ip + ":" + port;
    }
}
