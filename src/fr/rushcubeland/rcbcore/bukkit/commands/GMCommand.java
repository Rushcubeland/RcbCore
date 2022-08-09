package fr.rushcubeland.rcbcore.bukkit.commands;

import fr.rushcubeland.commons.Account;
import fr.rushcubeland.commons.data.callbacks.AsyncCallBack;
import fr.rushcubeland.commons.permissions.PermissionsUnit;
import fr.rushcubeland.commons.rank.RankUnit;
import fr.rushcubeland.commons.utils.MessageUtil;
import fr.rushcubeland.rcbcore.bukkit.RcbAPI;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GMCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            RcbAPI.getInstance().getAccount(player, new AsyncCallBack() {
                @Override
                public void onQueryComplete(Object result) {
                    Account account = (Account) result;
                    RankUnit rank = account.getRank();
                    if(label.equalsIgnoreCase("gamemode") || label.equalsIgnoreCase("gm")){
                        if(!player.hasPermission(PermissionsUnit.GAMEMODE.getPermission()) && !player.hasPermission(PermissionsUnit.ALL.getPermission())){
                            player.sendMessage(MessageUtil.NO_PERM.getMessage());
                        }
                        if(args.length == 0){
                            player.sendMessage("§cVeuillez spécifier un mode !");
                        }
                        if(args.length == 1){
                            if(args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("survie")){
                                player.setGameMode(GameMode.SURVIVAL);
                                player.sendMessage("§6Vous etes désormais en mode §csurvie");
                            }
                            else if(args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("creatif")){
                                player.setGameMode(GameMode.CREATIVE);
                                player.sendMessage("§6Vous etes désormais en mode §ccréatif");
                            }
                            else if(args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("2") || args[0].equalsIgnoreCase("aventure")){
                                player.setGameMode(GameMode.ADVENTURE);
                                player.sendMessage("§6Vous etes désormais en mode §caventure");
                            }
                            else if(args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("3") || args[0].equalsIgnoreCase("spectateur")){
                                player.setGameMode(GameMode.SPECTATOR);
                                player.sendMessage("§6Vous etes désormais en mode §cspectateur");
                            }
                        }
                        if(args.length == 2){
                            Player target = Bukkit.getPlayer(args[1]);
                            if(target != null){
                                RcbAPI.getInstance().getAccount(target, new AsyncCallBack() {
                                    @Override
                                    public void onQueryComplete(Object result) {
                                        Account accountT = (Account) result;
                                        RankUnit  rankT = accountT.getRank();
                                        if(args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("survie")){
                                            target.setGameMode(GameMode.SURVIVAL);
                                            player.sendMessage(rankT.getPrefix() + target.getDisplayName() + " §6est désormais en mode §csurvie");
                                            target.sendMessage("§6Vous etes désormais en mode §csurvie §6par le biais de §f" + rank.getPrefix() + player.getDisplayName());
                                        }
                                        else if(args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("creatif")){
                                            target.setGameMode(GameMode.CREATIVE);
                                            player.sendMessage(rankT.getPrefix() + target.getDisplayName() + " §6est désormais en mode §ccréatif");
                                            target.sendMessage("§6Vous etes désormais en mode §ccréatif §6par le biais de §f" + rank.getPrefix() +player.getDisplayName());
                                        }
                                        else if(args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("2") || args[0].equalsIgnoreCase("aventure")){
                                            target.setGameMode(GameMode.ADVENTURE);
                                            player.sendMessage(rankT.getPrefix() + target.getDisplayName() + " §6est désormais en mode §caventure");
                                            target.sendMessage("§6Vous etes désormais en mode §caventure §6par le biais de §f" + rank.getPrefix() +player.getDisplayName());
                                        }
                                        else if(args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("3") || args[0].equalsIgnoreCase("spectateur")){
                                            target.setGameMode(GameMode.SPECTATOR);
                                            player.sendMessage(rankT.getPrefix() + target.getDisplayName() + " §6est désormais en mode §cspectateur");
                                            target.sendMessage("§6Vous etes désormais en mode §cspectateur §6par le biais de §f" + rank.getPrefix() +player.getDisplayName());
                                        }
                                    }
                                });
                            }
                            else
                            {
                                player.sendMessage(MessageUtil.SPECIFY_VALID_PLAYER.getMessage());
                            }
                        }
                    }
                }
            });
        }
        return false;
    }
}
