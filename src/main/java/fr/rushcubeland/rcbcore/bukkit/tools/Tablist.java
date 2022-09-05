package fr.rushcubeland.rcbcore.bukkit.tools;

import fr.rushcubeland.commons.Account;
import fr.rushcubeland.commons.rank.RankUnit;
import fr.rushcubeland.rcbcore.bukkit.RcbAPI;
import net.minecraft.server.v1_15_R1.ChatComponentText;
import net.minecraft.server.v1_15_R1.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Tablist {

    private Scoreboard scoreboard;
    private final ArrayList<Team> teams = new ArrayList<>();

    public void sendTabList(Player player){

        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        Object header = new ChatComponentText("§6§nRushcubeland\n§f\n§aFais le plein d'émotions et de joie !\n§7===================");
        Object footer = new ChatComponentText("§7===================\n§aPour obtenir un grade achète en un sur:\n §6shop.rushcubeland.fr\n§ewww.rushcubeland.fr");

        try {

            Field a = packet.getClass().getDeclaredField("header");
            a.setAccessible(true);
            Field b = packet.getClass().getDeclaredField("footer");
            b.setAccessible(true);
            a.set(packet, header);
            b.set(packet, footer);

        } catch (NoSuchFieldException | SecurityException | IllegalAccessException e){
            e.printStackTrace();
        }
        Reflection.sendPacket(player, packet);
    }

    public void setTabListPlayer(Player player){

        RcbAPI.getInstance().getAccount(player, result -> {
            Team value1;
            Account account = (Account) result;
            for(Team team : teams){
                if(team.getName().equals(account.getRank().getPower().toString())){
                    value1 = team;
                    player.setScoreboard(scoreboard);
                    value1.addEntry(player.getName());
                    break;
                }
            }
        });
    }

    public void resetTabListPlayer(Player player){
        Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
        sb.registerNewObjective("scoreboard", "dummy", "§6Rushcubeland");
        player.setScoreboard(sb);
    }

    public void initTabListTeam(){
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        scoreboard.registerNewObjective("scoreboard", "dummy", "§6Rushcubeland");
        for(RankUnit rank : RankUnit.values()){
            Team team = scoreboard.registerNewTeam(rank.getPower().toString());
            teams.add(team);
            team.setPrefix(rank.getPrefix());
            team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        }
        Team teamNPC = scoreboard.registerNewTeam(Integer.toString(9999));
        teamNPC.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        for(NPC npc : NPC.getNPCs()){
            teamNPC.addEntry(npc.getNpc().getName());
        }
    }
}
