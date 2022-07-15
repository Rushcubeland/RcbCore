package fr.rushcubeland.rcbcore.bungee.listeners;

import fr.rushcubeland.rcbcore.bungee.maintenance.Maintenance;
import fr.rushcubeland.rcbcore.bungee.maintenance.MaintenanceMode;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ProxyPing implements Listener {

    private static final String COUNT_HOVER_MESSAGE = "§cMalheuresement, une maintenance est actuellement\n§cen cours sur notre infrastructure.\n\n§cVeuillez réessayer plus tard.";
    private static final String MOTD = "§6§lRushcubeland §a1.15+ §f- §6Serveur de jeu §ccompétitif §c----------- §4[Maintenance en cours] §c-----------";
    private static final String VERSION_INFO = "§cMaintenance...";

    @EventHandler(priority = EventPriority.HIGH)
    public void onProxyPing(ProxyPingEvent e) {

        final ServerPing ping = e.getResponse();

        //ping.getPlayers().setMax(ProxyServer.getInstance().getConfig().getPlayerLimit());

        if(Maintenance.INSTANCE.getMaintenanceMode().equals(MaintenanceMode.ON)){
            ServerPing.Players players = ping.getPlayers();
            if (players == null) {
                ping.setPlayers(players = new ServerPing.Players(0, 0,  null));
            }
            ping.setVersion(new ServerPing.Protocol(VERSION_INFO, 1));
            ping.setDescriptionComponent(new TextComponent(MOTD));

            final String[] split = COUNT_HOVER_MESSAGE.split("\n");
            final ServerPing.PlayerInfo[] samplePlayers = new ServerPing.PlayerInfo[split.length];
            for (int i = 0; i < split.length; i++) {
                samplePlayers[i] = new ServerPing.PlayerInfo(split[i], "");
            }
            players.setSample(samplePlayers);
        }
    }
}
