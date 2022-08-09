package fr.rushcubeland.rcbcore.bukkit;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.rushcubeland.commons.data.callbacks.AsyncCallBack;
import fr.rushcubeland.rcbcore.bukkit.utils.UUIDFetcher;
import fr.rushcubeland.rcbcore.bukkit.queue.QueueUnit;
import org.bukkit.entity.Player;

public class BukkitSend {

    public static void banToProxy(Player player, String targetname, long durationSeconds, String reason){
        UUIDFetcher.getUUIDFromName(targetname, new AsyncCallBack() {
            @Override
            public void onQueryComplete(Object result) {
                String uuid = (String) result;
                if(uuid != null){
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("Ban");
                    out.writeUTF(uuid);
                    out.writeLong(durationSeconds);
                    out.writeUTF(reason);
                    out.writeUTF(player.getName());
                    player.sendPluginMessage(RcbAPI.getInstance(), RcbAPI.channel, out.toByteArray());
                }
            }
        });
    }

    public static void muteToProxy(Player player, String targetname, long durationSeconds, String reason, String modName){
        UUIDFetcher.getUUIDFromName(targetname, new AsyncCallBack() {
            @Override
            public void onQueryComplete(Object result) {
                String uuid = (String) result;
                if(uuid != null){
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("Mute");
                    out.writeUTF(uuid);
                    out.writeLong(durationSeconds);
                    out.writeUTF(reason);
                    out.writeUTF(modName);
                    player.sendPluginMessage(RcbAPI.getInstance(), RcbAPI.channel, out.toByteArray());
                }
            }
        });
    }

    public static void kickToProxy(Player player, String targetname, String reason){
        try {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Kick");
            out.writeUTF(targetname);
            out.writeUTF(reason);
            player.sendPluginMessage(RcbAPI.getInstance(), RcbAPI.channel, out.toByteArray());
        }
        catch (NullPointerException nullPointerException){
            nullPointerException.getStackTrace();
        }
    }

    public static void cmdToProxy(Player player, String cmd){
        try {

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("CmdProxy");
            out.writeUTF(player.getName());
            out.writeUTF(cmd);
            player.sendPluginMessage(RcbAPI.getInstance(), RcbAPI.channel, out.toByteArray());
        }
        catch (NullPointerException nullPointerException){
            nullPointerException.getStackTrace();
        }
    }

    public static void reportMsgToProxy(Player player, String targetName, String message){
        try {

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("ReportMSG");
            out.writeUTF(player.getName());
            out.writeUTF(targetName);
            out.writeUTF(message);
            player.sendPluginMessage(RcbAPI.getInstance(), RcbAPI.channel, out.toByteArray());
        }
        catch (NullPointerException nullPointerException){
            nullPointerException.getStackTrace();
        }
    }

    public static void requestJoinQueue(Player player, QueueUnit queue){
        try {

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("RequestQueue");
            out.writeUTF(player.getName());
            out.writeUTF(queue.getName());

            player.sendPluginMessage(RcbAPI.getInstance(), RcbAPI.channel, out.toByteArray());
        }
        catch (NullPointerException nullPointerException){
            nullPointerException.getStackTrace();
        }
    }
}
