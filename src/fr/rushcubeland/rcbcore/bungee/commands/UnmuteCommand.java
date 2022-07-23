package fr.rushcubeland.rcbcore.bungee.commands;

import fr.rushcubeland.commons.permissions.PermissionsUnit;
import fr.rushcubeland.commons.utils.MessageUtil;
import fr.rushcubeland.rcbcore.bungee.utils.UUIDFetcher;
import fr.rushcubeland.rcbcore.bungee.RcbAPI;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class UnmuteCommand extends Command {

    private static String cmd = "unmute";

    public UnmuteCommand() {
        super("unmute");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer && !sender.hasPermission(PermissionsUnit.UNMUTE.getPermission()) && !sender.hasPermission(PermissionsUnit.ALL.getPermission())){
            sender.sendMessage(new TextComponent(MessageUtil.NO_PERM.getMessage()));
            return;
        }
        if (args.length != 1) {
            sender.sendMessage(new TextComponent("§c/unmute <joueur>"));
            return;
        }
        String targetName = args[0];

        String uuids = UUIDFetcher.getUUIDFromName(targetName);
        if(uuids == null){
            sender.sendMessage(new TextComponent(MessageUtil.UNKNOWN_PLAYER.getMessage()));
            return;
        }
        UUID targetUUID = UUID.fromString(uuids);

        if (!(RcbAPI.getInstance().getMuteManager().isMuted(targetUUID))) {
            sender.sendMessage(new TextComponent("§cCe joueur n'est pas mute !"));
            return;
        }
        RcbAPI.getInstance().getMuteManager().unmute(targetUUID, sender.getName());
        sender.sendMessage(new TextComponent("§aVous avez démute §6" + targetName));
    }

    public static String getCmd() {
        return cmd;
    }
}

