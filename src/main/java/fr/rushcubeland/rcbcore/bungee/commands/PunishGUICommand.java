package fr.rushcubeland.rcbcore.bungee.commands;

import fr.rushcubeland.commons.permissions.PermissionsUnit;
import fr.rushcubeland.rcbcore.bungee.utils.UUIDFetcher;
import fr.rushcubeland.rcbcore.bungee.BungeeSend;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class PunishGUICommand extends Command {

    private static final String cmd = "ap";

    public PunishGUICommand() {
        super(cmd);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer){
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if(!player.hasPermission(PermissionsUnit.SANCTION_GUI.getPermission()) && !player.hasPermission(PermissionsUnit.ALL.getPermission())){
                sender.sendMessage(new TextComponent("§cVous n'avez pas la permission de faire ceci !"));
                return;
            }
            if(args.length < 1){
                player.sendMessage(new TextComponent("§c/ap <joueur>"));
                return;

            }
            if(args.length == 1){
                String targetuuid;
                String target = args[0];
                if(ProxyServer.getInstance().getPlayer(target) != null){
                    targetuuid = ProxyServer.getInstance().getPlayer(target).getUniqueId().toString();
                }
                else
                {
                    targetuuid = UUIDFetcher.getUUIDFromName(target);
                }
                if(targetuuid == null){
                    player.sendMessage(new TextComponent("§cCe joueur n'exsiste pas !"));
                    return;
                }
                BungeeSend.sendPunishmentGui(player, target);
            }
        }
    }

    public static String getCmd() {
        return cmd;
    }
}
