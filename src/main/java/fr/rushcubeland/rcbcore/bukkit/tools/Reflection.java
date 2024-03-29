package fr.rushcubeland.rcbcore.bukkit.tools;

import net.minecraft.server.v1_15_R1.Packet;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class Reflection {

    public static void setValue(Object obj,String name,Object value){
        try{
            Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(obj, value);
        }catch(Exception e){}
    }

    public static Object getValue(Object obj,String name){
        try{
            Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(obj);
        }catch(Exception e){}
        return null;
    }

    public static void sendPacket(Player player, Packet<?> packet){
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }

    public static void sendPacket(Packet<?> packet){
        for(Player player : Bukkit.getOnlinePlayers()){
            sendPacket(player, packet);
        }
    }
}
