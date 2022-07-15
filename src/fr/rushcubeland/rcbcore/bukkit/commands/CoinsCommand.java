package fr.rushcubeland.rcbcore.bukkit.commands;

import fr.rushcubeland.commons.Account;
import fr.rushcubeland.commons.data.callbacks.AsyncCallBack;
import fr.rushcubeland.rcbcore.bukkit.RcbAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoinsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(label.equalsIgnoreCase("coins") && sender instanceof Player){
            Player player = (Player) sender;
            RcbAPI.getInstance().getAccount(player, new AsyncCallBack() {
                @Override
                public void onQueryComplete(Object result) {
                    Account account = (Account) result;
                    player.sendMessage("§6Votre solde de §eCoins §6est de: §c" + account.getCoins() + " §e⛁");
                }

                @Override
                public void onQueryError(Exception e) {
                    e.printStackTrace();
                    player.kickPlayer("§cVotre compte n'a pas été trouvé, veuillez contacter un administrateur.");
                }
            });
        }
        return false;
    }
}
