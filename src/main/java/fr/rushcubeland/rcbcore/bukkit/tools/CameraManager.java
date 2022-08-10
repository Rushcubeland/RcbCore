package fr.rushcubeland.rcbcore.bukkit.tools;

import fr.rushcubeland.rcbcore.bukkit.RcbAPI;
import net.minecraft.server.v1_15_R1.Entity;
import net.minecraft.server.v1_15_R1.PacketPlayOutCamera;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CameraManager {

    static HashMap<Player, Player> playersInSpectateToP = new HashMap<>();
    static HashMap<Player, Entity> playersInSpectateToE = new HashMap<>();

    public static void spectate(Player Pfrom, Player Pto) {
        if(Pfrom == null || Pto == null){
            return;
        }
        if(!playersInSpectateToP.containsKey(Pfrom)) {
            playersInSpectateToP.put(Pfrom,Pto);
            Pfrom.setGameMode(GameMode.SPECTATOR);
            for(Player ps : Bukkit.getOnlinePlayers()) {
                ps.hidePlayer(RcbAPI.getInstance(), Pfrom);

            }
            PacketPlayOutCamera packet = new PacketPlayOutCamera((Entity) Pto);
            Reflection.sendPacket(Pfrom, packet);
        }
        else
        {
            playersInSpectateToP.remove(Pfrom);
            for(Player ps : Bukkit.getOnlinePlayers()) {
                ps.showPlayer(RcbAPI.getInstance(), Pfrom);
            }
            PacketPlayOutCamera packet2 = new PacketPlayOutCamera((Entity) Pto);
            Reflection.sendPacket(Pfrom, packet2);
        }

    }

    public static void spectate(Player Pfrom, Entity Eto) {
        if(Pfrom != null) {
            if(!playersInSpectateToE.containsKey(Pfrom)) {
                playersInSpectateToE.put(Pfrom, Eto);
                Pfrom.setGameMode(GameMode.SPECTATOR);
                for(Player ps : Bukkit.getOnlinePlayers()) {
                    ps.hidePlayer(RcbAPI.getInstance(), Pfrom);

                }
                PacketPlayOutCamera packet = new PacketPlayOutCamera(Eto);
                Reflection.sendPacket(Pfrom, packet);
            }
            else
            {
                playersInSpectateToE.remove(Pfrom);
                for(Player ps : Bukkit.getOnlinePlayers()) {
                    ps.showPlayer(RcbAPI.getInstance(), Pfrom);
                }
                PacketPlayOutCamera packet2 = new PacketPlayOutCamera(Eto);
                Reflection.sendPacket(Pfrom, packet2);
            }
        }

    }
}
