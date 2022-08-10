package fr.rushcubeland.rcbcore.bungee.commands;

import fr.rushcubeland.commons.AFriends;
import fr.rushcubeland.rcbcore.bungee.utils.UUIDFetcher;
import fr.rushcubeland.rcbcore.bungee.RcbAPI;
import fr.rushcubeland.rcbcore.bungee.friends.Friend;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class FriendCommand extends Command {

    public static final List<String> ALIASES = Arrays.asList("friend", "f", "friends", "ami", "amis");

    public FriendCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer){
            ProxiedPlayer player = (ProxiedPlayer) sender;
            AFriends aFriends = RcbAPI.getInstance().getAccountFriends(player);
            if(args.length == 0){
                infos(player);
                return;
            }
            if(args.length == 1){
                oneArg(player, args[0]);
            }
            else {
                twoArgs(player, args, aFriends);
            }
        }
    }

    private void add(ProxiedPlayer player, String argPlayer) {
        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(argPlayer);
        if(target != player){
            if(target != null){
                Friend.sendFriendRequest(player, target);
            }
            else
            {
                player.sendMessage(new TextComponent("§d[Amis] §cCe joueur n'est pas connecté !"));
            }
        }
        else
        {
            player.sendMessage(new TextComponent("§d[Amis] §cImpossible d'envoyer une requête d'amis à vous meme !"));
        }
    }

    private void remove(ProxiedPlayer player, String argPlayer, AFriends aFriends) {
        if(!argPlayer.equals(player.getName())){
            String uuid = UUIDFetcher.getUUIDFromName(argPlayer);
            if(uuid == null) {
                return;
            }
            if(aFriends.areFriendWith(UUID.fromString(uuid))){
                Friend.removeFriend(player, argPlayer);
            }
            else
            {
                player.sendMessage(new TextComponent("§d[Amis] §cVous n'etes pas ami avec §e" + argPlayer));
            }
        }
        else
        {
            player.sendMessage(new TextComponent("§d[Amis] §cImpossible de vous supprimer de votre liste d'amis :)"));
        }
    }

    private void accept(ProxiedPlayer player, String argPlayer) {
        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(argPlayer);
        if(target != null){
            Friend.acceptFriendRequest(player, target);
        }
        else
        {
            player.sendMessage(new TextComponent("§d[Amis] §cCe joueur n'est pas connecté !"));
        }
    }

    private void deny(ProxiedPlayer player, String argPlayer) {
        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(argPlayer);
        if(target != null){
            Friend.denyFriendRequest(player, target);
        }
        else
        {
            player.sendMessage(new TextComponent("§d[Amis] §cCe joueur n'est pas connecté !"));
        }
    }

    private void twoArgs(ProxiedPlayer player, String[] args, AFriends aFriends){
        if(args[0].equalsIgnoreCase("add")){
            add(player, args[1]);
        }
        if(args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("rm")){
            remove(player, args[1], aFriends);
        }
        if(args[0].equalsIgnoreCase("accept")){
            accept(player, args[1]);
        }
        if(args[0].equalsIgnoreCase("deny")){
            deny(player, args[1]);
        }
    }

    private void oneArg(ProxiedPlayer player, String arg){
        if(arg.equalsIgnoreCase("list") || arg.equalsIgnoreCase("gui")){
            Friend.openFriendsInventory(player);
        }
        else
        {
            infos(player);
            player.sendMessage(new TextComponent("§d[Amis] §cVeuillez spécifier un argument valide !"));
        }
    }

    private void infos(ProxiedPlayer player){
        player.sendMessage(new TextComponent("§e---------§d[Amis]§e---------"));
        player.sendMessage(new TextComponent("§6/friends list - §eAfficher la liste de vos amis"));
        player.sendMessage(new TextComponent("§6/friends add <joueur> - §eEnvoyer une requête d'ami"));
        player.sendMessage(new TextComponent("§6/friends remove <joueur> - §eRetirer un joueur de votre liste"));
        player.sendMessage(new TextComponent("§ed'amis"));
        player.sendMessage(new TextComponent("§6/friends accept <joueur> - §eAccepter la requête d'ami d'un"));
        player.sendMessage(new TextComponent("§ejoueur"));
        player.sendMessage(new TextComponent("§6/friends deny <joueur> - §eRefuser la requête d'ami d'un"));
        player.sendMessage(new TextComponent("§ejoueur"));
        player.sendMessage(new TextComponent("§e----------------------"));
    }

}
