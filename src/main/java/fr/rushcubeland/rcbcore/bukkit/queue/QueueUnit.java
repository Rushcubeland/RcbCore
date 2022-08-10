package fr.rushcubeland.rcbcore.bukkit.queue;

import fr.rushcubeland.rcbcore.bukkit.network.ServerGroup;

public enum QueueUnit {

    DE_A_COUDRE("§bDé à coudre", ServerGroup.DE_A_COUDRE);

    private final String name;
    private final ServerGroup serverGroup;

    QueueUnit(String name, ServerGroup serverGroup) {
        this.name = name;
        this.serverGroup = serverGroup;
    }

    public String getName() {
        return name;
    }

    public ServerGroup getServerGroup() {
        return serverGroup;
    }
}
