package fr.rushcubeland.rcbcore.bukkit.friends;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public class FriendsGUIUpdater implements Runnable {

    @Override
    public void run() {
        HashMap<Player, Inventory> map =  FriendsGUI.getInvCache();
        for(Map.Entry<Player, Inventory> invs : map.entrySet()){
            if(invs.getKey() != null){
                FriendsGUI.generateInv(invs.getKey());
            }
        }
    }
}
