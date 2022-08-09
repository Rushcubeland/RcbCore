package fr.rushcubeland.rcbcore.bungee.network;

import fr.rushcubeland.commons.utils.MessageUtil;
import fr.rushcubeland.rcbcore.bungee.RcbAPI;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Network {

    public static final HashMap<ServerUnit, Boolean> serverStatus = new HashMap<>();

    public static void joinLobby(ProxiedPlayer player){
        if(!player.getServer().getInfo().getName().startsWith("Lobby")){
            ServerUnit best = Network.getBestServer(player, ServerGroup.Lobby);
            if(best != null){
                player.connect(best.getServerInfo());
            }
        }
        else
        {
            player.sendMessage(new TextComponent(MessageUtil.ALREADY_CONNECTED_TO_LOBBY.getMessage()));
        }
    }
    public static ServerUnit getBestServer(ProxiedPlayer player, ServerGroup serverGroup){
        ArrayList<ServerUnit> servers = serverGroup.getServersInGroup();
        ServerUnit currentServer = null;
        boolean first = true;
        for(ServerUnit s : servers){
            if(!player.getServer().getInfo().getName().equals(s.getName()) && !s.getServerInfo().getMotd().equalsIgnoreCase("INPROGRESS") && serverStatus.get(s)){
                if(first){
                    currentServer = s;
                    first = false;
                }
                if((s.getSlots() > currentServer.getSlots()) && !s.getMaxPlayers().equals(s.getSlots())){
                    currentServer = s;
                }
            }
        }
        if(currentServer == null){
            return null;
        }
        if(serverStatus.get(currentServer).equals(false) || currentServer.getServerInfo().equals(player.getServer().getInfo())){
            return null;
        }
        return currentServer;
    }

    private static void updateServers(){
        for(ServerUnit serverUnit : ServerUnit.values()){
            serverUnit.update();
            checkServerState(serverUnit);
        }
    }

    private static void checkServerState(ServerUnit serverUnit){
        try {
            Socket s = new Socket();
            s.connect(new InetSocketAddress(serverUnit.getIp(), serverUnit.getPort()), 10);
            s.close();
            serverStatus.replace(serverUnit, true);
        } catch (Exception e) {
            serverStatus.replace(serverUnit, false);
        }
    }

    public static void startUpdateServersTask(){
        ProxyServer.getInstance().getScheduler().schedule(RcbAPI.getInstance(), Network::updateServers, 0L, 3L, TimeUnit.SECONDS);
    }

    public static void sendPlayerToServer(ProxiedPlayer player, ServerUnit serverUnit){
        if(player != null){
            if(serverUnit != null){
                player.connect(serverUnit.getServerInfo());
            }
            else
            {
                player.sendMessage(new TextComponent(MessageUtil.NO_SERVERS_FOUND.getMessage()));
            }
        }
    }
}
