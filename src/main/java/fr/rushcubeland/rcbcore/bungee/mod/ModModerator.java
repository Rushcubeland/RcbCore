package fr.rushcubeland.rcbcore.bungee.mod;

import java.util.ArrayList;

public class ModModerator {

    private static final ArrayList<String> playersMod = new ArrayList<>();

    public static void removeMod(String targetUUID){
        playersMod.remove(targetUUID);
    }

    public static void addMod(String targetUUID){
        if(!playersMod.contains(targetUUID)){
            playersMod.add(targetUUID);
        }
    }

    public static boolean isInModData(String targetUUID){
        return playersMod.contains(targetUUID);
    }

    public static ArrayList<String> getPlayersMod() {
        return playersMod;
    }
}