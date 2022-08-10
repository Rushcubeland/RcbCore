package fr.rushcubeland.rcbcore.bungee.report;

import fr.rushcubeland.commons.permissions.PermissionsUnit;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;

public class Report {

    private static final HashMap<ProxiedPlayer, Boolean> reportToogleData = new HashMap<>();

    public static void reportMsg(ProxiedPlayer player, ProxiedPlayer target, String message){
        for (ProxiedPlayer mods : ProxyServer.getInstance().getPlayers()) {
            if(mods.hasPermission(PermissionsUnit.REPORT.getPermission())){
                if(!reportToogleData.containsKey(mods)){
                    displayReportMSG(message, mods, player, target);
                }
                else if(reportToogleData.get(mods)){
                    displayReportMSG(message, mods, player, target);
                }
            }
        }
    }

    public static void report(ProxiedPlayer player, ProxiedPlayer target, String raison) {
        for (ProxiedPlayer mods : ProxyServer.getInstance().getPlayers()) {
            if(mods.hasPermission(PermissionsUnit.REPORT.getPermission())){
                if(!reportToogleData.containsKey(mods)){
                    displayReport(raison, mods, player, target);
                }
                else if(reportToogleData.get(mods)){
                    displayReport(raison, mods, player, target);
                }
            }
        }
    }

    private static void displayReportMSG(String message, ProxiedPlayer mod, ProxiedPlayer player, ProxiedPlayer target){
        mod.sendMessage(new TextComponent("§e---------§d[Report]§e---------"));
        TextComponent l2 = new TextComponent("§fJoueur signalé: §c");
        TextComponent l3 = new TextComponent("§7- par §6");
        TextComponent targetn = new TextComponent(target.getName());
        TextComponent playern = new TextComponent(player.getName());
        targetn.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/apmsg " + target.getName()));
        targetn.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/apmsg "+ target.getName()).create()));
        playern.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/apmsg " + player.getName()));
        playern.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/apmsg "+ player.getName()).create()));
        mod.sendMessage((new ComponentBuilder(l2)).append(targetn).create());
        mod.sendMessage((new ComponentBuilder(l3)).append(playern).create());
        mod.sendMessage(new TextComponent("§fMessage signalé: §b" + message.replace(" ", " §b")));
        mod.sendMessage(new TextComponent("§e-------------------------"));
    }

    private static void displayReport(String raison, ProxiedPlayer mod, ProxiedPlayer player, ProxiedPlayer target){
        mod.sendMessage(new TextComponent("§e---------§d[Report]§e---------"));
        TextComponent l2 = new TextComponent("§fJoueur signalé: §c");
        TextComponent l3 = new TextComponent("§7- par §6");
        TextComponent targetn = new TextComponent(target.getName());
        TextComponent playern = new TextComponent(player.getName());
        targetn.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/btp " + target.getName()));
        targetn.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/btp "+ target.getName()).create()));
        playern.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/btp " + player.getName()));
        playern.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/btp "+ player.getName()).create()));
        mod.sendMessage(new ComponentBuilder(l2).append(targetn).create());
        mod.sendMessage(new ComponentBuilder(l3).append(playern).create());
        mod.sendMessage(new TextComponent("§fRaison: §b" + raison.replace(" ", " §b")));
        mod.sendMessage(new TextComponent("§e-------------------------"));
    }

    public static HashMap<ProxiedPlayer, Boolean> getReportToogleData() {
        return reportToogleData;
    }
}
