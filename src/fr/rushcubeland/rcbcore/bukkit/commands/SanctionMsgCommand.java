package fr.rushcubeland.rcbcore.bukkit.commands;

import fr.rushcubeland.commons.data.callbacks.AsyncCallBack;
import fr.rushcubeland.commons.permissions.PermissionsUnit;
import fr.rushcubeland.commons.utils.MessageUtil;
import fr.rushcubeland.rcbcore.bukkit.utils.UUIDFetcher;
import fr.rushcubeland.rcbcore.bukkit.sanction.SanctionGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SanctionMsgCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(label.equalsIgnoreCase("apmsgb") && sender instanceof Player){
            Player player = (Player) sender;
            if(!player.hasPermission(PermissionsUnit.ALL.getPermission()) || !player.hasPermission(PermissionsUnit.SANCTION_GUI.getPermission()) || !player.hasPermission(PermissionsUnit.SANCTION_GUI_MSG.getPermission())){
                player.sendMessage(MessageUtil.NO_PERM.getMessage());
                return true;
            }
            if(args.length == 0){
                player.sendMessage("§c/apmsgb <joueur>");
                return true;
            }
            if(args.length == 1){
                String target = args[0];
                UUIDFetcher.getUUIDFromName(target, new AsyncCallBack() {
                    @Override
                    public void onQueryComplete(Object result) {
                        String s = (String) result;
                        if(s == null){
                            player.sendMessage(MessageUtil.UNKNOWN_PLAYER.getMessage());
                            return;
                        }
                        SanctionGUI.getModAndTarget().put(player, target);
                        SanctionGUI.openMsgGui(player, target);
                    }
                });
            }
        }
        return false;
    }

}
