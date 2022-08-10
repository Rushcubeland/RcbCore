package fr.rushcubeland.rcbcore.bukkit.listeners;

import fr.rushcubeland.rcbcore.bukkit.commands.ReportMsgCommand;
import fr.rushcubeland.rcbcore.bukkit.friends.FriendsGUI;
import fr.rushcubeland.rcbcore.bukkit.mod.ModModerator;
import fr.rushcubeland.rcbcore.bukkit.tools.NPC;
import fr.rushcubeland.rcbcore.bukkit.tools.PacketReader;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachment;

public class QuitEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent e){
        Player player = e.getPlayer();

        for(NPC npc : NPC.getNPCs()){
            npc.destroy(player);
        }

        PacketReader reader = new PacketReader();
        reader.uninject(player);

        if(ModModerator.isInModData(player.getUniqueId().toString())){
            e.setQuitMessage(null);
        }

        FriendsGUI.getInvCache().remove(player);

        if(JoinEvent.permissionMap.containsKey(player.getUniqueId())){
            PermissionAttachment attachment = JoinEvent.permissionMap.get(player.getUniqueId());
            if(attachment != null){
                player.removeAttachment(attachment);
            }
            JoinEvent.permissionMap.remove(player.getUniqueId());
        }
        ReportMsgCommand.dataPlayers.remove(player);
    }
}
