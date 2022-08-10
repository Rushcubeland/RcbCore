package fr.rushcubeland.rcbcore.bukkit;

import fr.rushcubeland.rcbcore.bukkit.friends.FriendsGUI;
import fr.rushcubeland.rcbcore.bukkit.mod.ModModerator;
import fr.rushcubeland.rcbcore.bukkit.options.Options;
import fr.rushcubeland.rcbcore.bukkit.sanction.MuteData;
import fr.rushcubeland.rcbcore.bukkit.sanction.SanctionGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.ArrayList;

public class BukkitReceive implements PluginMessageListener, Listener {

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals(RcbAPI.channel))
            return;
        String action = null;

        ArrayList<String> received = new ArrayList<>();

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));

        try {
            action = in.readUTF();

            while (in.available() > 0) {
                received.add(in.readUTF());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if (action == null)
            return;
        if(action.equalsIgnoreCase("Teleport")){
            Player from = Bukkit.getServer().getPlayer(received.get(0));
            Player to = Bukkit.getServer().getPlayer(received.get(1));
            if(from == null){
                return;
            }
            if(to == null){
                from.sendMessage("§cLe joueur s'est déconnecté !");
                return;
            }
            from.teleport(to);
        }
        if(action.equalsIgnoreCase("PunishGUI")){
            Player mod = Bukkit.getServer().getPlayer(received.get(0));
            String target = received.get(1);
            SanctionGUI.getModAndTarget().put(mod, target);
            SanctionGUI.openModGui(mod, target);

        }
        if(action.equalsIgnoreCase("PunishGUIMsg")){
            Player mod = Bukkit.getServer().getPlayer(received.get(0));
            String target = received.get(1);
            if (mod != null) {
                SanctionGUI.getModAndTarget().put(mod, target);
                SanctionGUI.openMsgGui(mod, target);
            }

        }
        if(action.equalsIgnoreCase("MuteDataAdd")){
            String targetName = received.get(0);
            MuteData.addMute(targetName);
        }
        if(action.equalsIgnoreCase("ModModeratorAdd")){
            String modUUID = received.get(0);
            ModModerator.addMod(modUUID);
        }
        if(action.equalsIgnoreCase("MuteDataRemove")){
            String targetName = received.get(0);
            MuteData.removeMute(targetName);
        }
        if(action.equalsIgnoreCase("ModModeratorRemove")){
            String modUUID = received.get(0);
            ModModerator.removeMod(modUUID);
        }
        if(action.equalsIgnoreCase("FriendsGUI")){
            Player player1 = Bukkit.getPlayer(received.get(0));
            if(player1 != null){
                FriendsGUI.openFriendsGUI(player1);
            }
        }
        if(action.equalsIgnoreCase("OptionsGUI")){
            Player player1 = Bukkit.getPlayer(received.get(0));
            if(player1 != null){
                Options.openOptionsInv(player1);
            }
        }
    }

}
