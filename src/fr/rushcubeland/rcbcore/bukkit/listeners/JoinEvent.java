package fr.rushcubeland.rcbcore.bukkit.listeners;

import fr.rushcubeland.commons.APermissions;
import fr.rushcubeland.commons.Account;
import fr.rushcubeland.commons.data.callbacks.AsyncCallBack;
import fr.rushcubeland.commons.permissions.PermissionsUnit;
import fr.rushcubeland.commons.rank.RankUnit;
import fr.rushcubeland.rcbcore.bukkit.RcbAPI;
import fr.rushcubeland.rcbcore.bukkit.friends.FriendsGUI;
import fr.rushcubeland.rcbcore.bukkit.mod.ModModerator;
import fr.rushcubeland.rcbcore.bukkit.tools.ItemBuilder;
import fr.rushcubeland.rcbcore.bukkit.tools.PacketReader;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
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

    public static void giveLobbyJoinItems(Player player){
        ItemStack menup = new ItemBuilder(Material.COMPASS).setName("§6Menu Principal").setLore("§f ", "").toItemStack();
        menup.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        ItemMeta menupm = menup.getItemMeta();
        if (menupm != null) {
            menupm.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        menup.setItemMeta(menupm);

        ItemStack settings = new ItemBuilder(Material.COMPARATOR).setName("§cPréférences").setLore("§f ", "").toItemStack();

        ItemStack magicClock = new ItemBuilder(Material.CLOCK).setName("§7Ombre de Tartare: §cDésactivé").setLore("§f ", "").toItemStack();

        ItemStack profil = new ItemBuilder(Material.PLAYER_HEAD).setName("§eVotre profil").setLore("§f ", "").toItemStack();
        SkullMeta profilm = (SkullMeta) profil.getItemMeta();
        if (profilm != null) {
            profilm.setOwningPlayer(player);
        }
        profil.setItemMeta(profilm);

        ItemStack amis = new ItemBuilder(Material.PUFFERFISH).setName("§eAmis").setLore("§f ", "").removeFlags().toItemStack();

        player.getInventory().setItem(0, menup);
        player.getInventory().setItem(4, magicClock);
        player.getInventory().setItem(8, settings);
        player.getInventory().setItem(1, profil);
        player.getInventory().setItem(6, amis);
    }

}
