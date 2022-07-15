package fr.rushcubeland.rcbcore.bungee.commands;

import fr.rushcubeland.commons.Account;
import fr.rushcubeland.commons.permissions.PermissionsUnit;
import fr.rushcubeland.rcbcore.bungee.RcbAPI;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;
import java.util.List;

public class StaffChatCommand extends Command {

    public static final List<String> ALIASES = Arrays.asList("staffchat", "sc");

    public StaffChatCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer){
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if(player.hasPermission(PermissionsUnit.STAFF_CHAT.getPermission()) || player.hasPermission(PermissionsUnit.ALL.getPermission())){
                if(args.length > 0){
                    StringBuilder message = new StringBuilder();
                    for (String arg : args) {
                        message.append(arg).append(" ");
                    }
                    Account account = RcbAPI.getInstance().getAccount(player);
                    for(ProxiedPlayer plsstaff : ProxyServer.getInstance().getPlayers()){
                        if(plsstaff.hasPermission(PermissionsUnit.STAFF_CHAT.getPermission()) || plsstaff.hasPermission(PermissionsUnit.ALL.getPermission())){
                            plsstaff.sendMessage(new TextComponent("§6[StaffChat] " + account.getRank().getPrefix() + player.getName() + " §f: " + message));
                        }
                    }
                }
                else
                {
                    player.sendMessage(new TextComponent("§c/staffchat <message>"));
                }
            }
            else
            {
                player.sendMessage(new TextComponent("§cVous n'avez pas la permission de faire ceci !"));
            }
        }
    }

}
