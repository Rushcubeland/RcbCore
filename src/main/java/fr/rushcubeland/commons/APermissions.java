package fr.rushcubeland.commons;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class APermissions implements Cloneable {

    private UUID uuid;
    private List<String> permissions;

    public APermissions(){
    }

    public APermissions(UUID uuid, List<String> permissions) {
        this.uuid = uuid;
        this.permissions = permissions;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void addPermission(String... permissions){
        this.permissions.addAll(Arrays.asList(permissions));
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void removePermission(String... permissions){
        this.permissions.removeAll(Arrays.asList(permissions));
    }

    public APermissions clone(){
        try {

            return (APermissions) super.clone();

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
