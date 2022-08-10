package fr.rushcubeland.rcbcore.bukkit.listeners;

import fr.rushcubeland.rcbcore.bukkit.mod.ModModerator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodChange implements Listener {

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent e){
        if(e.getEntity() instanceof Player){
            Player player = (Player) e.getEntity();
            if(ModModerator.isInModData(player.getUniqueId().toString())){
                e.setCancelled(true);
            }
        }
    }
}
