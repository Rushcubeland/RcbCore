package fr.rushcubeland.rcbcore.bungee.queue;

import fr.rushcubeland.commons.AParty;
import fr.rushcubeland.commons.Account;
import fr.rushcubeland.commons.rank.RankUnit;
import fr.rushcubeland.rcbcore.bungee.RcbAPI;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Optional;

public class Queue {

    public static void checkForJoinQueue(ProxiedPlayer player, QueueUnit queueUnit){
        if((queueUnit.getPlayers().size() >= queueUnit.getMaxPlayer()) && !queueUnit.getMaxPlayer().equals(-1)){
            return;
        }
        if(proxiedPlayerIsInTheQueue(player, queueUnit)){
            player.sendMessage(new TextComponent("§cVous etes déjà dans cette file d'attente !"));
        }
        else if(proxiedPlayerIsInAQueue(player)){
            leaveQueue(player, getCurrentQueue(player));
            joinQueue(player, queueUnit);
        }
        else
        {
            joinQueue(player, queueUnit);
        }

    }

    public static void joinQueue(ProxiedPlayer player, QueueUnit queueUnit){
        queueUnit.getPlayers().add(player);

        if(hasPriorityInQueues(player)){
            final Account account = RcbAPI.getInstance().getAccount(player);

            if(account.getRank().getPower() <= RankUnit.FIRST_LEVEL_SPECIAL_RANK){
                player.sendMessage(new TextComponent("§e[File d'attente] §6Vous avez rejoin la file d'attente pour le jeu " + queueUnit.getName() + ", §6Priorité: §cTrès Élevée"));
            }
            else
            {
                player.sendMessage(new TextComponent("§e[File d'attente] §6Vous avez rejoin la file d'attente pour le jeu " + queueUnit.getName() + ", §6Priorité: §cÉlevée"));
            }

            int count = 0;
            for(ProxiedPlayer pls : queueUnit.getPlayers()){

                final Account account2 = RcbAPI.getInstance().getAccount(pls);

                if(account2.getRank().getPower() < account.getRank().getPower()){
                    queueUnit.getPlayers().remove(player);
                    queueUnit.getPlayers().add(count, player);
                }
                count = count+1;
            }

        }
        else {
            player.sendMessage(new TextComponent("§e[File d'attente] §6Vous avez rejoin la file d'attente pour le jeu " + queueUnit.getName() + ", §6Priorité: §fNormale"));
        }

        if(queueUnit.getPlayers().size() == 1){
            queueUnit.startTask();
        }

        Optional<AParty> optional = RcbAPI.getInstance().getAccountParty(player);
        if(optional.isPresent()){
            AParty aParty = optional.get();
            if(aParty.isInParty() && aParty.getParty().getCaptain().equals(player)){
                for(ProxiedPlayer pls : aParty.getParty().getPlayers()){
                    if(pls != null && !pls.equals(player)){
                        pls.sendMessage(new TextComponent("§d[Groupe] §6Vous avez rejoin la file d'attente avec votre groupe"));
                        checkForJoinQueue(pls, queueUnit);
                    }
                }
            }
        }
    }

    public static void leaveQueue(ProxiedPlayer ProxiedPlayer, QueueUnit queueUnit){
        if(proxiedPlayerIsInTheQueue(ProxiedPlayer, queueUnit)){
            queueUnit.getPlayers().remove(ProxiedPlayer);

            if(queueUnit.getPlayers().size() == 0){
                queueUnit.stopTask();
            }
            ProxiedPlayer.sendMessage(new TextComponent("§d[Groupe] §cVous avez quitté la file d'attente"));
        }
    }

    public static boolean proxiedPlayerIsInAQueue(ProxiedPlayer ProxiedPlayer){
        for(QueueUnit queueUnit : QueueUnit.values()){
            if(queueUnit.getPlayers().contains(ProxiedPlayer)){
                return true;
            }
        }
        return false;
    }

    public static boolean proxiedPlayerIsInTheQueue(ProxiedPlayer ProxiedPlayer, QueueUnit queueUnit){
        return queueUnit.getPlayers().contains(ProxiedPlayer);
    }

    public static QueueUnit getCurrentQueue(ProxiedPlayer ProxiedPlayer){
        if(proxiedPlayerIsInAQueue(ProxiedPlayer)){
            for(QueueUnit queueUnit : QueueUnit.values()){
                if(queueUnit.getPlayers().contains(ProxiedPlayer)){
                    return queueUnit;
                }
            }
        }
        return null;
    }

    public static Integer getPositionInQueue(ProxiedPlayer ProxiedPlayer, QueueUnit queueUnit){
        if(proxiedPlayerIsInTheQueue(ProxiedPlayer, queueUnit) && queueUnit.getPlayers().size() > 0){
            int counts = 1;
            int position;
            for(ProxiedPlayer pls : queueUnit.getPlayers()){
                counts = counts+1;
                if(ProxiedPlayer == pls){
                    position = counts;
                    return position;
                }
            }
        }
        return null;
    }

    public static boolean hasPriorityInQueues(ProxiedPlayer player){
        Account account = RcbAPI.getInstance().getAccount(player);
        RankUnit ProxiedPlayerRank = account.getRank();
        return ProxiedPlayerRank.getPower() <= RankUnit.FIRST_LEVEL_RANK && account.RankIsValid();
    }
}
