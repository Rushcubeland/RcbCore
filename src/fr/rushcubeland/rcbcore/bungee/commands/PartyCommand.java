package fr.rushcubeland.rcbcore.bungee.commands;

import fr.rushcubeland.commons.AParty;
import fr.rushcubeland.commons.rank.RankUnit;
import fr.rushcubeland.rcbcore.bungee.RcbAPI;
import fr.rushcubeland.rcbcore.bungee.parties.Parties;
import fr.rushcubeland.rcbcore.bungee.parties.Party;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class PartyCommand extends Command {

    public static final List<String> ALIASES = Arrays.asList("group", "party", "p", "g", "groupe");

    public PartyCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer){
            ProxiedPlayer player = (ProxiedPlayer) sender;
            Optional<AParty> aParty = RcbAPI.getInstance().getAccountParty(player);
            RankUnit rank = RcbAPI.getInstance().getAccount(player).getRank();

            if(args.length == 0){
                infos(player);
                return;
            }
            if(args.length == 1){
                if(args[0].equalsIgnoreCase("list")){
                    if(aParty.isPresent() && aParty.get().isInParty()){
                        Party party = aParty.get().getParty();
                        player.sendMessage(new TextComponent("§e---------§d[Groupe]§e---------"));
                        for(ProxiedPlayer pls : party.getPlayers()){
                            RankUnit rank2 = RcbAPI.getInstance().getAccount(pls).getRank();
                            if(party.getCaptain() == pls){
                                player.sendMessage(new TextComponent("§b" + rank2.getPrefix() + pls.getDisplayName() + " §7<> " + pls.getServer().getInfo().getName() + " §7[Capitaine]"));
                            }
                            else
                            {
                                player.sendMessage(new TextComponent("§b" + rank2.getPrefix() + pls.getDisplayName() + " §7<> " + pls.getServer().getInfo().getName()));
                            }
                        }
                        player.sendMessage(new TextComponent("§e-------------------------"));
                    }
                    else
                    {
                        player.sendMessage(new TextComponent("§d[Groupe] §cVous n'êtes pas dans un groupe !"));
                        return;
                    }
                }
                if(args[0].equalsIgnoreCase("leave") || args[0].equalsIgnoreCase("quit")){
                    if(aParty.isPresent() && aParty.get().isInParty()){
                        Parties.leave(player);
                    }
                    else
                    {
                        player.sendMessage(new TextComponent("§cVotre compte est introuvable, veuillez vous reconnecter."));
                        player.sendMessage(new TextComponent("§cSi le problème persite, veuillez contacter un administrateur."));
                        return;
                    }
                }
                if(args[0].equalsIgnoreCase("disband")){
                    if(aParty.isPresent()){
                        Party party = aParty.get().getParty();
                        if(party.getCaptain() == null){
                            return;
                        }
                        if(party.getCaptain() == player){
                            for(ProxiedPlayer pls : party.getPlayers()){
                                Optional<AParty> a = RcbAPI.getInstance().getAccountParty(pls);
                                pls.sendMessage(new TextComponent("§d[Groupe] §cVotre groupe a été dissout par " + rank.getPrefix() + player.getDisplayName() + " §7[Capitaine]"));
                                a.ifPresent(value -> value.setParty(null));
                                party.disbandParty();
                            }
                            player.sendMessage(new TextComponent("§d[Groupe] §cVous avez dissous le groupe !"));
                        }
                        else
                        {
                            player.sendMessage(new TextComponent("§d[Groupe] §cVous ne pouvez pas dissoudre le groupe en"));
                            player.sendMessage(new TextComponent("§cn'etant pas capitaine !"));
                        }
                    }
                }
                if(args[0].equalsIgnoreCase("lead") || args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("rm") || args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("accept") || args[0].equalsIgnoreCase("deny")){
                    player.sendMessage(new TextComponent("§d[Groupe] §cVeuillez spécifier un joueur !"));
                    return;
                }
            }
            if(args.length == 2){
                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[1]);
                Optional<AParty> aParty2 = RcbAPI.getInstance().getAccountParty(target);
                if(target != null){
                    if(args[0].equalsIgnoreCase("add")){
                        if(target.equals(player)){
                            player.sendMessage(new TextComponent("§d[Groupe] §cVous ne pouvez pas vous inviter vous-memes !"));
                            return;
                        }
                        if(aParty.isPresent()){
                            if(aParty.get().isInParty()){
                                Parties.sendRequest(player, target);
                            }
                            else
                            {
                                Party party = new Party(5);
                                party.addPlayer(player);
                                Parties.sendRequest(player, target);
                                return;
                            }
                        }
                    }
                    if(args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("rm") || args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("dl")){
                        if(aParty.isPresent()){
                            if(aParty.get().isInParty()){
                                if(aParty.get().getParty().getCaptain() == player){
                                    Parties.removeMember(player, target);
                                }
                                else
                                {
                                    player.sendMessage(new TextComponent("§d[Groupe] §cSeul le capitaine du groupe peut exclure des membres !"));
                                    return;
                                }
                            }
                            else
                            {
                                player.sendMessage(new TextComponent("§d[Groupe] §cVous n'êtes pas dans un groupe !"));
                                return;
                            }
                        }
                    }
                    if(args[0].equalsIgnoreCase("accept")){
                        if(aParty2.isPresent()){
                            if(aParty2.get().isInParty()){
                                Parties.acceptRequest(target, player, aParty2.get().getParty());
                            }
                            else
                            {
                                player.sendMessage(new TextComponent("§d[Groupe] §cCe joueur n'est plus dans une partie !"));
                            }
                        }
                    }
                    if(args[0].equalsIgnoreCase("deny")){
                        if(aParty2.isPresent()){
                            if(aParty2.get().isInParty()){
                                Parties.denyRequest(target, player, aParty2.get().getParty());
                            }
                            else
                            {
                                player.sendMessage(new TextComponent("§d[Groupe] §cCe joueur n'est plus dans une partie !"));
                            }
                        }
                    }
                    if(args[0].equalsIgnoreCase("lead")){
                        if(aParty.isPresent() && aParty2.isPresent()){
                            if(aParty.get().isInParty()){
                                Party party = aParty.get().getParty();
                                if(party.equals(aParty2.get().getParty())){
                                    if(party.getCaptain().equals(player)){
                                        party.setCaptain(target);
                                        RankUnit rank2 = RcbAPI.getInstance().getAccount(target).getRank();
                                        for(ProxiedPlayer pls : party.getPlayers()){
                                            pls.sendMessage(new TextComponent("§d[Groupe] §b" + rank.getPrefix() + player.getDisplayName() + " §aa transmis le lead à §e" + rank2.getPrefix() + target.getDisplayName()));
                                        }
                                    }
                                    else
                                    {
                                        player.sendMessage(new TextComponent("§d[Groupe] §cSeul le capitaine du groupe peut transmettre le lead §cà un membre !"));
                                    }
                                }
                                else
                                {
                                    player.sendMessage(new TextComponent("§d[Groupe] §cVous n'etes pas dans le meme groupe !"));
                                }
                            }
                            else
                            {
                                player.sendMessage(new TextComponent("§d[Groupe] §cVous n'etes pas dans un groupe !"));
                            }
                        }
                    }
                }
            }
        }
    }

    private void infos(ProxiedPlayer player){
        player.sendMessage(new TextComponent("§e---------§d[Groupe]§e---------"));
        player.sendMessage(new TextComponent("§6/group list - §eAfficher la liste des joueurs de votre groupe"));
        player.sendMessage(new TextComponent("§6/group add <joueur> - §eEnvoyer une invitation de groupe"));
        player.sendMessage(new TextComponent("§6/group remove <joueur> - §eExclure un joueur de votre groupe"));
        player.sendMessage(new TextComponent("§6/group leave - §eQuitter votre groupe"));
        player.sendMessage(new TextComponent("§6/group lead <joueur> - §eDéfinir le role de capitaine"));
        player.sendMessage(new TextComponent("§e à un joueur de votre groupe"));
        player.sendMessage(new TextComponent("§6/group accept <joueur> - §eAccepter l'invitation d'un joueur"));
        player.sendMessage(new TextComponent("§epour rejoindre son groupe"));
        player.sendMessage(new TextComponent("§6/group deny <joueur> - §eRefuser l'invitation d'un"));
        player.sendMessage(new TextComponent("§ejoueur"));
        player.sendMessage(new TextComponent("§6/group disband - §eDissoudre votre groupe"));
        player.sendMessage(new TextComponent("§e-----------------------"));
    }

}
