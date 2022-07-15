package fr.rushcubeland.rcbcore.bukkit.network;

import java.util.ArrayList;

public enum ServerGroup {

    Lobby(),
    DE_A_COUDRE();

    private final ArrayList<ServerUnit> serversInServerGroup = new ArrayList<>();

    ServerGroup(){
    }

    public static void initServerGroup(){
        for(ServerUnit serverUnit : ServerUnit.values()){
            if(serverUnit.getServerGroup().equals(ServerGroup.Lobby)){
                ServerGroup.Lobby.getServersInGroup().add(serverUnit);
            }
            if(serverUnit.getServerGroup().equals(ServerGroup.DE_A_COUDRE)){
                ServerGroup.DE_A_COUDRE.getServersInGroup().add(serverUnit);
            }
        }
    }

    public ArrayList<ServerUnit> getServersInGroup(){
        return serversInServerGroup;
    }

}
