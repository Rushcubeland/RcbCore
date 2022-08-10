package fr.rushcubeland.rcbcore.bungee.commands;

import fr.rushcubeland.commons.AFriends;
import fr.rushcubeland.commons.AOptions;
import fr.rushcubeland.commons.Account;
import fr.rushcubeland.commons.options.OptionUnit;
import fr.rushcubeland.commons.utils.MessageUtil;
import fr.rushcubeland.rcbcore.bungee.utils.UUIDFetcher;
import fr.rushcubeland.rcbcore.bungee.RcbAPI;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class MPCommand extends Command {

    private static final String cmd = "msg";


    public MPCommand() {
        super(cmd);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer){
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if(RcbAPI.getInstance().getMuteManager().isMuted(player.getUniqueId())){
                player.sendMessage(new TextComponent("§cVous avez été mute !"));
                return;
            }
            if(args.length == 0){
                player.sendMessage(new TextComponent("§cVeuillez spécifier un joueur et votre message !"));
                player.sendMessage(new TextComponent("§c/msg <joueur> <message>"));
            }
            if(args.length == 1){
                player.sendMessage(new TextComponent("§cVeuillez spécifier votre message !"));
                player.sendMessage(new TextComponent("§c/msg <joueur> <message>"));
            }
            if(args.length >= 2){
                if(!args[0].equals(player.getDisplayName())){
                    ProxiedPlayer receiver = ProxyServer.getInstance().getPlayer(args[0]);
                    if(receiver != null){
                        String message = "";
                        for (int i = 1; i < args.length; i++) {
                            message = message + args[i] + " ";
                        }
                        Account account = RcbAPI.getInstance().getAccount(player);
                        Account account2 = RcbAPI.getInstance().getAccount(receiver);
                        AFriends aFriends = RcbAPI.getInstance().getAccountFriends(player);
                        AOptions aOptions51 = RcbAPI.getInstance().getAccountOptions(receiver);
                        if(aOptions51.getStateMP().equals(OptionUnit.NEVER)){
                            player.sendMessage(new TextComponent("§cCe joueur ne souhaite pas recevoir de messages privés !"));
                            return;
                        }
                        else if(aOptions51.getStateMP().equals(OptionUnit.ONLY_FRIENDS) && !aFriends.areFriendWith(UUID.fromString(UUIDFetcher.getUUIDFromName(receiver.getName())))){
                            player.sendMessage(new TextComponent("§cCe joueur ne souhaite pas recevoir de messages privés !"));
                            return;
                        }
                        receiver.sendMessage(new TextComponent(account.getRank().getPrefix() + player.getDisplayName() + " §6-> §7Moi: §f" + message));
                        player.sendMessage(new TextComponent("§7Moi §6-> " + account2.getRank().getPrefix() + receiver.getDisplayName() + " §7: §f" + message));
                        RcbAPI.getInstance().getMpData().put(player, receiver);
                        RcbAPI.getInstance().getMpData().put(receiver, player);
                    }
                    else
                    {
                        player.sendMessage(new TextComponent(MessageUtil.PLAYER_NOT_ONLINE.getMessage()));
                    }
                }
                else
                {
                    player.sendMessage(new TextComponent("§cVous ne pouvez pas envoyer un message à vous-meme !"));
                }
            }
        }
    }

    public static String getCmd() {
        return cmd;
    }
}
