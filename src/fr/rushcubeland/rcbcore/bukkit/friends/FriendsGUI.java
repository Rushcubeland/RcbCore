package fr.rushcubeland.rcbcore.bukkit.friends;

import fr.rushcubeland.commons.AFriends;
import fr.rushcubeland.commons.AStats;
import fr.rushcubeland.commons.Account;
import fr.rushcubeland.commons.data.callbacks.AsyncCallBack;
import fr.rushcubeland.commons.utils.UUIDFetcher;
import fr.rushcubeland.rcbcore.bukkit.RcbAPI;
import fr.rushcubeland.rcbcore.bukkit.network.ServerUnit;
import fr.rushcubeland.rcbcore.bukkit.tools.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

public class FriendsGUI {

    private static final HashMap<Player, Inventory> invCache = new HashMap<>();

    public static void openFriendsGUI(Player player){
        if(invCache.containsKey(player)){
            player.openInventory(invCache.get(player));
            return;
        }

        generateInv(player);
        openFriendsGUI(player);
    }

    public static void generateInv(Player player){
        Inventory inv = Bukkit.createInventory(null, 54, "§d[Amis]");

        RcbAPI.getInstance().getAccountFriends(player, new AsyncCallBack() {
            @Override
            public void onQueryComplete(Object result) {
                AFriends aFriends = (AFriends) result;
                List<UUID> sortedFriends = sortFriends(aFriends.getFriends());

                final int[] a = {0};
                for(UUID friend : sortedFriends){
                    Player target = Bukkit.getPlayer(friend);
                    RcbAPI.getInstance().getAccount(friend, new AsyncCallBack() {

                        @Override
                        public void onQueryComplete(Object result) {
                            Account account = (Account) result;

                            RcbAPI.getInstance().getAccountStats(friend, new AsyncCallBack() {
                                @Override
                                public void onQueryComplete(Object result) {
                                    AStats aStats = (AStats) result;
                                    ItemStack head;
                                    if(target == null){
                                        if(account.isState(false)){
                                            if(aStats != null){
                                                if(aStats.getLastConnection() != null){
                                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                                                    dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
                                                    head = new ItemBuilder(Material.PLAYER_HEAD).setName(account.getRank().getPrefix() + UUIDFetcher.getNameFromUUID(friend))
                                                            .setLore(" ", "§fStatut: §cHors-ligne", "§fDernière connexion: §7" + dateFormat.format(aStats.getLastConnection()))
                                                            .setSkullOwner(Bukkit.getOfflinePlayer(friend)).toItemStack();
                                                }
                                                else
                                                {
                                                    head = new ItemBuilder(Material.PLAYER_HEAD).setName(account.getRank().getPrefix() + UUIDFetcher.getNameFromUUID(friend))
                                                            .setLore(" ", "§fStatut: §cHors-ligne", "§fDernière connexion: §7null")
                                                            .setSkullOwner(Bukkit.getOfflinePlayer(friend)).toItemStack();
                                                }
                                            }
                                            else
                                            {
                                                head = new ItemBuilder(Material.PLAYER_HEAD).setName(account.getRank().getPrefix() + UUIDFetcher.getNameFromUUID(friend))
                                                        .setLore(" ", "§fStatut: §cHors-ligne", "§fDernière connexion: §7null")
                                                        .setSkullOwner(Bukkit.getOfflinePlayer(friend)).toItemStack();
                                            }
                                        }
                                        else
                                        {
                                            String serverName = account.getServer();
                                            head = new ItemBuilder(Material.PLAYER_HEAD).setName(account.getRank().getPrefix() + UUIDFetcher.getNameFromUUID(friend)).setLore(" ", "§fStatut: §aEn-ligne", "§fServeur: §6" + serverName).setSkullOwner(Bukkit.getOfflinePlayer(friend)).toItemStack();
                                        }
                                    }
                                    else
                                    {
                                        if(ServerUnit.getByPort(target.getServer().getPort()).isPresent()){
                                            String serverName = ServerUnit.getByPort(target.getServer().getPort()).get().getName();
                                            head = new ItemBuilder(Material.PLAYER_HEAD).setName(account.getRank().getPrefix() + target.getName()).setLore(" ", "§fStatut: §aEn-ligne", "§fServeur: §6" + serverName).setSkullOwner(Bukkit.getPlayer(friend)).toItemStack();
                                        }
                                        else {
                                            head = new ItemBuilder(Material.PLAYER_HEAD).setName(account.getRank().getPrefix() + target.getName()).setLore(" ", "§fStatut: §aEn-ligne", "§fServeur: §6Null").setSkullOwner(Bukkit.getPlayer(friend)).toItemStack();
                                        }
                                    }
                                    inv.setItem(a[0], head);
                                    a[0] = a[0] +1;
                                }

                                @Override
                                public void onQueryError(Exception e) {

                                }
                            });
                        }

                        @Override
                        public void onQueryError(Exception e) {

                        }
                    });

                }

                inv.setItem(53, new ItemBuilder(Material.ACACIA_DOOR).setName("§cQuitter").toItemStack());

                invCache.put(player, inv);
            }

            @Override
            public void onQueryError(Exception e) {
                e.printStackTrace();
                player.kickPlayer("§cVotre compte n'a pas été trouvé, veuillez contacter un administrateur.");
            }
        });


    }

    private static List<UUID> sortFriends(List<UUID> list){

        final int[] count = {0};
        if(list.size() <= 1){
            return list;
        }
        for(UUID frienduuid : list){
            UUID friend2uuid = list.get(count[0]);
            RcbAPI.getInstance().getAccount(frienduuid, new AsyncCallBack() {
                @Override
                public void onQueryComplete(Object result) {
                    Account account = (Account) result;
                    RcbAPI.getInstance().getAccount(friend2uuid, new AsyncCallBack() {
                        @Override
                        public void onQueryComplete(Object result) {
                            Account account2 = (Account) result;
                            if(!account.isState(true) && account2.isState(false)){
                                list.remove(frienduuid);
                                list.add(frienduuid);
                            }
                            count[0] += 1;
                        }
                        @Override
                        public void onQueryError(Exception e) {

                        }
                    });
                }
                @Override
                public void onQueryError(Exception e) {

                }
            });
        }
        return list;
    }

    public static HashMap<Player, Inventory> getInvCache() {
        return invCache;
    }
}