package fr.rushcubeland.rcbcore.bungee.queue;

import fr.rushcubeland.rcbcore.bungee.RcbAPI;
import fr.rushcubeland.rcbcore.bungee.network.Network;
import fr.rushcubeland.rcbcore.bungee.network.ServerGroup;
import fr.rushcubeland.rcbcore.bungee.network.ServerUnit;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public enum QueueUnit {

    DE_A_COUDRE("§bDé à coudre", ServerGroup.DE_A_COUDRE, -1);

    private final String name;
    private final ArrayList<String> serversInGroup = new ArrayList<>();
    private final int playersMax;
    private final ServerGroup serverGroup;
    private ScheduledTask task;

    private final ArrayList<ProxiedPlayer> playersInQueue = new ArrayList<>();

    QueueUnit(String name, ServerGroup serverGroup, Integer playersMax){
        this.name = name;
        this.playersMax = playersMax;
        this.serverGroup = serverGroup;
    }

    public static QueueUnit getByName(String name){
        return Arrays.stream(values()).filter(r -> r.getName().equalsIgnoreCase(name)).findAny().orElse(null);
    }

    public String getName() {
        return name;
    }

    public Integer getMaxPlayer(){
        return playersMax;
    }

    public ArrayList<ProxiedPlayer> getPlayers(){
        return playersInQueue;
    }

    public ArrayList<String> getTargetServers(){
        return serversInGroup;
    }

    public ServerGroup getServerGroup(){
        return serverGroup;
    }

    public ScheduledTask getTask(){
        return task;
    }

    public void startTask(){

        task = ProxyServer.getInstance().getScheduler().schedule(RcbAPI.getInstance(), () -> {

            if(getPlayers().size() >= 1){
                ProxiedPlayer player = getPlayers().get(0);
                ServerUnit bestTargetServer = Network.getBestServer(player, getServerGroup());
                Network.sendPlayerToServer(player, bestTargetServer);
                if(getPlayers().isEmpty()){
                    return;
                }
                getPlayers().remove(0);
            }

        }, 1L, 1L, TimeUnit.SECONDS);

    }

    public void stopTask(){
        ProxyServer.getInstance().getScheduler().cancel(task);
    }

}
