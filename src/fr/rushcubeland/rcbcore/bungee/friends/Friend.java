package fr.rushcubeland.rcbcore.bungee.friends;

import fr.rushcubeland.commons.AFriends;
import fr.rushcubeland.commons.AOptions;
import fr.rushcubeland.commons.Account;
import fr.rushcubeland.commons.options.OptionUnit;
import fr.rushcubeland.commons.rank.RankUnit;
import fr.rushcubeland.commons.utils.UUIDFetcher;
import fr.rushcubeland.rcbcore.bungee.BungeeSend;
import fr.rushcubeland.rcbcore.bungee.RcbAPI;
import fr.rushcubeland.rcbcore.bungee.provider.FriendsProvider;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Friend {

    private static final HashMap<ProxiedPlayer, ArrayList<ProxiedPlayer>> datarequest = new HashMap<>();

    public static void removeFriend(ProxiedPlayer player, String targetName){
        Account account = RcbAPI.getInstance().getAccount(player);
        AFriends aFriends = RcbAPI.getInstance().getAccountFriends(player);

        if(UUIDFetcher.getUUIDFromName(targetName) == null){
            player.sendMessage(new TextComponent("§cCe joueur n'existe pas."));
            return;
        }
        UUID targetUUID = UUID.fromString(UUIDFetcher.getUUIDFromName(targetName));
        if(aFriends.areFriendWith(targetUUID)){
            Account account2 = RcbAPI.getInstance().getAccount(targetUUID);
            AFriends aFriends2 = RcbAPI.getInstance().getAccountFriends(targetUUID);
            aFriends2.removeFriend(player.getUniqueId());
            aFriends.removeFriend(targetUUID);
            if(ProxyServer.getInstance().getPlayer(targetUUID) != null){
                ProxyServer.getInstance().getPlayer(targetUUID).sendMessage(new TextComponent("§d[Amis] §e" + account.getRank().getPrefix() + player.getName() + " §cvous a retiré de sa liste d'amis !"));
            }
            player.sendMessage(new TextComponent("§d[Amis] §cVous avez retiré §e" + account2.getRank().getPrefix() + account2.getRank().getPrefix() + targetName + " §cde votre liste d'amis !"));
            final FriendsProvider friendsProvider2 = new FriendsProvider(targetUUID);
            friendsProvider2.sendFriendsToRedis(aFriends2);
            final FriendsProvider friendsProvider = new FriendsProvider(player);
            friendsProvider.sendFriendsToRedis(aFriends);
        }
        else
        {
            player.sendMessage(new TextComponent("§d[Amis] §cVous n'etes pas ami avec §e" + targetName + " §c!"));
        }
    }

    public static void addFriend(ProxiedPlayer player, String targetName){
        Account account = RcbAPI.getInstance().getAccount(player);
        AFriends aFriends = RcbAPI.getInstance().getAccountFriends(player);
        if(!aFriends.areFriendWith(UUID.fromString(UUIDFetcher.getUUIDFromName(targetName)))){
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(targetName);
            if(target != null){
                Account account2 = RcbAPI.getInstance().getAccount(target);
                AFriends aFriends2 = RcbAPI.getInstance().getAccountFriends(target);
                if(aFriends.hasReachedMaxFriends() || aFriends2.hasReachedMaxFriends()){
                    aFriends.addFriend(UUID.fromString(UUIDFetcher.getUUIDFromName(targetName)));
                    aFriends2.addFriend(player.getUniqueId());
                    target.sendMessage(new TextComponent("§d[Amis] §e" + account.getRank().getPrefix() + player.getName() + " §6vous a §aajouté §6à sa liste d'amis !"));
                    player.sendMessage(new TextComponent("§d[Amis] §6Vous avez §aajouté §e" + account2.getRank().getPrefix() + target.getName() + " §6de votre liste d'amis !"));

                    final FriendsProvider friendsProvider = new FriendsProvider(player);
                    friendsProvider.sendFriendsToRedis(aFriends);
                    final FriendsProvider friendsProvider2 = new FriendsProvider(target);
                    friendsProvider2.sendFriendsToRedis(aFriends2);
                }
                else
                {
                    player.sendMessage(new TextComponent("§d[Amis] §cVous ou le joueur cible a atteint le nombre maximum d'amis !"));
                }
            }
            else
            {
                player.sendMessage(new TextComponent("§f[Amis] §cCe joueur n'est pas connecté !"));
            }
        }
        else
        {
            player.sendMessage(new TextComponent("§d[Amis] §cVous etes déjà ami avec §e" + targetName + " §c!"));
        }
    }

    public static void denyFriendRequest(ProxiedPlayer receiver, ProxiedPlayer sender){
        if(datarequest.containsKey(sender)){
            if(datarequest.get(sender).contains(receiver)){
                datarequest.get(sender).remove(receiver);
                receiver.sendMessage(new TextComponent("§d[Amis] §cVous avez décliné la requête d'ami de " + RcbAPI.getInstance().getAccount(sender).getRank().getPrefix()+sender.getName()));
            }
        }
        else
        {
            receiver.sendMessage(new TextComponent("§d[Amis] §cRequête introuvable !"));
        }
    }

    public static void acceptFriendRequest(ProxiedPlayer receiver, ProxiedPlayer sender){
        if(datarequest.containsKey(sender)){
            if(datarequest.get(sender).contains(receiver)){
                datarequest.get(sender).remove(receiver);
                addFriend(receiver, sender.getName());
                receiver.sendMessage(new TextComponent("§d[Amis] §6Vous avez §aaccepté §6la requête d'ami de " + RcbAPI.getInstance().getAccount(sender).getRank().getPrefix() + sender.getName()));
            }
        }
        else
        {
            receiver.sendMessage(new TextComponent("§d[Amis] §cRequête introuvable !"));
        }
    }

    public static void sendFriendRequest(ProxiedPlayer sender, ProxiedPlayer receiver){
        Account account = RcbAPI.getInstance().getAccount(sender);
        Account account2 = RcbAPI.getInstance().getAccount(receiver);
        AFriends aFriends = RcbAPI.getInstance().getAccountFriends(sender);
        AOptions aOptions52 = RcbAPI.getInstance().getAccountOptions(receiver);
        if(!aFriends.areFriendWith(receiver.getUniqueId())){
            if(aOptions52.getStateFriendRequests().equals(OptionUnit.OPEN)){
                if(datarequest.containsKey(sender)){
                    if(!(datarequest.get(sender).contains(receiver))){
                        sender.sendMessage(new TextComponent("§d[Amis] §6Vous avez §aenvoyé §6une requête d'ami à " + account2.getRank().getPrefix() + receiver.getName()));
                        receiver.sendMessage(new TextComponent("§e-----------------------------"));
                        receiver.sendMessage(new TextComponent("§d[Amis] §6Vous avez §arecu §6une requête d'ami de " + account.getRank().getPrefix() + sender.getName()));
                        datarequest.get(sender).add(receiver);
                        ComponentBuilder componentBuilder = new ComponentBuilder("      ");
                        TextComponent accept = new TextComponent("§a[Accepter]");
                        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/f accept " + sender.getName()));
                        accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("/f accept " + sender.getName())));
                        TextComponent deny = new TextComponent("§c[Refuser]");
                        deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/f deny " + sender.getName()));
                        deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("/f deny " + sender.getName())));
                        componentBuilder.append(accept);
                        componentBuilder.append(new TextComponent("      ")).reset();
                        componentBuilder.append(deny);
                        receiver.sendMessage(componentBuilder.create());
                        receiver.sendMessage(new TextComponent("§e-----------------------------"));
                    }
                    else
                    {
                        sender.sendMessage(new TextComponent("§d[Amis] §cVous avez déjà envoyé une requête d'ami à ce joueur !"));
                    }
                }
                else
                {
                    sender.sendMessage(new TextComponent("§d[Amis] §6Vous avez §aenvoyé §6une requête d'ami à " + account2.getRank().getPrefix() + receiver.getName()));
                    receiver.sendMessage(new TextComponent("§e-----------------------------"));
                    receiver.sendMessage(new TextComponent("§d[Amis] §6Vous avez §arecu §6une requête d'ami de " + account.getRank().getPrefix() + sender.getName()));
                    ArrayList<ProxiedPlayer> nouvelle = new ArrayList<>();
                    nouvelle.add(receiver);
                    datarequest.put(sender, nouvelle);
                    ComponentBuilder componentBuilder = new ComponentBuilder("      ");
                    TextComponent accept = new TextComponent("§a[Accepter]");
                    accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/f accept " + sender.getName()));
                    accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("/f accept " + sender.getName())));
                    TextComponent deny = new TextComponent("§c[Refuser]");
                    deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/f deny " + sender.getName()));
                    deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("/f deny " + sender.getName())));
                    componentBuilder.append(accept);
                    componentBuilder.append(new TextComponent("      ")).reset();
                    componentBuilder.append(deny);
                    receiver.sendMessage(componentBuilder.create());
                    receiver.sendMessage(new TextComponent("§e-----------------------------"));
                }
            }
            else
            {
                sender.sendMessage(new TextComponent("§d[Amis] §cCe joueur ne souhaite pas recevoir des requêtes"));
                sender.sendMessage(new TextComponent("§fd'amis !"));
            }
        }
        else
        {
            sender.sendMessage(new TextComponent("§d[Amis] §cVous etes déjà ami avec " + account2.getRank().getPrefix() + receiver.getName() + " §c!"));
        }
    }

    public static void openFriendsInventory(ProxiedPlayer player){
        BungeeSend.sendGUIFriends(player);
    }

    public static void quitNotifFriends(ProxiedPlayer player){
        AFriends aFriends = RcbAPI.getInstance().getAccountFriends(player);
        for(UUID friend : aFriends.getFriends()){
            ProxiedPlayer friendP = ProxyServer.getInstance().getPlayer(friend);
            if(friendP != null){
                AOptions aOptions52 = RcbAPI.getInstance().getAccountOptions(friendP);
                if(aOptions52.getStateFriendsStatutNotif().equals(OptionUnit.OPEN)){
                    RankUnit rank = RcbAPI.getInstance().getAccount(player).getRank();
                    friendP.sendMessage(new TextComponent("§d[Ami] §b" + rank.getPrefix() + player.getName() + " §cs'est déconnecté !"));
                }
            }
        }
    }

    public static void joinNotifFriends(ProxiedPlayer player){
        AFriends aFriends = RcbAPI.getInstance().getAccountFriends(player);
        for(UUID friend : aFriends.getFriends()){
            ProxiedPlayer friendP = ProxyServer.getInstance().getPlayer(friend);
            if(friendP != null){
                AOptions aOptions52 = RcbAPI.getInstance().getAccountOptions(friendP);
                if(aOptions52.getStateFriendsStatutNotif().equals(OptionUnit.OPEN)){
                    RankUnit rank = RcbAPI.getInstance().getAccount(player).getRank();
                    friendP.sendMessage(new TextComponent("§d[Ami] §b" + rank.getPrefix() + player.getName() + " §as'est connecté !"));
                }
            }
        }
    }

    public static HashMap<ProxiedPlayer, ArrayList<ProxiedPlayer>> getDatarequest() {
        return datarequest;
    }

    public static void onQuit(ProxiedPlayer player){
        datarequest.remove(player);
    }
}
