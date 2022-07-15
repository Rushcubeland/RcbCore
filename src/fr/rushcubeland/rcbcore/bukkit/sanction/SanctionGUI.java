package fr.rushcubeland.rcbcore.bukkit.sanction;

import fr.rushcubeland.rcbcore.bukkit.tools.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class SanctionGUI {

    private static final HashMap<Player, String> ModAndTarget = new HashMap<>();
    private static final HashMap<Player, SanctionUnit> CurrentSanctionForATarget = new HashMap<>();

    public static HashMap<Player, SanctionUnit> getCurrentSanctionForATarget() {
        return CurrentSanctionForATarget;
    }

    public static HashMap<Player, String> getModAndTarget() {
        return ModAndTarget;
    }

    public static void removeSanction(Player mod){
        getCurrentSanctionForATarget().remove(mod);
    }

    public static void openModGui(Player from, String targetname){
        ModAndTarget.put(from, targetname);
        Inventory inv = Bukkit.createInventory(null, 54, "§6Sanction §e> §c" + targetname);

        ItemStack headp = new ItemBuilder(Material.PLAYER_HEAD).setName("§c" + targetname).setSkullOwner(Bukkit.getOfflinePlayer(targetname)).toItemStack();
        inv.setItem(0, headp);

        ItemStack msg = new ItemBuilder(Material.PAPER).setName("§6Messages").setLore("§fSanction liée au §bcontenu §fd'un", "§fmessage", "§c ", "§a> §fClic gauche pour ouvrir").toItemStack();
        inv.setItem(2, msg);

        ItemStack gameplay = new ItemBuilder(Material.IRON_SWORD).setName("§6Gameplay").setLore("§fSanction liée au §bcomportement" , "§bin-game §f(Triche, anti-jeu...)", "§c", "§a> §fClic gauche pour ouvrir").toItemStack();
        ItemMeta gameplayItemMeta = gameplay.getItemMeta();
        if (gameplayItemMeta != null) {
            gameplayItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        }
        gameplay.setItemMeta(gameplayItemMeta);
        inv.setItem(3, gameplay);

        ItemStack triche = new ItemBuilder(Material.ENCHANTED_GOLDEN_APPLE).setName("§6Triche").setLore("§fSanction liée à l'utilisation d'un" , "§bmode de triche", "§c", "§a> §fClic gauche pour ouvrir").toItemStack();
        inv.setItem(4, triche);

        ItemStack abus = new ItemBuilder(Material.LAVA_BUCKET).setName("§6Abus").setLore("§fAbus des joueurs" , "§c", "§a> §fClic gauche pour ouvrir").toItemStack();
        inv.setItem(5, abus);

        ItemStack quit = new ItemBuilder(Material.ACACIA_DOOR).setName("§cRetour").toItemStack();
        inv.setItem(53, quit);
        from.openInventory(inv);
    }

    public static void openAbusGui(Player from, String targetname){
        Inventory inv = Bukkit.createInventory(null, 54, "§6Sanction Abus §e> §c" + targetname);

        ItemStack headp = new ItemBuilder(Material.PLAYER_HEAD).setName("§c" + targetname).setSkullOwner(Bukkit.getOfflinePlayer(targetname)).toItemStack();
        inv.setItem(0, headp);

        ItemStack abus = new ItemBuilder(Material.LAVA_BUCKET).setName("§6Abus").setLore("§fAbus des joueurs" , "§c", "§a> §fClic gauche pour ouvrir").toItemStack();
        inv.setItem(2, abus);

        ItemStack troll = new ItemBuilder(Material.PUMPKIN).setName("§6Troll").setLore("", "§c ", "§a> §fClic gauche pour appliquer").toItemStack();
        inv.setItem(9, troll);

        ItemStack report = new ItemBuilder(Material.DIAMOND_AXE).setName("§6Abus de report").setLore("", "§c ", "§a> §fClic gauche pour appliquer").toItemStack();
        ItemMeta reportItemMeta = report.getItemMeta();
        if (reportItemMeta != null) {
            reportItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        }
        report.setItemMeta(reportItemMeta);
        inv.setItem(10, report);

        ItemStack quit = new ItemBuilder(Material.ACACIA_DOOR).setName("§cRetour").toItemStack();
        inv.setItem(53, quit);

        from.openInventory(inv);

    }

    public static void openMsgGui(Player from, String targetname){
        Inventory inv = Bukkit.createInventory(null, 54, "§6Sanction Messages §e> §c" + targetname);

        ItemStack headp = new ItemBuilder(Material.PLAYER_HEAD).setName("§c" + targetname).setSkullOwner(Bukkit.getOfflinePlayer(targetname)).toItemStack();
        inv.setItem(0, headp);

        ItemStack msg = new ItemBuilder(Material.PAPER).setName("§6Messages").setLore("§fSanction liée au §bcontenu §fd'un", "§fmessage", "§c ", "§a> §fClic gauche pour ouvrir").toItemStack();
        inv.setItem(2, msg);

        ItemStack msginu = new ItemBuilder(Material.WOODEN_HOE).setName("§6Messages inutile").setLore("", "§c ", "§a> §fClic gauche pour appliquer").toItemStack();
        ItemMeta msginuItemMeta = msginu.getItemMeta();
        if (msginuItemMeta != null) {
            msginuItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        }
        msginu.setItemMeta(msginuItemMeta);
        inv.setItem(9, msginu);

        ItemStack fakenews = new ItemBuilder(Material.GLASS_BOTTLE).setName("§6Fausse information").setLore("", "§c ", "§a> §fClic gauche pour appliquer").toItemStack();
        inv.setItem(10, fakenews);

        ItemStack formatIncorrect = new ItemBuilder(Material.PUFFERFISH).setName("§6Formatage incorrect").setLore("", "§c ", "§a> §fClic gauche pour appliquer").toItemStack();
        inv.setItem(11, formatIncorrect);

        ItemStack ventard = new ItemBuilder(Material.GOLDEN_SWORD).setName("§6Ventardise").setLore("", "§c ", "§a> §fClic gauche pour appliquer").toItemStack();
        ItemMeta  ventardItemMeta = ventard.getItemMeta();
        if (ventardItemMeta != null) {
            ventardItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        }
        ventard.setItemMeta(ventardItemMeta);
        inv.setItem(12, ventard);

        ItemStack flood = new ItemBuilder(Material.TNT).setName("§6Flood/Spam").setLore("", "§c ", "§a> §fClic gauche pour appliquer").toItemStack();
        inv.setItem(13, flood);

        ItemStack mlangage = new ItemBuilder(Material.ROTTEN_FLESH).setName("§6Mauvais langage").setLore("", "§c ", "§a> §fClic gauche pour appliquer").toItemStack();
        inv.setItem(14, mlangage);

        ItemStack provoc = new ItemBuilder(Material.ZOMBIE_HEAD).setName("§6Provocation").setLore("", "§c ", "§a> §fClic gauche pour appliquer").toItemStack();
        inv.setItem(15, provoc);

        ItemStack insulte = new ItemBuilder(Material.CREEPER_HEAD).setName("§6Insulte").setLore("", "§c ", "§a> §fClic gauche pour appliquer").toItemStack();
        inv.setItem(16, insulte);

        ItemStack incitation = new ItemBuilder(Material.ENDER_PEARL).setName("§6Incitation à l'infraction").setLore("", "§c ", "§a> §fClic gauche pour appliquer").toItemStack();
        inv.setItem(17, incitation);

        ItemStack link = new ItemBuilder(Material.BARRIER).setName("§6Lien interdit").setLore("", "§c ", "§a> §fClic gauche pour appliquer").toItemStack();
        inv.setItem(18, link);

        ItemStack pub = new ItemBuilder(Material.BOOK).setName("§6Publicité").setLore("", "§c ", "§a> §fClic gauche pour appliquer").toItemStack();
        inv.setItem(19, pub);

        ItemStack ddoshack = new ItemBuilder(Material.CARROT_ON_A_STICK).setName("§6DDOS/Hack (lien)").setLore("", "§c ", "§a> §fClic gauche pour appliquer").toItemStack();
        inv.setItem(20, ddoshack);

        ItemStack pseudoincorrect = new ItemBuilder(Material.NAME_TAG).setName("§6Pseudo incorrect").setLore("", "§c ", "§a> §fClic gauche pour appliquer").toItemStack();
        inv.setItem(21, pseudoincorrect);

        ItemStack menaceirl = new ItemBuilder(Material.WITHER_SKELETON_SKULL).setName("§6Menaces IRL").setLore("", "§c ", "§a> §fClic gauche pour appliquer").toItemStack();
        inv.setItem(22, menaceirl);

        ItemStack quit = new ItemBuilder(Material.ACACIA_DOOR).setName("§cRetour").toItemStack();
        inv.setItem(53, quit);

        from.openInventory(inv);

    }

    public static void openTricheGui(Player from, String targetname){
        Inventory inv = Bukkit.createInventory(null, 54, "§6Sanction Triche §e> §c" + targetname);

        ItemStack headp = new ItemBuilder(Material.PLAYER_HEAD).setName("§c" + targetname).setSkullOwner(Bukkit.getOfflinePlayer(targetname)).toItemStack();
        inv.setItem(0, headp);

        ItemStack triche = new ItemBuilder(Material.ENCHANTED_GOLDEN_APPLE).setName("§6Triche").setLore("§fSanction liée à l'utilisation d'un" , "§bmode de triche", "§c", "§a> §fClic gauche pour ouvrir").toItemStack();
        inv.setItem(2, triche);

        ItemStack antikb = new ItemBuilder(Material.COBWEB).setName("§6Anti-knockback").setLore("", "§c ", "§a> §fClic gauche pour appliquer").toItemStack();
        inv.setItem(9, antikb);

        ItemStack killaura = new ItemBuilder(Material.IRON_SWORD).setName("§6KillAura/Forcefield").setLore("", "§c ", "§a> §fClic gauche pour appliquer").toItemStack();
        ItemMeta killauraItemMeta = killaura.getItemMeta();
        if (killauraItemMeta != null) {
            killauraItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        }
        killaura.setItemMeta(killauraItemMeta);
        inv.setItem(10, killaura);

        ItemStack fastplace = new ItemBuilder(Material.CLOCK).setName("§6FastPlace").setLore("", "§c ", "§a> §fClic gauche pour appliquer").toItemStack();
        inv.setItem(11, fastplace);

        ItemStack macro = new ItemBuilder(Material.DIAMOND_SHOVEL).setName("§6Macro").setLore("", "§c ", "§a> §fClic gauche pour appliquer").toItemStack();
        ItemMeta macroItemMeta = macro.getItemMeta();
        if (macroItemMeta != null) {
            macroItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        }
        macro.setItemMeta(macroItemMeta);
        inv.setItem(12, macro);

        ItemStack reach = new ItemBuilder(Material.BOW).setName("§6Reach").setLore("", "§c ", "§a> §fClic gauche pour appliquer").toItemStack();
        inv.setItem(13, reach);

        ItemStack fly = new ItemBuilder(Material.FEATHER).setName("§6Fly/Glide").setLore("", "§c ", "§a> §fClic gauche pour appliquer").toItemStack();
        inv.setItem(14, fly);

        ItemStack autre = new ItemBuilder(Material.STONE).setName("§6Autre").setLore("", "§c ", "§a> §fClic gauche pour appliquer").toItemStack();
        inv.setItem(15, autre);

        ItemStack quit = new ItemBuilder(Material.ACACIA_DOOR).setName("§cRetour").toItemStack();
        inv.setItem(53, quit);

        from.openInventory(inv);

    }

    public static void openGameplayGui(Player from, String targetname){
        Inventory inv = Bukkit.createInventory(null, 54, "§6Sanction Gameplay §e> §c" + targetname);

        ItemStack headp = new ItemBuilder(Material.PLAYER_HEAD).setName("§c" + targetname).setSkullOwner(Bukkit.getOfflinePlayer(targetname)).toItemStack();
        inv.setItem(0, headp);

        ItemStack gameplay = new ItemBuilder(Material.IRON_SWORD).setName("§6Gameplay").setLore("§fSanction liée au §bcomportement" , "§bin-game §f(Triche, anti-jeu...)", "§c", "§a> §fClic gauche pour ouvrir").toItemStack();
        ItemMeta gameplayItemMeta = gameplay.getItemMeta();
        if (gameplayItemMeta != null) {
            gameplayItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        }
        gameplay.setItemMeta(gameplayItemMeta);
        inv.setItem(2, gameplay);

        ItemStack ally = new ItemBuilder(Material.TROPICAL_FISH).setName("§6Alliance en jeu interdite").setLore("", "§c ", "§a> §fClic gauche pour appliquer").toItemStack();
        inv.setItem(9, ally);

        ItemStack skinincorect = new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("§6Skin incorrect").setLore("", "§c ", "§a> §fClic gauche pour appliquer").toItemStack();
        ItemMeta skinincorectItemMeta = skinincorect.getItemMeta();
        if (skinincorectItemMeta != null) {
            skinincorectItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        }
        skinincorect.setItemMeta(skinincorectItemMeta);
        inv.setItem(10, skinincorect);

        ItemStack antijeu = new ItemBuilder(Material.FLINT_AND_STEEL).setName("§6Anti-jeu").setLore("", "§c ", "§a> §fClic gauche pour appliquer").toItemStack();
        inv.setItem(11, antijeu);

        ItemStack quit = new ItemBuilder(Material.ACACIA_DOOR).setName("§cRetour").toItemStack();
        inv.setItem(53, quit);

        from.openInventory(inv);

    }

    public static void openConfirmSanctionGUI(Player from, String targetname, SanctionUnit sanction){
        CurrentSanctionForATarget.put(from, sanction);
        Inventory inv = Bukkit.createInventory(null, 54, "§6Sanction "+ sanction.getMotif() + " §e> §c" + targetname);

        ItemStack motifi = new ItemBuilder(Material.ITEM_FRAME).setName("§6" + sanction.getMotif()).setLore("§c ", "§fSanction: §c" + sanction.getSanctionCmd() + " §c" + sanction.getMultiplier() + sanction.getTimeUnit().getName(), "§f ", "§a> §fClic gauche pour appliquer").toItemStack();
        inv.setItem(22, motifi);

        ItemStack yes = new ItemBuilder(Material.LIME_DYE).setName("§aAppliquer la sanction").toItemStack();
        inv.setItem(29, yes);

        ItemStack no = new ItemBuilder(Material.RED_DYE).setName("§cNe pas appliquer la sanction").toItemStack();
        inv.setItem(33, no);

        from.openInventory(inv);
    }
}
