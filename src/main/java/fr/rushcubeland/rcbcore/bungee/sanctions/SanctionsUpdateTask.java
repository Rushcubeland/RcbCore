package fr.rushcubeland.rcbcore.bungee.sanctions;

import fr.rushcubeland.rcbcore.bungee.RcbAPI;
import net.md_5.bungee.api.ProxyServer;

import java.util.concurrent.TimeUnit;

public class SanctionsUpdateTask {

    private static void async(){
        ProxyServer.getInstance().getScheduler().runAsync(RcbAPI.getInstance(), () -> RcbAPI.getInstance().getBanManager().update());
        ProxyServer.getInstance().getScheduler().runAsync(RcbAPI.getInstance(), () -> RcbAPI.getInstance().getMuteManager().update());
    }

    public static void update(){
        ProxyServer.getInstance().getScheduler().schedule(RcbAPI.getInstance(), SanctionsUpdateTask::async,1L, 5L, TimeUnit.MINUTES);
    }
}
