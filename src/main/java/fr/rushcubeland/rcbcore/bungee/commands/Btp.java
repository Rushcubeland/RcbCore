package fr.rushcubeland.rcbcore.bungee.commands;

import fr.rushcubeland.commons.Account;
import fr.rushcubeland.commons.permissions.PermissionsUnit;
import fr.rushcubeland.commons.rank.RankUnit;
import fr.rushcubeland.commons.utils.MessageUtil;
import fr.rushcubeland.rcbcore.bungee.BungeeSend;
import fr.rushcubeland.rcbcore.bungee.RcbAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Btp extends Command {

    public static final List<String> ALIASES = Arrays.asList("btp", "bungeeteleport");

    public Btp(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new TextComponent(ChatColor.RED + "Seul un joueur peut effectuer cette commande !"));
            return;
        }
        if (!sender.hasPermission(PermissionsUnit.TELEPORT.getPermission()) && !sender.hasPermission(PermissionsUnit.ALL.getPermission())) {
            sender.sendMessage(new TextComponent(MessageUtil.NO_PERM.getMessage()));
            return;
        }
        if (args.length < 1) {
            sender.sendMessage(new TextComponent(ChatColor.RED + "Erreur: /btp <player> [<player>]"));

            return;
        }
        if (args.length == 1) {

            ProxiedPlayer from = (ProxiedPlayer) sender;
            ProxiedPlayer to = ProxyServer.getInstance().getPlayer(args[0]);


            if (args[0] != null && to == null) {
                from.sendMessage(new TextComponent(MessageUtil.PLAYER_NOT_ONLINE.getMessage()));

                return;
            }
            teleport(from, to);
            Account accountT = RcbAPI.getInstance().getAccount(to);
            RankUnit rankT = accountT.getRank();
            from.sendMessage(new TextComponent("§6Vous avez été téleporté vers " + rankT.getPrefix() + to.getDisplayName()));

            return;
        }

        if (args.length == 2) {

            ProxiedPlayer from = ProxyServer.getInstance().getPlayer(args[0]);

            ProxiedPlayer to = ProxyServer.getInstance().getPlayer(args[1]);


            if (from == null) {
                sender.sendMessage(new TextComponent(ChatColor.RED + args[0] + " n'est pas en-ligne !"));

                return;
            }
            if (to == null) {
                sender.sendMessage(new TextComponent(ChatColor.RED + args[1] + " n'est pas en-ligne !"));

                return;
            }
            teleport(from, to);

            Account accountF = RcbAPI.getInstance().getAccount(from);
            RankUnit rankF = accountF.getRank();

            Account accountT = RcbAPI.getInstance().getAccount(to);
            RankUnit rankT = accountT.getRank();

            sender.sendMessage(new TextComponent(rankF.getPrefix() + from.getDisplayName() + " §6a été téleporté vers " + rankT.getPrefix() + to.getDisplayName()));
        }

    }

    public static void teleport(ProxiedPlayer from, ProxiedPlayer to) {
        if(from == to){
            return;
        }
        if (from.getServer().getInfo() != to.getServer().getInfo()) {
            from.connect(to.getServer().getInfo());
        }

        ProxyServer.getInstance().getScheduler().schedule(RcbAPI.getInstance(), () -> BungeeSend.teleport(from, to), 1L, TimeUnit.SECONDS);
    }

}