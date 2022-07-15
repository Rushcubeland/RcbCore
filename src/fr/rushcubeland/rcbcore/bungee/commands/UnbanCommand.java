package fr.rushcubeland.rcbcore.bungee.commands;

import fr.rushcubeland.commons.permissions.PermissionsUnit;
import fr.rushcubeland.commons.utils.UUIDFetcher;
import fr.rushcubeland.rcbcore.bungee.RcbAPI;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class UnbanCommand extends Command {

    private static final String cmd = "unban";

    public UnbanCommand() {
        super("unban");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer && !sender.hasPermission(PermissionsUnit.UNBAN.getPermission()) && !sender.hasPermission(PermissionsUnit.ALL.getPermission())){
            sender.sendMessage(new TextComponent("§cVous n'avez pas la permission de faire ceci !"));
            return;
        }
        if (args.length != 1) {
            sender.sendMessage(new TextComponent("§c/unban <joueur>"));
            return;
        }
        String targetName = args[0];

        String uuids = UUIDFetcher.getUUIDFromName(targetName);
        if(uuids == null){
            sender.sendMessage(new TextComponent("§cCe joueur n'existe pas !"));
            return;
        }
        UUID targetUUID = UUID.fromString(uuids);

        if (!(RcbAPI.getInstance().getBanManager().isBanned(targetUUID))) {
            sender.sendMessage(new TextComponent("§cCe joueur n'est pas banni !"));
            return;
        }
        RcbAPI.getInstance().getBanManager().unban(targetUUID, sender.getName());
        sender.sendMessage(new TextComponent("§aVous avez débanni §6" + targetName));
    }

    public static String getCmd() {
        return cmd;
    }
}
