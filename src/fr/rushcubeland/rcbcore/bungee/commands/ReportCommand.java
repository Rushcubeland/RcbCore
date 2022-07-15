package fr.rushcubeland.rcbcore.bungee.commands;

import fr.rushcubeland.commons.permissions.PermissionsUnit;
import fr.rushcubeland.rcbcore.bungee.RcbAPI;
import fr.rushcubeland.rcbcore.bungee.report.Report;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;
import java.util.List;

public class ReportCommand extends Command {

    public static final List<String> ALIASES = Arrays.asList("report", "signalement");

    public ReportCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer){
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if(player.hasPermission(PermissionsUnit.REPORT.getPermission()) || player.hasPermission(PermissionsUnit.ALL.getPermission())){
                if(args.length == 1){
                    if(args[0].equalsIgnoreCase("on")){
                        Report.getReportToogleData().put(player, true);
                        player.sendMessage(new TextComponent("§d[Report] §aactivé"));
                        return;
                    }
                    if(args[0].equalsIgnoreCase("off")){
                        Report.getReportToogleData().put(player, false);
                        player.sendMessage(new TextComponent("§d[Report] §cdésactivé"));
                        return;
                    }
                }
                if(args.length == 0){
                    if(Report.getReportToogleData().containsKey(player)){
                        if(!Report.getReportToogleData().get(player)){
                            Report.getReportToogleData().put(player, true);
                            player.sendMessage(new TextComponent("§d[Report] §aactivé"));
                        }
                        else
                        {
                            Report.getReportToogleData().put(player, false);
                            player.sendMessage(new TextComponent("§d[Report] §cdésactivé"));
                        }
                    }
                    else
                    {
                        Report.getReportToogleData().put(player, false);
                        player.sendMessage(new TextComponent("§d[Report] §cdésactivé"));
                    }
                    return;
                }
            }
            if(!RcbAPI.getInstance().getMuteManager().isMuted(player.getUniqueId())){
                if(args.length >= 1){
                    if(args.length >= 2){
                        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
                        if(target != null){
                            if(player == target){
                                player.sendMessage(new TextComponent("§cVous ne pouvez pas vous signaler !"));
                                return;
                            }
                            StringBuilder raison = new StringBuilder();
                            for(int i=1; i < args.length; i++){
                                raison.append(args[i]).append(" ");
                            }
                            for(ProxiedPlayer mods : ProxyServer.getInstance().getPlayers()){
                                if(mods.hasPermission(PermissionsUnit.REPORT.getPermission()) || player.hasPermission(PermissionsUnit.ALL.getPermission())){
                                    Report.report(player, target, raison.toString());
                                }
                            }
                            player.sendMessage(new TextComponent("§6[AP] §aVous avez signalé §c" + target.getName() + " §apour: §e" + raison));
                        }
                        else
                        {
                            player.sendMessage(new TextComponent("§cCe joueur n'est pas connecté !"));
                        }
                    }
                    else
                    {
                        player.sendMessage(new TextComponent("§cVeuillez spécifier une raison !"));
                        player.sendMessage(new TextComponent("§c/report <joueur> <raison>"));
                    }
                }
                else
                {
                    player.sendMessage(new TextComponent("§cVeuillez spécifier un joueur !"));
                    player.sendMessage(new TextComponent("§c/report <joueur> <raison>"));
                }
            }
            else
            {
                player.sendMessage(new TextComponent("§cVous avez été mute !"));
            }
        }
    }
}
