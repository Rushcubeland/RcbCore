package fr.rushcubeland.rcbcore.bukkit.commands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;

public class ReportMsgCommand implements CommandExecutor {

    public static HashMap<String, ArrayList<String>> msgs = new HashMap<>();
    public static HashMap<Player, HashMap<String, ArrayList<String>>> dataPlayers = new HashMap<>();

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd, @Nonnull String label, @Nonnull String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(label.equalsIgnoreCase("reportmsg") && args.length >= 2){
                String targetName = args[0];
                if(!targetName.equals(player.getName())){
                    StringBuilder builder = new StringBuilder();
                    for(int i=1; i < args.length; i++){
                        builder.append(args[i]).append(" ");
                    }
                    String message = builder.toString();
                    message = message.substring(0, message.length() - 1);
                    if(msgs.containsKey(targetName)){
                        ArrayList<String> msgsCache = msgs.get(targetName);
                        for(String messages : msgsCache){
                            if(messages.equals(message)){
                                if(dataPlayers.containsKey(player)){
                                    if(dataPlayers.get(player).containsKey(targetName)){
                                        if(dataPlayers.get(player).get(targetName).contains(message)){
                                            return true;
                                        }
                                        else
                                        {
                                            HashMap<String, ArrayList<String>> hash = dataPlayers.get(player);
                                            ArrayList<String> array = hash.get(targetName);
                                            array.add(message);
                                            hash.put(targetName, array);
                                            dataPlayers.replace(player, hash);
                                        }
                                    }
                                    else
                                    {
                                        HashMap<String, ArrayList<String>> hash = dataPlayers.get(player);
                                        ArrayList<String> newarray = new ArrayList<>();
                                        newarray.add(message);
                                        hash.put(targetName, newarray);
                                        dataPlayers.replace(player, hash);
                                    }
                                }
                                else
                                {
                                    HashMap<String, ArrayList<String>> newhash = new HashMap<>();
                                    ArrayList<String> newarray = new ArrayList<>();
                                    newarray.add(message);
                                    newhash.put(targetName, newarray);
                                    dataPlayers.put(player, newhash);
                                }
                                sendReportConfirmation(player, targetName, message);
                            }
                        }
                    }
                }
                else
                {
                    player.sendMessage("§cVous ne pouvez pas vous signaler !");
                }
            }
        }
        return false;
    }

    private void sendReportConfirmation(Player player, String targetName, String message){
        player.sendMessage("§e---------§d[Report]§e---------");
        player.sendMessage("§fJoueur a signaler: §6" + targetName);
        player.sendMessage("§fMessage a signaler: §b" + message.replace(" ", " §b"));
        player.sendMessage(" ");
        TextComponent accept = new TextComponent("§a[Signaler ce message]");
        accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§aSignaler ce message")));
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reportconfirm " + targetName + " " + message));
        player.spigot().sendMessage(new ComponentBuilder(new TextComponent("    ")).append(accept).appendLegacy("   ").create());
        player.sendMessage("§e-------------------------");
    }
                    
}
