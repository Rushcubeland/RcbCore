package fr.rushcubeland.rcbcore.bungee.parties;

import fr.rushcubeland.commons.AParty;
import fr.rushcubeland.commons.Account;
import fr.rushcubeland.commons.data.exceptions.AccountNotFoundException;
import fr.rushcubeland.commons.options.OptionUnit;
import fr.rushcubeland.rcbcore.bungee.RcbAPI;
import fr.rushcubeland.rcbcore.bungee.provider.AccountProvider;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;
import java.util.Optional;

public class Parties {

    private static final HashMap<ProxiedPlayer, HashMap<ProxiedPlayer, Party>> datarequest = new HashMap<>();

    public static void leave(ProxiedPlayer player){
        Optional<AParty> optional = RcbAPI.getInstance().getAccountParty(player);
        if(optional.isPresent()){
            AParty aParty = optional.get();
            if(aParty.isInParty()){
                Party party = aParty.getParty();
                aParty.getParty().removePlayer(player);
                player.sendMessage(new TextComponent("§d[Groupe] §cVous avez quitté votre groupe !"));
                for(ProxiedPlayer pls : party.getPlayers()){
                    pls.sendMessage(new TextComponent("§d[Groupe] §b" + player.getDisplayName() + " §ca quitté le groupe !"));
                }
            }
        }
    }

    public static void removeMember(ProxiedPlayer player, ProxiedPlayer target){
        Optional<AParty> optional = RcbAPI.getInstance().getAccountParty(player);
        if(optional.isPresent()){
            AParty aParty = optional.get();
            if(aParty.getParty().getPlayers().contains(target)){

                try {

                    final AccountProvider accountProvider = new AccountProvider(target);
                    final Account account = accountProvider.getAccount();
                    aParty.getParty().removePlayer(target);
                    target.sendMessage(new TextComponent("§d[Groupe] §e" + account.getRank().getPrefix() + player.getDisplayName() + " §cvous a exclu de son groupe !"));
                    player.sendMessage(new TextComponent("§d[Groupe] §cVous avez retiré §e" + account.getRank().getPrefix() + target.getDisplayName() + " §cde votre groupe !"));

                } catch (AccountNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                player.sendMessage(new TextComponent("§d[Groupe] §ce" + target.getDisplayName() + " §c n'est pas dans votre groupe !"));
            }
        }

    }

    public static void addMember(ProxiedPlayer player, ProxiedPlayer target){

        try {

            final AccountProvider accountProvider = new AccountProvider(player);
            final AccountProvider accountProvider2 = new AccountProvider(target);
            final Account account = accountProvider.getAccount();
            final Account account2 = accountProvider2.getAccount();

            Optional<AParty> optional = RcbAPI.getInstance().getAccountParty(player);
            if(optional.isPresent()){
                AParty aParty = optional.get();

                if(!aParty.isInParty()){
                    Party party = new Party(5);
                    party.addPlayer(player);
                    party.setCaptain(player);
                    aParty.getParty().addPlayer(target);
                    target.sendMessage(new TextComponent("§d[Groupe] §e" + account.getRank().getPrefix() + player.getDisplayName() + " §6vous a §aajouté §6à son groupe !"));
                    player.sendMessage(new TextComponent("§d[Groupe] §6Vous avez §aajouté §e" + account2.getRank().getPrefix() + target.getDisplayName() + " §6à votre groupe"));
                    return;

                }
                if(!aParty.getParty().getPlayers().contains(target)){
                    if(aParty.getParty().getPlayers().size() < aParty.getParty().getMaxPlayers()){
                        aParty.getParty().addPlayer(target);
                        target.sendMessage(new TextComponent("§d[Groupe] §e" + account.getRank().getPrefix() + player.getDisplayName() + " §6vous a §aajouté §6à son groupe !"));
                        player.sendMessage(new TextComponent("§d[Groupe] §6Vous avez §aajouté §e" + account2.getRank().getPrefix() + target.getDisplayName() + " §6à votre groupe"));
                    }
                    else
                    {
                        player.sendMessage(new TextComponent("§d[Groupe] §cLa partie a atteint sa limite de joueurs !"));
                    }
                }
                else
                {
                    player.sendMessage(new TextComponent("§d[Groupe] §e" + account2.getRank().getPrefix() + target.getDisplayName() + " §cest déjà dans votre groupe !"));
                }
            }

        } catch (AccountNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    private static void requestFormat(ProxiedPlayer sender, ProxiedPlayer target){
        sender.sendMessage(new TextComponent("§d[Groupe] §6Vous avez §aenvoyé §6une invitation de groupe à " + RcbAPI.getInstance().getAccount(target).getRank().getPrefix() + target.getName()));
        target.sendMessage(new TextComponent("§e-----------------------------"));
        target.sendMessage(new TextComponent("§d[Groupe] §6Vous avez §arecu §6une invitation de groupe de " + RcbAPI.getInstance().getAccount(sender).getRank().getPrefix() + sender.getName()));
        ComponentBuilder componentBuilder = new ComponentBuilder("      ");
        TextComponent accept = new TextComponent("§a[Accepter]");
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/group accept " + sender.getDisplayName()));
        accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,  new Text("/group accept " + sender.getDisplayName())));
        TextComponent deny = new TextComponent("§c[Refuser]");
        deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/group deny " + sender.getDisplayName()));
        deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,  new Text("/group deny " + sender.getDisplayName())));
        componentBuilder.append(accept);
        componentBuilder.append(new TextComponent("      ")).reset();
        componentBuilder.append(deny);
        target.sendMessage(componentBuilder.create());
        target.sendMessage(new TextComponent("§e-----------------------------"));
    }

    public static void sendRequest(ProxiedPlayer sender, ProxiedPlayer target){
        if(RcbAPI.getInstance().getAccountParty(sender).isEmpty() || RcbAPI.getInstance().getAccountParty(target).isEmpty()){
            return;
        }
        if(RcbAPI.getInstance().getAccountParty(sender).get().isInParty() && RcbAPI.getInstance().getAccountParty(target).get().isInParty()){
            if(!RcbAPI.getInstance().getAccountParty(sender).get().getParty().equals(RcbAPI.getInstance().getAccountParty(target).get().getParty())){
                if(RcbAPI.getInstance().getAccountOptions(target).getStatePartyInvite().equals(OptionUnit.OPEN)){
                    requestFormat(sender, target);
                }
                else if(RcbAPI.getInstance().getAccountOptions(target).getStatePartyInvite().equals(OptionUnit.ONLY_FRIENDS) && RcbAPI.getInstance().getAccountFriends(sender).areFriendWith(target.getUniqueId())){
                    requestFormat(sender, target);
                }
                else
                {
                    sender.sendMessage(new TextComponent("§d[Groupe] §cCe joueur ne souhaite pas recevoir d'invitations de"));
                    sender.sendMessage(new TextComponent("§cgroupe !"));
                    return;
                }
                HashMap<ProxiedPlayer, Party> h = new HashMap<>();
                h.put(target, RcbAPI.getInstance().getAccountParty(sender).get().getParty());
                datarequest.put(sender, h);
            }
            else
            {
                sender.sendMessage(new TextComponent("§d[Groupe] §cVous etes déjà dans le meme groupe !"));
            }
        }
        else if(RcbAPI.getInstance().getAccountOptions(target).getStatePartyInvite().equals(OptionUnit.ONLY_FRIENDS)){
            if(RcbAPI.getInstance().getAccountFriends(sender).areFriendWith(target.getUniqueId())){
                if(!RcbAPI.getInstance().getAccountParty(sender).get().isInParty()){
                    Party party = new Party(5);
                    party.addPlayer(sender);
                    party.setCaptain(sender);
                }
                requestFormat(sender, target);
                HashMap<ProxiedPlayer, Party> h = new HashMap<>();
                h.put(target, RcbAPI.getInstance().getAccountParty(sender).get().getParty());
                datarequest.put(sender, h);
            }
            else
            {
                sender.sendMessage(new TextComponent("§d[Groupe] §cCe joueur ne souhaite pas recevoir d'invitations de"));
                sender.sendMessage(new TextComponent("§cgroupe !"));
            }
        }
        else if(RcbAPI.getInstance().getAccountOptions(target).getStatePartyInvite().equals(OptionUnit.NEVER)){
            sender.sendMessage(new TextComponent("§d[Groupe] §cCe joueur ne souhaite pas recevoir d'invitations de"));
            sender.sendMessage(new TextComponent("§cgroupe !"));
        }
        else if(RcbAPI.getInstance().getAccountOptions(target).getStatePartyInvite().equals(OptionUnit.OPEN)){
            Party party = new Party(5);
            party.addPlayer(sender);
            party.setCaptain(sender);
            requestFormat(sender, target);
            HashMap<ProxiedPlayer, Party> h = new HashMap<>();
            h.put(target, RcbAPI.getInstance().getAccountParty(sender).get().getParty());
            datarequest.put(sender, h);
        }
        else
        {
            sender.sendMessage(new TextComponent("§d[Groupe] §cCe joueur ne souhaite pas recevoir d'invitations de"));
            sender.sendMessage(new TextComponent("§cgroupe !"));
        }
    }

    public static void denyRequest(ProxiedPlayer sender, ProxiedPlayer target, Party party){
        if(datarequest.containsKey(sender)){
            HashMap<ProxiedPlayer, Party> value = datarequest.get(sender);
            if(value.containsKey(target) && value.get(target).equals(party)){
                datarequest.get(sender).remove(target);
                target.sendMessage(new TextComponent("§d[Groupe] §cVous avez décliné l'invitation de groupe de " + RcbAPI.getInstance().getAccount(sender).getRank().getPrefix() + sender.getName()));
            }
        }
        else
        {
            target.sendMessage(new TextComponent("§d[Groupe] §cRequête introuvable !"));
        }
    }

    public static void acceptRequest(ProxiedPlayer sender, ProxiedPlayer target, Party party){
        if(datarequest.containsKey(sender)){
            HashMap<ProxiedPlayer, Party> value = datarequest.get(sender);
            if(value.containsKey(target) && value.get(target).equals(party)){
                datarequest.get(sender).remove(target);
                addMember(sender, target);
                target.sendMessage(new TextComponent("§d[Groupe] §6Vous avez §aaccepté §6l'invitation de groupe de " + RcbAPI.getInstance().getAccount(sender).getRank().getPrefix() + sender.getDisplayName()));
            }
        }
        else
        {
            target.sendMessage(new TextComponent("§d[Groupe] §cRequête introuvable !"));
        }
    }

    public static HashMap<ProxiedPlayer, HashMap<ProxiedPlayer, Party>> getDatarequest() {
        return datarequest;
    }
}
