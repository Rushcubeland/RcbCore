package fr.rushcubeland.rcbcore.bukkit.friends;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Map;

public class FriendsGUIUpdater implements Runnable {

    @Override
    public void run() {
        for(Map.Entry<Player, Inventory> invs : FriendsGUI.getInvCache().entrySet()){
            if(invs.getKey() != null){
                FriendsGUI.generateInv(invs.getKey());
            }
        }
    }
}
