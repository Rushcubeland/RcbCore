package fr.rushcubeland.rcbcore.bukkit.listeners;

import fr.rushcubeland.commons.APermissions;
import fr.rushcubeland.commons.Account;
import fr.rushcubeland.commons.data.callbacks.AsyncCallBack;
import fr.rushcubeland.commons.permissions.PermissionsUnit;
import fr.rushcubeland.commons.rank.RankUnit;
import fr.rushcubeland.rcbcore.bukkit.RcbAPI;
import fr.rushcubeland.rcbcore.bukkit.friends.FriendsGUI;
import fr.rushcubeland.rcbcore.bukkit.mod.ModModerator;
import fr.rushcubeland.rcbcore.bukkit.tools.PacketReader;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.*;

public class JoinEvent implements Listener {

    public static Map<UUID, PermissionAttachment> permissionMap = new HashMap<>();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();

        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(16.0D);

        RcbAPI.getInstance().getAccount(player, new AsyncCallBack() {
            @Override
            public void onQueryComplete(Object result) {

                Account account = (Account) result;

                initPermissions(player, account.getRank());

                PacketReader reader = new PacketReader();
                reader.inject(player);

                if(ModModerator.isInModData(player.getUniqueId().toString())){
                    e.setJoinMessage(null);
                    player.setAllowFlight(true);
                    player.setFlying(true);
                    ModModerator.giveTools(player.getUniqueId().toString());
                    for(Player pls : Bukkit.getOnlinePlayers()){
                        if(!ModModerator.isInModData(pls.getUniqueId().toString())){
                            pls.hidePlayer(RcbAPI.getInstance(), player);
                        }
                    }
                }
                else
                {
                    for(Player pls : Bukkit.getOnlinePlayers()){
                        if(ModModerator.isInModData(pls.getUniqueId().toString())){
                            player.hidePlayer(RcbAPI.getInstance(), pls);
                        }
                    }
                }
                taskFriendGUIGeneration(player);
            }

            @Override
            public void onQueryError(Exception e) {
                e.printStackTrace();
                player.kickPlayer("§cVotre compte n'a pas été trouvé, veuillez contacter un administrateur.");
            }
        });
    }

    private void initPermissions(Player player, RankUnit rank){
        Set<PermissionAttachmentInfo> permissions = new HashSet<>(player.getEffectivePermissions());
        for (PermissionAttachmentInfo permissionInfo : permissions) {
            String permission = permissionInfo.getPermission();

            getPermissionAttachment(player).setPermission(permission, false);
        }
        for(PermissionsUnit permUnit : PermissionsUnit.values()){
            getPermissionAttachment(player).setPermission(permUnit.getPermission(), false);
        }
        initPlayerPermissions(player);
        initRankPlayerPermissions(player, rank);
    }

    private void initRankPlayerPermissions(Player player, RankUnit rank){
        if(player != null && rank != null){
            ArrayList<String> perms = rank.getPermissions();
            if (perms != null) {
                for(String perm : perms) {
                    if (perm != null) {
                        getPermissionAttachment(player).setPermission(perm, true);
                    }
                }
            }
        }
    }

    private void initPlayerPermissions(Player player){
        if(player != null){
            RcbAPI.getInstance().getAccountPermissions(player, new AsyncCallBack() {
                @Override
                public void onQueryComplete(Object result) {
                    APermissions aPermissions = (APermissions) result;
                    if(aPermissions != null) {
                        List<String> perms = aPermissions.getPermissions();
                        if (perms != null) {
                            for (String perm : perms) {
                                if (perm != null) {
                                    getPermissionAttachment(player).setPermission(perm, true);
                                }
                            }
                        }
                    }
                }
                @Override
                public void onQueryError(Exception e) {
                    e.printStackTrace();
                    player.kickPlayer("§cVotre compte n'a pas été trouvé, veuillez contacter un administrateur.");
                }
            });
        }
    }

    private void taskFriendGUIGeneration(Player player){
        Bukkit.getScheduler().runTaskLater(RcbAPI.getInstance(), () -> FriendsGUI.generateInv(player), 80L);
    }

    private static PermissionAttachment getPermissionAttachment(Player player) {
        if (!permissionMap.containsKey(player.getUniqueId())) {
            permissionMap.put(player.getUniqueId(), player.addAttachment(RcbAPI.getInstance()));
        }
        return permissionMap.get(player.getUniqueId());
    }

}
