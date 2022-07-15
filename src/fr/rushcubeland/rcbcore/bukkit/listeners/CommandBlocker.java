package fr.rushcubeland.rcbcore.bukkit.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Arrays;
import java.util.List;

public class CommandBlocker implements Listener {

    private final List<String> blockedCmds = Arrays.asList("/version", "/restart", "/help", "/restart", "/stop", "plugins", "/bukkit:plugins", "/bukkit:pl", "/timings", "/pl", "/reload", "/about",
            "/ver", "/op", "/deop", "/enchant", "/effect", "/tp", "/give", "/me", "/tell", "/say", "/spawnpoint", "/whitelist", "/xp", "/clear", "/seed", "/save-all", "/save-on", "/save-off", "/?",
            "/debug", "/playsound", "/difficulty", "/defaultgamemode", "/setblock", "/tellraw", "/summon", "/testfor", "/testforblock", "/toggledownfall");

    @EventHandler
    public void on(PlayerCommandPreprocessEvent e){
        blockedCmds.stream().filter((cmd) -> e.getMessage().toLowerCase().contains(cmd.toLowerCase())).forEach((msg) -> {
            e.setCancelled(true);
            e.getPlayer().sendMessage("§cErreur, Impossible d'éxécuter cette commande !");
        });
    }
}
