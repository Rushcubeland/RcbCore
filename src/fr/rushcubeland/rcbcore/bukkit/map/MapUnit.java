package fr.rushcubeland.rcbcore.bukkit.map;

import fr.rushcubeland.rcbcore.bukkit.network.ServerGroup;

public enum MapUnit {

    LOBBY("Lobby", "Lobby", MapGroup.LOBBY, ServerGroup.Lobby),

    WAITING_LOBBY("Waiting lobby", "Waiting_lobby", MapGroup.LOBBY, ServerGroup.DE_A_COUDRE),

    DAC("DAC", "DAC", MapGroup.DAC_POOL, ServerGroup.DE_A_COUDRE);

    private final String name;
    private final String path;
    private final MapGroup mapGroup;
    private final ServerGroup serverGroup;

    MapUnit(String name, String path, MapGroup mapGroup, ServerGroup serverGroup) {
        this.name = name;
        this.path = path;
        this.mapGroup = mapGroup;
        this.serverGroup = serverGroup;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public ServerGroup getServerGroup() {
        return serverGroup;
    }

    public MapGroup getMapGroup() {
        return mapGroup;
    }
}