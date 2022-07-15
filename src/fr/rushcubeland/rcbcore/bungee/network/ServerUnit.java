package fr.rushcubeland.rcbcore.bungee.network;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.Arrays;
import java.util.Optional;

public enum ServerUnit {

    Lobby_1("Lobby", ServerGroup.Lobby, 500, "127.0.0.1", 25566),
    De_a_coudre_1("De_a_coudre_1", ServerGroup.DE_A_COUDRE, 16, "127.0.0.1", 25567),
    De_a_coudre_2("De_a_coudre_2", ServerGroup.DE_A_COUDRE, 16, "127.0.0.1", 25568);

    private final String name;
    private final int maxPlayers;
    private final ServerGroup serverGroup;
    private final int port;
    private int slots;
    private final String ip;
    private ServerInfo serverInfo;

    ServerUnit(String name, ServerGroup serverGroup, int maxPlayers, String ip, int port){
        this.name = name;
        this.maxPlayers = maxPlayers;
        this.serverGroup = serverGroup;
        this.port = port;
        this.ip = ip;
        serverInfo = ProxyServer.getInstance().getServers().get(name);
        Network.serverStatus.put(this, false);
    }

    public static Optional<ServerUnit> getByName(String name){
        return Arrays.stream(values()).filter(r -> r.getName().equalsIgnoreCase(name)).findFirst();
    }

    public static Optional<ServerUnit> getByPort(int port){
        return Arrays.stream(values()).filter(r -> r.getPort() == port).findFirst();
    }

    public ServerInfo getServerInfo() {
        return serverInfo;
    }

    public String getName() {
        return name;
    }

    public Integer getMaxPlayers(){
        return maxPlayers;
    }

    public ServerGroup getServerGroup(){
        return serverGroup;
    }

    public Integer getPort(){
        return port;
    }

    public void update(){
        serverInfo.ping((serverPing, throwable) -> {
            if(serverPing != null){
                this.slots = serverPing.getPlayers().getOnline();
            }
        });
        this.serverInfo = ProxyServer.getInstance().getServers().get(name);
    }

    public Integer getSlots(){
        return slots;
    }

    public String getIp() {
        return ip;
    }
}
