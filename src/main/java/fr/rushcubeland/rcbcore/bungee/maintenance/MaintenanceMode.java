package fr.rushcubeland.rcbcore.bungee.maintenance;

public enum MaintenanceMode {

    ON(true, 1),
    OFF(false, 0);

    private final Boolean state;
    private final int value;

    MaintenanceMode(Boolean state, int value) {
        this.state = state;
        this.value = value;
    }

    public Boolean isState() {
        return state;
    }

    public Boolean getState() {
        return state;
    }

    public int getValue() {
        return value;
    }
}
