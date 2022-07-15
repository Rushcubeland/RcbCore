package fr.rushcubeland.rcbcore.bukkit.commands;

import fr.rushcubeland.rcbcore.bukkit.BukkitSend;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class ReportMsgConfirmCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd, @Nonnull String label, @Nonnull String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(label.equalsIgnoreCase("reportconfirm") && args.length >= 2){
                String targetName = args[0];
                StringBuilder builder = new StringBuilder();
                for(int i=1; i < args.length; i++){
                    builder.append(args[i]).append(" ");
                }
                String message = builder.toString();
                message = message.substring(0, message.length() - 1);
                if(ReportMsgCommand.dataPlayers.containsKey(player) && ReportMsgCommand.dataPlayers.get(player).containsKey(targetName)){
                    ReportMsgCommand.dataPlayers.get(player).get(targetName).remove(message);
                    if(ReportMsgCommand.msgs.containsKey(targetName)){
                        ArrayList<String> msgsCache = ReportMsgCommand.msgs.get(targetName);
                        for(String messages : msgsCache){
                            if(messages.equals(message)){
                                BukkitSend.reportMsgToProxy(player, targetName, message);
                                msgsCache.remove(messages);
                                ReportMsgCommand.msgs.replace(targetName, msgsCache);
                                player.sendMessage("§6[AP] §aVotre signalement sera prochainnement examiné par un Modérateur. Merci.");
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
