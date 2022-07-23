package fr.rushcubeland.rcbcore.bungee.commands;

import fr.rushcubeland.commons.Account;
import fr.rushcubeland.commons.permissions.PermissionsUnit;
import fr.rushcubeland.commons.rank.RankUnit;
import fr.rushcubeland.commons.utils.MessageUtil;
import fr.rushcubeland.rcbcore.bungee.RcbAPI;
import fr.rushcubeland.rcbcore.bungee.mod.ModModerator;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Collection;

public class StaffListCommand extends Command {

    private static final String cmd = "staff";

    public StaffListCommand() {
        super(cmd);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer){
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if(!player.hasPermission(PermissionsUnit.STAFF_LIST.getPermission()) && !player.hasPermission(PermissionsUnit.ALL.getPermission())){
                player.sendMessage(new TextComponent(MessageUtil.NO_PERM.getMessage()));
                return;
            }

            Collection<ProxiedPlayer> list = ProxyServer.getInstance().getPlayers();

            ProxyServer.getInstance().getScheduler().runAsync(RcbAPI.getInstance(), () -> {

                player.sendMessage(new TextComponent("§6"));
                player.sendMessage(new TextComponent("§fVoici la liste des membres du staff en ligne:"));

                list.forEach(element -> {
                    Account account = RcbAPI.getInstance().getAccount(element);
                    RankUnit rank = account.getRank();

                    if(rank.getPower() <= 20){
                        TextComponent tp = new TextComponent(" §6[§aTP§6]");
                        tp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§6Se téléporter")));
                        tp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/btp " + element.getName()));
                        if(ModModerator.isInModData(element.getUniqueId().toString())){
                            player.sendMessage(new ComponentBuilder("§6(MOD) " + rank.getPrefix() + element.getName()).append(tp).create());

                        }
                        else
                        {
                            player.sendMessage(new ComponentBuilder(rank.getPrefix() + element.getName()).append(tp).create());
                        }
                    }
                });
                player.sendMessage(new TextComponent("§6"));
            });
        }
    }
}
