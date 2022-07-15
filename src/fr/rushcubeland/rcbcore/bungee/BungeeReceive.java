package fr.rushcubeland.rcbcore.bungee;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import fr.rushcubeland.rcbcore.bungee.queue.Queue;
import fr.rushcubeland.rcbcore.bungee.queue.QueueUnit;
import fr.rushcubeland.rcbcore.bungee.report.Report;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class BungeeReceive implements Listener {


    @EventHandler
    public void on(PluginMessageEvent event){
        if (!event.getTag().equalsIgnoreCase(RcbAPI.getInstance().getChannel())){
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
        String subChannel = in.readUTF();

        if(subChannel.equalsIgnoreCase( "Ban")){
            String uuid = in.readUTF();
            long durationSeconds = in.readLong();
            String reason = in.readUTF();
            String modName = in.readUTF();
            RcbAPI.getInstance().getBanManager().ban(UUID.fromString(uuid), durationSeconds, reason, modName);

        }
        if(subChannel.equalsIgnoreCase("Mute")){
            String uuid = in.readUTF();
            long durationSeconds = in.readLong();
            String reason = in.readUTF();
            String modName = in.readUTF();
            RcbAPI.getInstance().getMuteManager().mute(UUID.fromString(uuid), durationSeconds, reason, modName);

        }
        if(subChannel.equalsIgnoreCase("Kick")){
            String targetname = in.readUTF();
            String reason = in.readUTF();
            ProxyServer.getInstance().getPlayer(targetname).disconnect(new TextComponent("§cVous avez été kick ! \n \n §6Raison: §e" + reason));
        }
        if(subChannel.equalsIgnoreCase("CmdProxy")){
            String targetName = in.readUTF();
            String cmd = in.readUTF();
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(targetName);
            if(player != null){
                ProxyServer.getInstance().getPluginManager().dispatchCommand(player, cmd);
            }
        }
        if(subChannel.equalsIgnoreCase("ReportMSG")){
            String playerName = in.readUTF();
            String targetName = in.readUTF();
            String message = in.readUTF();
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerName);
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(targetName);
            if(player != null && target != null){
                Report.reportMsg(player, target, message);
            }
        }
        if(subChannel.equalsIgnoreCase("RequestQueue")){
            String playerName = in.readUTF();
            String queueName = in.readUTF();
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerName);
            QueueUnit queueUnit = QueueUnit.getByName(queueName);
            if(player != null && queueUnit != null){
                Queue.checkForJoinQueue(player, queueUnit);
            }
        }
    }
}

