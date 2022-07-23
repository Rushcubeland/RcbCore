package fr.rushcubeland.rcbcore.bukkit.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.rushcubeland.commons.data.callbacks.AsyncCallBack;
import fr.rushcubeland.rcbcore.bukkit.RcbAPI;
import org.bukkit.Bukkit;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.UUID;

public class UUIDFetcher {

    public UUIDFetcher() {
        throw new UnsupportedOperationException("Utils class should not be instancied");
    }

    public static String insertDashUUID(String uuid) {
        StringBuilder sb = new StringBuilder(uuid);
        sb.insert(8, "-");
        sb = new StringBuilder(sb.toString());
        sb.insert(13, "-");
        sb = new StringBuilder(sb.toString());
        sb.insert(18, "-");
        sb = new StringBuilder(sb.toString());
        sb.insert(23, "-");

        return sb.toString();
    }

    public static String deleteDashUUID(String uuid){
        return uuid.replaceAll("-", "");
    }


    public static void getUUIDFromName(String name, final AsyncCallBack callBack){
        Bukkit.getScheduler().runTaskAsynchronously(RcbAPI.getInstance(), () -> {
            try {
                URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
                InputStreamReader reader = new InputStreamReader(url.openStream());
                JsonObject jsonObject = new JsonParser().parse(reader).getAsJsonObject();
                if(jsonObject != null){
                    Bukkit.getScheduler().runTask(RcbAPI.getInstance(), () -> callBack.onQueryComplete(insertDashUUID(jsonObject.get("id").getAsString())));
                }
            } catch (Exception e){
                e.printStackTrace();
                Bukkit.getScheduler().runTask(RcbAPI.getInstance(), () -> callBack.onQueryComplete(null));
            }
        });
    }

    public static void getNameFromUUID(UUID uuid, final AsyncCallBack callBack){
        Bukkit.getScheduler().runTaskAsynchronously(RcbAPI.getInstance(), new Runnable() {
            @Override
            public void run() {
                String uuidf = deleteDashUUID(uuid.toString());
                try {
                    URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuidf
                            + "?unsigned=false");
                    InputStreamReader reader = new InputStreamReader(url.openStream());
                    JsonObject jsonObject = new JsonParser().parse(reader).getAsJsonObject();
                    if(jsonObject != null){
                        Bukkit.getScheduler().runTask(RcbAPI.getInstance(), () -> callBack.onQueryComplete(jsonObject.get("name").getAsString()));
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    Bukkit.getScheduler().runTask(RcbAPI.getInstance(), () -> callBack.onQueryComplete(null));
                }
            }
        });
    }
}
