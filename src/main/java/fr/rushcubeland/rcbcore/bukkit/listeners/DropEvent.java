package fr.rushcubeland.rcbcore.bukkit.listeners;

import fr.rushcubeland.rcbcore.bukkit.mod.ModModerator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class DropEvent implements Listener {

    @EventHandler
    public void onDrop(PlayerDropItemEvent e){
        Player player = (Player) e.getPlayer();
        if(ModModerator.isInModData(player.getUniqueId().toString())){
            e.setCancelled(true);
        }
    }
}
