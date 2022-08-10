package fr.rushcubeland.rcbcore.bungee.commands;

import fr.rushcubeland.commons.Account;
import fr.rushcubeland.commons.permissions.PermissionsUnit;
import fr.rushcubeland.commons.rank.RankUnit;
import fr.rushcubeland.rcbcore.bungee.RcbAPI;
import fr.rushcubeland.rcbcore.bungee.mod.ModModerator;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class PlayersListCommand extends Command {

    public static final String[] ALIASES = new String[]{"list", "players", "joueurs"};

    public PlayersListCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer){
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if(!player.hasPermission(PermissionsUnit.LIST_PLAYERS.getPermission()) && !player.hasPermission(PermissionsUnit.ALL.getPermission())){
                player.sendMessage(new TextComponent("§cVous n'avez pas la permission de faire ceci !"));
            }

            for(ProxiedPlayer pls : player.getServer().getInfo().getPlayers()){
                player.sendMessage(new TextComponent("§c"));
                player.sendMessage(new TextComponent("§fVoici la liste des joueurs présent sur le serveur:"));
                Account account = RcbAPI.getInstance().getAccount(pls);
                RankUnit rank = account.getRank();
                TextComponent tp = new TextComponent(" §6[§aTP§6]");
                tp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§6Se téléporter").create()));
                tp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/btp " + pls.getName()));
                if(ModModerator.isInModData(pls.getUniqueId().toString())){
                    player.sendMessage(new ComponentBuilder("§6(MOD) " + rank.getPrefix() + pls.getName()).append(tp).create());

                }
                else
                {
                    player.sendMessage(new ComponentBuilder(rank.getPrefix() + pls.getName()).append(tp).create());
                }
                player.sendMessage(new TextComponent("§c"));
            }
        }
    }


}
