package fr.rushcubeland.rcbcore.bukkit.options;

import fr.rushcubeland.commons.AOptions;
import fr.rushcubeland.commons.data.callbacks.AsyncCallBack;
import fr.rushcubeland.commons.options.OptionUnit;
import fr.rushcubeland.rcbcore.bukkit.RcbAPI;
import fr.rushcubeland.rcbcore.bukkit.tools.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Options {

    public static void openOptionsInv(Player player){

        Inventory inv = Bukkit.createInventory(null, 54, "§cPréférences");

        RcbAPI.getInstance().getAccountOptions(player, new AsyncCallBack() {
            @Override
            public void onQueryComplete(Object result) {
                AOptions account = (AOptions) result;
                if(account == null){
                    return;
                }

                ItemStack chat;
                ItemStack state;

                if(account.getStateChat() == OptionUnit.OPEN){
                    chat = new ItemBuilder(Material.PAPER).setName("§6Afficher le chat").setLore("§fActiver ou désactiver le chat", "§c ", "§bActuellement: §aActivé").toItemStack();
                    state = new ItemBuilder(Material.LIME_DYE).setName("§bActuellement: §aActivé").setLore("§c ", "§a> §fClic gauche pour §cDésactiver").toItemStack();
                }
                else
                {
                    chat = new ItemBuilder(Material.PAPER).setName("§6Afficher le chat").setLore("§fActiver ou désactiver le chat", "§c ", "§bActuellement: §cDésactivé").toItemStack();
                    state = new ItemBuilder(Material.GRAY_DYE).setName("§bActuellement: §cDésactivé").setLore("§c ", "§a> §fClic gauche pour §aActiver").toItemStack();
                }
                inv.setItem(0, chat);
                inv.setItem(1, state);

                ItemStack friendRequests;
                ItemStack state2;

                if(account.getStateFriendRequests().equals(OptionUnit.OPEN)){
                    friendRequests = new ItemBuilder(Material.PLAYER_HEAD).setName("§6Recevoir des requetes d'amis").setLore("§fActiver ou désactiver le fait de recevoir des requetes d'amis", "§c ", "§bActuellement: §aActivé").toItemStack();
                    state2 = new ItemBuilder(Material.LIME_DYE).setName("§bActuellement: §aActivé").setLore("§c ", "§a> §fClic gauche pour §cDésactiver").toItemStack();
                }
                else
                {
                    friendRequests = new ItemBuilder(Material.PLAYER_HEAD).setName("§6Recevoir des requetes d'amis").setLore("§fActiver ou désactiver le fait de recevoir des requetes d'amis", "§c ", "§bActuellement: §cDésactivé").toItemStack();
                    state2 = new ItemBuilder(Material.GRAY_DYE).setName("§bActuellement: §cDésactivé").setLore("§c ", "§a> §fClic gauche pour §aActiver").toItemStack();
                }
                inv.setItem(3, friendRequests);
                inv.setItem(4, state2);

                ItemStack mp;
                ItemStack state3;

                if(account.getStateMP().equals(OptionUnit.OPEN)){
                    mp = new ItemBuilder(Material.WRITABLE_BOOK).setName("§6Recevoir des messages privés").setLore("§fActiver ou désactiver le fait de recevoir des messages privés", "§c ", "§bActuellement: §aActivé").toItemStack();
                    state3 = new ItemBuilder(Material.LIME_DYE).setName("§bActuellement: §aActivé").setLore("§c ", "§a> §fClic gauche pour §cDésactiver").toItemStack();
                    inv.setItem(18, mp);
                    inv.setItem(19, state3);
                }
                else if(account.getStateMP().equals(OptionUnit.ONLY_FRIENDS)){
                    mp = new ItemBuilder(Material.WRITABLE_BOOK).setName("§6Recevoir des messages privés").setLore("§fActiver ou désactiver le fait de recevoir des messages privés", "§c ", "§bActuellement: §eAmis uniquement").toItemStack();
                    state3 = new ItemBuilder(Material.CYAN_DYE).setName("§bActuellement: §eAmis uniquement").setLore("§c ", "§a> §fClic gauche pour §aActiver").toItemStack();
                    inv.setItem(18, mp);
                    inv.setItem(19, state3);
                }
                else
                {
                    mp = new ItemBuilder(Material.WRITABLE_BOOK).setName("§6Recevoir des messages privés").setLore("§fActiver ou désactiver le fait de recevoir des messages privés", "§c ", "§bActuellement: §cDésactivé").toItemStack();
                    state3 = new ItemBuilder(Material.GRAY_DYE).setName("§bActuellement: §cDésactivé").setLore("§c ", "§a> §fClic gauche pour §aActiver").toItemStack();
                    inv.setItem(18, mp);
                    inv.setItem(19, state3);
                }

                ItemStack notif;
                ItemStack state4;

                if(account.getStateFriendsStatutNotif().equals(OptionUnit.OPEN)){
                    notif = new ItemBuilder(Material.NETHER_STAR).setName("§6Notifications de co/deco des amis").setLore("§fActiver ou désactiver le fait de recevoir des messages privés", "§c ", "§bActuellement: §aActivé").toItemStack();
                    state4 = new ItemBuilder(Material.LIME_DYE).setName("§bActuellement: §aActivé").setLore("§c ", "§a> §fClic gauche pour §cDésactiver").toItemStack();
                }
                else
                {
                    notif = new ItemBuilder(Material.NETHER_STAR).setName("§6Notifications de co/deco des amis").setLore("§fActiver ou désactiver le fait de recevoir des messages privés", "§c ", "§bActuellement: §cDésactivé").toItemStack();
                    state4 = new ItemBuilder(Material.GRAY_DYE).setName("§bActuellement: §cDésactivé").setLore("§c ", "§a> §fClic gauche pour §aActiver").toItemStack();
                }

                inv.setItem(21, notif);
                inv.setItem(22, state4);

                ItemStack party;
                ItemStack state5;

                if(account.getStatePartyInvite().equals(OptionUnit.OPEN)){
                    party = new ItemBuilder(Material.MINECART).setName("§6Recevoir des invitations de groupe").setLore("§fActiver ou désactiver le fait de recevoir des invitations de groupe", "§c ", "§bActuellement: §aActivé").toItemStack();
                    state5 = new ItemBuilder(Material.LIME_DYE).setName("§bActuellement: §aActivé").setLore("§c ", "§a> §fClic gauche pour §cDésactiver").toItemStack();
                }
                else if(account.getStatePartyInvite().equals(OptionUnit.ONLY_FRIENDS)){
                    party = new ItemBuilder(Material.MINECART).setName("§6Recevoir des invitations de groupe").setLore("§fActiver ou désactiver le fait de recevoir des invitations de groupe", "§c ", "§bActuellement: §eAmis uniquement").toItemStack();
                    state5 = new ItemBuilder(Material.CYAN_DYE).setName("§bActuellement: §eAmis uniquement").setLore("§c ", "§a> §fClic gauche pour §aActiver").toItemStack();
                }
                else
                {
                    party = new ItemBuilder(Material.MINECART).setName("§6Recevoir des invitations de groupe").setLore("§fActiver ou désactiver le fait de recevoir des invitations de groupe", "§c ", "§bActuellement: §cDésactivé").toItemStack();
                    state5 = new ItemBuilder(Material.GRAY_DYE).setName("§bActuellement: §cDésactivé").setLore("§c ", "§a> §fClic gauche pour §aActiver").toItemStack();
                }

                inv.setItem(36, party);
                inv.setItem(37, state5);


                ItemStack close = new ItemBuilder(Material.ACACIA_DOOR).setName("§cFermer").toItemStack();
                inv.setItem(53, close);

                player.openInventory(inv);
            }
        });
    }
}
