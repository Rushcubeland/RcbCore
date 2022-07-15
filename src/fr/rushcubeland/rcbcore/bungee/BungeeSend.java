package fr.rushcubeland.rcbcore.bungee;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.Map;

public class BungeeSend {

    public static void teleport(ProxiedPlayer from, ProxiedPlayer to) {
        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteArrayOut);

        try {
            out.writeUTF("Teleport");
            out.writeUTF(from.getName());
            out.writeUTF(to.getName());

            from.getServer().getInfo()
                    .sendData(RcbAPI.channel, byteArrayOut.toByteArray());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendPunishmentGui(ProxiedPlayer player, String target){
        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteArrayOut);

        try {
            out.writeUTF("PunishGUI");
            out.writeUTF(player.getName());
            out.writeUTF(target);

            player.getServer().getInfo()
                    .sendData(RcbAPI.channel, byteArrayOut.toByteArray());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendPunishmentGuiMsg(ProxiedPlayer player, String target){
        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteArrayOut);

        try {
            out.writeUTF("PunishGUIMsg");
            out.writeUTF(player.getName());
            out.writeUTF(target);

            player.getServer().getInfo()
                    .sendData(RcbAPI.channel, byteArrayOut.toByteArray());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendMuteDataAdd(String targetUUID){
        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteArrayOut);

        try {
            out.writeUTF("MuteDataAdd");
            out.writeUTF(targetUUID);

            for(Map.Entry servers : ProxyServer.getInstance().getServers().entrySet()){
                ServerInfo serverInfo = (ServerInfo) servers.getValue();
                serverInfo.sendData(RcbAPI.channel, byteArrayOut.toByteArray());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendMuteDataRemove(String targetUUID){
        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteArrayOut);

        try {
            out.writeUTF("MuteDataRemove");
            out.writeUTF(targetUUID);

            for(Map.Entry servers : ProxyServer.getInstance().getServers().entrySet()){
                ServerInfo serverInfo = (ServerInfo) servers.getValue();
                serverInfo.sendData(RcbAPI.channel, byteArrayOut.toByteArray());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendModModeratorDataRemove(String modUUID){
        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteArrayOut);

        try {
            out.writeUTF("ModModeratorRemove");
            out.writeUTF(modUUID);

            for(Map.Entry servers : ProxyServer.getInstance().getServers().entrySet()){
                ServerInfo serverInfo = (ServerInfo) servers.getValue();
                serverInfo.sendData(RcbAPI.channel, byteArrayOut.toByteArray());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendModModeratorDataAdd(String modUUID){
        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteArrayOut);

        try {
            out.writeUTF("ModModeratorAdd");
            out.writeUTF(modUUID);

            for(Map.Entry servers : ProxyServer.getInstance().getServers().entrySet()){
                ServerInfo serverInfo = (ServerInfo) servers.getValue();
                serverInfo.sendData(RcbAPI.channel, byteArrayOut.toByteArray());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendGUIFriends(ProxiedPlayer player){
        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteArrayOut);

        try {
            out.writeUTF("FriendsGUI");
            out.writeUTF(player.getName());

            player.getServer().getInfo()
                    .sendData(RcbAPI.channel, byteArrayOut.toByteArray());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendGUIOptions(ProxiedPlayer player){
        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteArrayOut);

        try {
            out.writeUTF("OptionsGUI");
            out.writeUTF(player.getName());

            player.getServer().getInfo()
                    .sendData(RcbAPI.channel, byteArrayOut.toByteArray());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
