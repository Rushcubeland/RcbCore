package fr.rushcubeland.rcbcore.bungee.listeners;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Arrays;
import java.util.List;

public class OnChat implements Listener {

    private final List<String> blockedCmds = Arrays.asList("/send", "/glist", "/ip", "/server", "/perms", "/end", "/greload", "/find", "/bungee", "/alert", "/alertraw");

    @EventHandler
    public void onChat(ChatEvent e) {
        ProxiedPlayer player = (ProxiedPlayer) e.getSender();
        String msg = e.getMessage();
        if (msg.length() > 1) {
            String[] arg = msg.split(" ");
            msg = arg[0];
        }

        String finalMsg = msg;
        blockedCmds.stream().filter((cmd) -> finalMsg.toLowerCase().contains(cmd.toLowerCase())).forEach((msg1) -> {
            e.setCancelled(true);
            player.sendMessage(new TextComponent("§cErreur, Impossible d'éxécuter cette commande !"));
        });

    }

}
