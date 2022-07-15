package fr.rushcubeland.rcbcore.bukkit.listeners;

import fr.rushcubeland.rcbcore.bukkit.mod.ModModerator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BreackBlock implements Listener {

    @EventHandler
    public void onBreack(BlockBreakEvent e){
        Player player = e.getPlayer();
        if(ModModerator.isInModData(player.getUniqueId().toString())){
            e.setCancelled(true);
        }
    }
}
