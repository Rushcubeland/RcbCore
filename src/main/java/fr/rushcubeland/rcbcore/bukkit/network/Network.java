package fr.rushcubeland.rcbcore.bukkit.network;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.rushcubeland.rcbcore.bukkit.RcbAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Network implements PluginMessageListener {

    public static void getCount(Player player, ServerUnit serverUnit) {
        String serverName = serverUnit.getName();
        if(serverName == null) {
            serverName = "ALL";
        }

        try {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("PlayerCount");
            out.writeUTF(serverName);
            player.sendPluginMessage(RcbAPI.getInstance(), "BungeeCord", out.toByteArray());
        }
        catch (NullPointerException nullPointerException){
            nullPointerException.getStackTrace();
        }
    }

    public static ServerUnit getBestServer(Player player, ServerGroup serverGroup){
        ArrayList<ServerUnit> servers = serverGroup.getServersInGroup();
        ServerUnit currentServer = servers.get(0);
        for(ServerUnit s : servers){
            if(s != currentServer && !player.getServer().getName().equals(s.getName()) && !getMOTD(s).equalsIgnoreCase("INPROGRESS") &&
                    (s.getSlots() > currentServer.getSlots()) && !s.getMaxPlayers().equals(s.getSlots())){
                currentServer = s;
            }
        }
        return currentServer;
    }

    public static String getMOTD(ServerUnit serverUnit) {

        try {

            Socket sock = new Socket();
            sock.setSoTimeout(100);
            sock.connect(new InetSocketAddress(serverUnit.getIp(), serverUnit.getPort()), 100);
            DataOutputStream out = new DataOutputStream(sock.getOutputStream());
            DataInputStream in = new DataInputStream(sock.getInputStream());

            out.write(254);

            StringBuffer str = new StringBuffer(); int b;
            while ((b = in.read()) != -1) {
                if (b != 0 && b > 16 && b != 255 && b != 23 && b != 24) {
                    str.append((char)b);
                }
            }

            String[] data = str.toString().split(String.valueOf(ChatColor.RESET));
            String serverMotd = data[0];

            sock.close();

            return String.format(serverMotd);

        }
        catch (ConnectException e) {
            return "ConnectException";
        } catch (UnknownHostException e) {
            return "UnknownHostException";
        } catch (IOException e) {
            return "IOException";
        }
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();

        if(subchannel.equals("PlayerCount")) {
            String server = in.readUTF();
            for(ServerUnit serverUnit : ServerUnit.values()){
                if(server.equals(serverUnit.getName())){
                    serverUnit.setSlots(in.readInt());
                }
            }
        }
    }

    public static void sendPlayerToServer(Player player, ServerUnit serverUnit){
        String serverName = serverUnit.getName();
        try
        {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF(serverName);
            player.sendPluginMessage(RcbAPI.getInstance(), "BungeeCord", out.toByteArray());
        }
        catch (NullPointerException localNullPointerException) {
            localNullPointerException.printStackTrace();
        }
    }

    private static void updateSlotsServers(){
        for(ServerUnit serverUnit : ServerUnit.values()){
            try
            {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("PlayerCount");
                out.writeUTF(serverUnit.getName());
                Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
                if(player == null){
                    return;
                }
                player.sendPluginMessage(RcbAPI.getInstance(), "BungeeCord", out.toByteArray());
            }
            catch (NullPointerException localNullPointerException) {
                localNullPointerException.printStackTrace();
            }
        }
    }

    public static void startTaskUpdateSlotsServer(){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(RcbAPI.getInstance(), Network::updateSlotsServers,0L, 60L);
    }

    public static Integer getNetworkSlots(){
        int slots = 0;
        for(ServerUnit server : ServerUnit.values()){
            slots = slots+server.getSlots();
        }
        return slots;
    }

}
