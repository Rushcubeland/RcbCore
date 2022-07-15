package fr.rushcubeland.rcbcore.bungee.listeners;

import fr.rushcubeland.commons.AFriends;
import fr.rushcubeland.commons.AParty;
import fr.rushcubeland.commons.APermissions;
import fr.rushcubeland.commons.Account;
import fr.rushcubeland.commons.data.exceptions.AccountNotFoundException;
import fr.rushcubeland.commons.permissions.PermissionsUnit;
import fr.rushcubeland.commons.rank.RankUnit;
import fr.rushcubeland.rcbcore.bungee.RcbAPI;
import fr.rushcubeland.rcbcore.bungee.friends.Friend;
import fr.rushcubeland.rcbcore.bungee.maintenance.Maintenance;
import fr.rushcubeland.rcbcore.bungee.maintenance.MaintenanceMode;
import fr.rushcubeland.rcbcore.bungee.provider.*;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProxiedPlayerJoin implements Listener {

    @EventHandler
    public void onConnect(PostLoginEvent e){
        ProxiedPlayer player = e.getPlayer();

        initAllDataProvider(player);

        Account account = RcbAPI.getInstance().getAccount(player);
        account.setState(true);
        RcbAPI.getInstance().sendAccountToRedis(account);

        Friend.joinNotifFriends(player);
        initPermissions(player, RcbAPI.getInstance().getAccount(player).getRank());
    }

    @EventHandler
    public void onLogin(LoginEvent e){
        RcbAPI.getInstance().getBanManager().checkDuration(e.getConnection().getUniqueId());

        if(RcbAPI.getInstance().getBanManager().isBanned(e.getConnection().getUniqueId())) {
            e.setCancelled(true);
            e.setCancelReason(new TextComponent("§cVous avez été banni !\n \n§eRaison : §f" +
                    RcbAPI.getInstance().getBanManager().getReason(e.getConnection().getUniqueId()) + "\n \n §aTemps restant : §f" +
                    RcbAPI.getInstance().getBanManager().getTimeLeft(e.getConnection().getUniqueId())));
        }

        if(Maintenance.INSTANCE.getMaintenanceMode().equals(MaintenanceMode.ON) && !Maintenance.INSTANCE.isIn(e.getConnection().getUniqueId())){
            e.setCancelled(true);
            e.setCancelReason(new TextComponent(Maintenance.getReason()));
        }
    }

    private void initPermissions(ProxiedPlayer player, RankUnit rank){
        List<String> perms = new ArrayList<>(player.getPermissions());
        perms.forEach(perm -> {
            player.setPermission(perm, false);
        });
        initPlayerPermissions(player);
        initRankPlayerPermissions(player, rank);
    }

    private void initRankPlayerPermissions(ProxiedPlayer player, RankUnit rank){
        if(rank.getPermissions().isEmpty()){
            return;
        }
        for(String perm : rank.getPermissions()){
            player.setPermission(perm, true);
        }
    }

    private void initPlayerPermissions(ProxiedPlayer player){
        APermissions aPermissions = RcbAPI.getInstance().getAccountPermissions(player);
        for(String perm : aPermissions.getPermissions()){
            player.setPermission(perm, true);
        }
    }

        private void initAllDataProvider(ProxiedPlayer player){

            AParty aParty = new AParty(player.getUniqueId());
            RcbAPI.getInstance().getAPartyList().add(aParty);

            try {

                final AccountProvider accountProvider = new AccountProvider(player);
                accountProvider.getAccount();

            } catch (AccountNotFoundException exception) {
                System.err.println(exception.getMessage());
                player.disconnect(new TextComponent("§cVotre compte n'a pas été crée ou trouvé !"));
            }

            ProxyServer.getInstance().getScheduler().runAsync(RcbAPI.getInstance(), () -> {
                try {

                    final OptionsProvider optionsProvider = new OptionsProvider(player);
                    optionsProvider.getAccount();

                } catch (AccountNotFoundException exception) {
                System.err.println(exception.getMessage());
                player.disconnect(new TextComponent("§cVotre compte n'a pas été crée ou trouvé !"));
            }

            try {

                final StatsProvider statsProvider = new StatsProvider(player);
                statsProvider.getAccount();

            } catch (AccountNotFoundException exception) {
                System.err.println(exception.getMessage());
                player.disconnect(new TextComponent("§cVotre compte n'a pas été crée ou trouvé !"));
            }

            try {

                final CosmeticsProvider cosmeticsProvider = new CosmeticsProvider(player);
                cosmeticsProvider.getAccount();

            } catch (AccountNotFoundException exception) {
                System.err.println(exception.getMessage());
                player.disconnect(new TextComponent("§cVotre compte n'a pas été crée ou trouvé !"));
            }

            try {

                final FriendsProvider friendsProvider = new FriendsProvider(player);
                AFriends aFriends = friendsProvider.getAccount();

                final AccountProvider accountProvider = new AccountProvider(player);
                Account account = accountProvider.getAccount();

                if(account.getRank().getPower() >= 45){
                    aFriends.setMaxFriends(30);
                }
                else if(account.getRank().getPower() >= 40){
                    aFriends.setMaxFriends(40);
                }
                else if(account.getRank().getPower() <= 38){
                    aFriends.setMaxFriends(50);
                }

                friendsProvider.sendFriendsToRedis(aFriends);

            } catch (AccountNotFoundException exception) {
                System.err.println(exception.getMessage());
                player.disconnect(new TextComponent("§cVotre compte n'a pas été crée ou trouvé !"));
            }

            try {

                final PermissionsProvider permissionsProvider = new PermissionsProvider(player);
                permissionsProvider.getAccount();


            } catch (AccountNotFoundException exception) {
                System.err.println(exception.getMessage());
                player.disconnect(new TextComponent("§cVotre compte n'a pas été crée ou trouvé !"));
            }
        });
    }
}
