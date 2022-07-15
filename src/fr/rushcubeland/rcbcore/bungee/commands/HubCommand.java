package fr.rushcubeland.rcbcore.bungee.commands;

import fr.rushcubeland.rcbcore.bungee.network.Network;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;
import java.util.List;

public class HubCommand extends Command {

    public static final List<String> ALIASES = Arrays.asList("hub", "lobby");

    public HubCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer){
            ProxiedPlayer player = (ProxiedPlayer) sender;
            Network.joinLobby(player);
        }
    }

}
