package fr.rushcubeland.rcbcore.bukkit.mod;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ModModeratorTask implements Runnable {

    @Override
    public void run() {
        for(Player pls : Bukkit.getOnlinePlayers()){
            if(ModModerator.isInModData(pls.getUniqueId().toString())){
                pls.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§6Mode modérateur"));
            }
        }
    }
}
