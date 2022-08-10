package fr.rushcubeland.rcbcore.bungee.commands;

import fr.rushcubeland.commons.Account;
import fr.rushcubeland.commons.data.exceptions.AccountNotFoundException;
import fr.rushcubeland.commons.permissions.PermissionsUnit;
import fr.rushcubeland.commons.protocol.ProtocolVersion;
import fr.rushcubeland.commons.utils.MessageUtil;
import fr.rushcubeland.rcbcore.bungee.RcbAPI;
import fr.rushcubeland.rcbcore.bungee.mod.ModModerator;
import fr.rushcubeland.rcbcore.bungee.provider.AccountProvider;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class WhoisCommand extends Command {

    private static final String cmd = "whois";

    public WhoisCommand() {
        super("whois");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer){
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if(!player.hasPermission(PermissionsUnit.WHOIS.getPermission()) && !player.hasPermission(PermissionsUnit.ALL.getPermission())){
                player.sendMessage(new TextComponent(MessageUtil.NO_PERM.getMessage()));
                return;
            }
            if(args.length < 1){
                infos(player, player);
            }
            if(args.length == 1){
                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
                if(target != null){
                    infos(player, target);
                }
            }
        }
    }

    public void infos(ProxiedPlayer sender, ProxiedPlayer target) {
        ProxyServer.getInstance().getScheduler().runAsync(RcbAPI.getInstance(), () -> {

            try {

            final AccountProvider accountProvider = new AccountProvider(target);
            final Account account = accountProvider.getAccount();

                if(ModModerator.isInModData(target.getUniqueId().toString())){
                    sender.sendMessage(new TextComponent("§6[Whois] " + account.getRank().getPrefix() + target.getName() + " §6(MOD) §f:"));
                }
                else
                {
                    sender.sendMessage(new TextComponent("§6[Whois] " + account.getRank().getPrefix() + target.getName() + " :"));
                }

                String ip = target.getSocketAddress().toString();
                sender.sendMessage(new TextComponent("§fAdresse: §7" + ip));
                TextComponent server = new TextComponent(target.getServer().getInfo().getName());
                server.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/btp" + target.getName()));
                server.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§fRejoindre").create()));
                sender.sendMessage(new ComponentBuilder("§fServeur: §c").append(server).create());
                sender.sendMessage(new TextComponent("§fPing: §7" + target.getPing() + "ms"));
                sender.sendMessage(new TextComponent("§fVersion: §e" + getVersionStringPlayer(target)));

            } catch (AccountNotFoundException e) {
                e.printStackTrace();
            }
            
        });
    }

    private String getVersionStringPlayer(ProxiedPlayer player){
        int version = player.getPendingConnection().getVersion();
        ProtocolVersion protocolVersion = ProtocolVersion.valueOf(version);
        if(protocolVersion != null){
            return protocolVersion.getName();
        }
        return "Non officielle";

    }

    public static String getCmd() {
        return cmd;
    }
}
