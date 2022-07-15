package fr.rushcubeland.rcbcore.bungee.commands;

import fr.rushcubeland.commons.permissions.PermissionsUnit;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class KickCommand extends Command {

    private static final String cmd = "kick";


    public KickCommand() {
        super("kick");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer){
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if(!player.hasPermission(PermissionsUnit.KICK.getPermission()) && !player.hasPermission(PermissionsUnit.ALL.getPermission())){
                sender.sendMessage(new TextComponent("§cVous n'avez pas la permission de faire ceci !"));
                return;
            }
            if(args.length < 1){
                player.sendMessage(new TextComponent("§c/kick <Joueur> [raison]"));
                return;

            }
            if(args.length == 1){
                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
                if(target == null){
                    player.sendMessage(new TextComponent("§cCe joueur n'est pas connecté !"));
                    return;
                }
                target.disconnect(new TextComponent("§cVous avez été kick !"));
            }
            String reason = "";
            for (int i = 1; i < args.length; i++) {
                reason = reason + args[i] + " ";
            }
            if(args.length > 1){
                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
                if(target == null){
                    player.sendMessage(new TextComponent("§cCe joueur n'est pas connecté !"));
                    return;
                }
                target.disconnect(new TextComponent("§6Vous avez été §ckick !\n  \n§eRaison: §c" + reason));
            }
        }
    }

    public static String getCmd() {
        return cmd;
    }
}
