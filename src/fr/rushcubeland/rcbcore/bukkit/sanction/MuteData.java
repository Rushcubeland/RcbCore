package fr.rushcubeland.rcbcore.bukkit.sanction;

import java.util.ArrayList;

public class MuteData {

    private static final ArrayList<String> playerMute = new ArrayList<>();

    public static void removeMute(String targetUUID){
        playerMute.remove(targetUUID);
    }

    public static void addMute(String targetUUID){
        if(!playerMute.contains(targetUUID)){
            playerMute.add(targetUUID);
        }
    }

    public static boolean isInMuteData(String targetUUID){
        return playerMute.contains(targetUUID);
    }

    public static ArrayList<String> getPlayerMute() {
        return playerMute;
    }
}
