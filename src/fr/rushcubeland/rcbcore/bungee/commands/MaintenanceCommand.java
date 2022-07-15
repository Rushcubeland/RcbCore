package fr.rushcubeland.rcbcore.bungee.commands;

import fr.rushcubeland.commons.permissions.PermissionsUnit;
import fr.rushcubeland.commons.utils.UUIDFetcher;
import fr.rushcubeland.rcbcore.bungee.maintenance.Maintenance;
import fr.rushcubeland.rcbcore.bungee.maintenance.MaintenanceMode;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.command.ConsoleCommandSender;

import java.util.UUID;

public class MaintenanceCommand extends Command {

    private static final String cmd = "maintenance";

    public MaintenanceCommand() {
        super(cmd);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer){
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if(!player.hasPermission(PermissionsUnit.MAINTENANCE.getPermission()) && !player.hasPermission(PermissionsUnit.ALL.getPermission())){
                player.sendMessage(new TextComponent("§cVous n'avez pas la permission d'utiliser cette commande !"));
                return;
            }
            if(args.length == 0){
                if(Maintenance.INSTANCE.getMaintenanceMode().equals(MaintenanceMode.ON)){
                    Maintenance.INSTANCE.setMaintenanceMode(MaintenanceMode.OFF);
                    player.sendMessage(new TextComponent("§6La maintenance a été §cdésactivée !"));
                }
                else
                {
                    Maintenance.INSTANCE.setMaintenanceMode(MaintenanceMode.ON);
                    player.sendMessage(new TextComponent("§6La maintenance a été §aactivée !"));
                }
                return;
            }
            if(args.length == 1){
                if(args[0].equalsIgnoreCase("on")){
                    if(!Maintenance.INSTANCE.getMaintenanceMode().equals(MaintenanceMode.ON)){
                        Maintenance.INSTANCE.setMaintenanceMode(MaintenanceMode.ON);
                        player.sendMessage(new TextComponent("§6La maintenance a été §aactivée !"));
                    }
                    else
                    {
                        player.sendMessage(new TextComponent("§cLa maintenance est déjà activée !"));
                        return;
                    }
                }
                else if(args[0].equalsIgnoreCase("off")){
                    if(!Maintenance.INSTANCE.getMaintenanceMode().equals(MaintenanceMode.OFF)){
                        Maintenance.INSTANCE.setMaintenanceMode(MaintenanceMode.OFF);
                        player.sendMessage(new TextComponent("§6La maintenance a été §cdésactivée !"));
                    }
                    else
                    {
                        player.sendMessage(new TextComponent("§cLa maintenance est déjà désactivée !"));
                        return;
                    }
                }
                else if(args[0].equalsIgnoreCase("add")){
                    player.sendMessage(new TextComponent("§cIl manque un argument !"));
                }
                else if(args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("rm")){
                    player.sendMessage(new TextComponent("§cIl manque un argument !"));
                }
            }
            if(args.length == 2){
                String targetName = args[1];
                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(targetName);
                if(target != null){
                    UUID uuid = target.getUniqueId();
                    if(args[0].equalsIgnoreCase("add")){
                        if(!Maintenance.INSTANCE.isIn(uuid)){
                            Maintenance.INSTANCE.addPlayer(uuid);
                            player.sendMessage(new TextComponent("§aLe joueur §e" + target.getName() + " §aa été ajouté à la liste"));
                        }
                        else
                        {
                            player.sendMessage(new TextComponent("§cCe joueur est déjà dans la liste !"));
                        }
                    }
                    else if(args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("rm")){
                        if(Maintenance.INSTANCE.isIn(uuid)){
                            Maintenance.INSTANCE.removePlayer(uuid);
                            player.sendMessage(new TextComponent("§cLe joueur §e" + target.getName() + " §ca été retiré de la liste !"));
                        }
                        else
                        {
                            player.sendMessage(new TextComponent("§cCe joueur n'est pas dans la liste !"));
                        }
                    }
                }
                else
                {
                    String suuid = UUIDFetcher.getUUIDFromName(targetName);
                    if(suuid != null){
                        UUID uuid = UUID.fromString(suuid);
                        if(args[0].equalsIgnoreCase("add")){
                            if(!Maintenance.INSTANCE.isIn(uuid)){
                                Maintenance.INSTANCE.addPlayer(uuid);
                                player.sendMessage(new TextComponent("§aLe joueur §e" + targetName + " §aa été ajouté à la liste"));
                            }
                            else
                            {
                                player.sendMessage(new TextComponent("§cCe joueur est déjà dans la liste !"));
                            }
                        }
                        else if(args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("rm")){
                            if(Maintenance.INSTANCE.isIn(uuid)){
                                Maintenance.INSTANCE.removePlayer(uuid);
                                player.sendMessage(new TextComponent("§cLe joueur §e" + targetName + " §ca été retiré de la liste !"));
                            }
                            else
                            {
                                player.sendMessage(new TextComponent("§cCe joueur n'est pas dans la liste !"));
                            }
                        }
                    }
                    else
                    {
                        player.sendMessage(new TextComponent("§cJoueur inconnu !"));
                    }
                }
            }
        }
        if(sender instanceof ConsoleCommandSender){
            ConsoleCommandSender console = (ConsoleCommandSender) sender;
            if(args.length == 0){
                if(Maintenance.INSTANCE.getMaintenanceMode().equals(MaintenanceMode.ON)){
                    Maintenance.INSTANCE.setMaintenanceMode(MaintenanceMode.OFF);
                    console.sendMessage(new TextComponent("§6La maintenance a été §cdésactivée !"));
                }
                else
                {
                    Maintenance.INSTANCE.setMaintenanceMode(MaintenanceMode.ON);
                    console.sendMessage(new TextComponent("§6La maintenance a été §aactivée !"));
                }
            }
            if(args.length == 1){
                if(args[0].equalsIgnoreCase("on")){
                    if(!Maintenance.INSTANCE.getMaintenanceMode().equals(MaintenanceMode.ON)){
                        Maintenance.INSTANCE.setMaintenanceMode(MaintenanceMode.ON);
                        console.sendMessage(new TextComponent("§6La maintenance a été §aactivée !"));
                    }
                    else
                    {
                        console.sendMessage(new TextComponent("§cLa maintenance est déjà activée !"));
                    }
                }
                else if(args[0].equalsIgnoreCase("off")){
                    if(!Maintenance.INSTANCE.getMaintenanceMode().equals(MaintenanceMode.OFF)){
                        Maintenance.INSTANCE.setMaintenanceMode(MaintenanceMode.OFF);
                        console.sendMessage(new TextComponent("§6La maintenance a été §cdésactivée !"));
                    }
                    else
                    {
                        console.sendMessage(new TextComponent("§cLa maintenance est déjà désactivée !"));
                    }
                }
                else if(args[0].equalsIgnoreCase("add")){
                    console.sendMessage(new TextComponent("§cIl manque un argument !"));
                }
                else if(args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("rem")){
                    console.sendMessage(new TextComponent("§cIl manque un argument !"));
                }
            }
            if(args.length == 2){
                String targetName = args[1];
                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(targetName);
                if(target != null){
                    UUID uuid = target.getUniqueId();
                    if(args[0].equalsIgnoreCase("add")){
                        if(!Maintenance.INSTANCE.isIn(uuid)){
                            Maintenance.INSTANCE.addPlayer(uuid);
                            console.sendMessage(new TextComponent("§aLe joueur §e" + target.getName() + " §aa été ajouté à la liste"));
                        }
                        else
                        {
                            console.sendMessage(new TextComponent("§cCe joueur est déjà dans la liste !"));
                        }
                    }
                    else if(args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("rm")){
                        if(Maintenance.INSTANCE.isIn(uuid)){
                            Maintenance.INSTANCE.removePlayer(uuid);
                            console.sendMessage(new TextComponent("§cLe joueur §e" + target.getName() + " §ca été retiré de la liste !"));
                        }
                        else
                        {
                            console.sendMessage(new TextComponent("§cCe joueur n'est pas dans la liste !"));
                        }
                    }
                }
                else
                {
                    String suuid = UUIDFetcher.getUUIDFromName(targetName);
                    if(suuid != null){
                        UUID uuid = UUID.fromString(suuid);
                        if(args[0].equalsIgnoreCase("add")){
                            if(!Maintenance.INSTANCE.isIn(uuid)){
                                Maintenance.INSTANCE.addPlayer(uuid);
                                console.sendMessage(new TextComponent("§aLe joueur §e" + targetName + " §aa été ajouté à la liste"));
                            }
                            else
                            {
                                console.sendMessage(new TextComponent("§cCe joueur est déjà dans la liste !"));
                            }
                        }
                        else if(args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("rm")){
                            if(Maintenance.INSTANCE.isIn(uuid)){
                                Maintenance.INSTANCE.removePlayer(uuid);
                                console.sendMessage(new TextComponent("§cLe joueur §e" + targetName + " §ca été retiré de la liste !"));
                            }
                            else
                            {
                                console.sendMessage(new TextComponent("§cCe joueur n'est pas dans la liste !"));
                            }
                        }
                    }
                    else
                    {
                        console.sendMessage(new TextComponent("§cJoueur inconnu !"));
                    }
                }
            }
        }
    }

    public static String getCmd(){
        return cmd;
    }
}
