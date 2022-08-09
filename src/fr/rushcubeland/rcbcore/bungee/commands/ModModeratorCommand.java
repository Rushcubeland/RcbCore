package fr.rushcubeland.rcbcore.bungee.commands;

import fr.rushcubeland.commons.permissions.PermissionsUnit;
import fr.rushcubeland.commons.utils.MessageUtil;
import fr.rushcubeland.rcbcore.bungee.BungeeSend;
import fr.rushcubeland.rcbcore.bungee.mod.ModModerator;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;


public class ModModeratorCommand extends Command {

    public ModModeratorCommand() {
        super("mod");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer){
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if(!player.hasPermission(PermissionsUnit.MODERATOR_MOD.getPermission()) && !player.hasPermission(PermissionsUnit.ALL.getPermission())){
                player.sendMessage(new TextComponent(MessageUtil.NO_PERM.getMessage()));
                return;
            }
            if(ModModerator.isInModData(player.getUniqueId().toString())){
                ModModerator.removeMod(player.getUniqueId().toString());
                BungeeSend.sendModModeratorDataRemove(player.getUniqueId().toString());
                player.sendMessage(new TextComponent("§6Vous avez §cquitté §6le mode §cModérateur"));
                return;
            }
            ModModerator.addMod(player.getUniqueId().toString());
            BungeeSend.sendModModeratorDataAdd(player.getUniqueId().toString());
            player.sendMessage(new TextComponent("§aVous êtes désormais en mode §6Modérateur"));
        }
    }

}
