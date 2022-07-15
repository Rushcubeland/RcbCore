package fr.rushcubeland.rcbcore.bungee.sanctions.mute;

import fr.rushcubeland.rcbcore.bungee.RcbAPI;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class CheckMuteStateTask implements Runnable {

    @Override
    public void run() {
        for(ProxiedPlayer pls : ProxyServer.getInstance().getPlayers()){
            if(RcbAPI.getInstance().getMuteManager().isMuted(pls.getUniqueId())){
                RcbAPI.getInstance().getMuteManager().checkDuration(pls.getUniqueId());
            }
        }
    }
}