package fr.rushcubeland.rcbcore.bukkit.tools;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import fr.rushcubeland.rcbcore.bungee.utils.UUIDFetcher;
import fr.rushcubeland.rcbcore.bukkit.RcbAPI;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class NPC {

    private static final List<NPC> npcs = new ArrayList<>();

    private String name;
    private World world;
    private Location location;
    private EntityPlayer npc;
    private GameProfile gameProfile;
    private String[] property;

    private final HashMap<EnumItemSlot, ItemStack> equiments = new HashMap<>();

    public NPC(String name, World world) {
        this.name = name;
        this.world = world;
        this.gameProfile = new GameProfile(UUID.randomUUID(), this.name);
    }

    public NPC(String name, World world, Location location) {
        this.name = name;
        this.world = world;
        this.location = location;
        this.gameProfile = new GameProfile(UUID.randomUUID(), this.name);
    }

    public void create(){
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer worldServer = ((CraftWorld) this.world).getHandle();
        applySkin();
        this.npc = new EntityPlayer(server, worldServer, this.gameProfile, new PlayerInteractManager(worldServer));
        this.npc.setLocation(this.location.getX(), this.location.getY(), this.location.getZ(),
                this.location.getYaw(), this.location.getPitch());
        this.npc.setInvulnerable(true);
        npcs.add(this);
    }

    public void spawn(Player player){
        Reflection.sendPacket(player, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, this.npc));
        Reflection.sendPacket(player, new PacketPlayOutNamedEntitySpawn(this.npc));
        Reflection.sendPacket(player, new PacketPlayOutEntityHeadRotation(this.npc, (byte) (this.npc.yaw * 256 / 360)));
        for(Map.Entry data : this.equiments.entrySet()) {
            Reflection.sendPacket(player, new PacketPlayOutEntityEquipment(this.npc.getId(), (EnumItemSlot) data.getKey(), (ItemStack) data.getValue()));
        }
        Bukkit.getScheduler().runTaskLater(RcbAPI.getInstance(), () -> Reflection.sendPacket(player, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc)), 40L);
    }

    public void spawn(){
        Reflection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, this.npc));
        Reflection.sendPacket(new PacketPlayOutNamedEntitySpawn(this.npc));
        Reflection.sendPacket(new PacketPlayOutEntityHeadRotation(this.npc, (byte) (this.npc.yaw * 256 / 360)));
        for(Map.Entry data : this.equiments.entrySet()) {
            Reflection.sendPacket(new PacketPlayOutEntityEquipment(this.npc.getId(), (EnumItemSlot) data.getKey(), (ItemStack) data.getValue()));
        }
        Bukkit.getScheduler().runTaskLater(RcbAPI.getInstance(), () -> Reflection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc)), 40L);
    }

    public void setEquipment(EnumItemSlot slot, ItemStack item){
        if(equiments.containsKey(slot)){
            this.equiments.replace(slot, item);
        }
        this.equiments.put(slot, item);
    }

    public void removeEquipment(EnumItemSlot slot){
        if(equiments.containsKey(slot)){
            this.equiments.remove(slot);
        }
    }

    public void removeEquipment(EnumItemSlot slot, ItemStack item){
        if(equiments.containsKey(slot) && this.equiments.get(slot).equals(item)){
            this.equiments.remove(slot, item);
        }
    }

    public void setLook(float yaw){
        Reflection.sendPacket(new PacketPlayOutEntity.PacketPlayOutEntityLook(this.npc.getId(), (byte) (yaw * 256 / 360), (byte) 0, true));
    }

    public void setHeadRotation(float yaw){
        Reflection.sendPacket(new PacketPlayOutEntityHeadRotation(this.npc, (byte) (yaw * 256 / 360)));
    }

    private void applySkin(){
        this.gameProfile.getProperties().put("textures", new Property("textures", property[0], property[1]));
    }

    public void setSkin(Player player){
        this.property = getSkin(player);
    }

    public void setSkin(String playerName){
        this.property = getSkin(playerName);
    }

    public void setSkin(String texture, String signature){
        this.property = new String[]{texture, signature};
    }

    public void destroy(Player player){
        Reflection.sendPacket(player, new PacketPlayOutEntityDestroy(this.npc.getId()));
    }

    public void destroy(){
        Reflection.sendPacket(new PacketPlayOutEntityDestroy(this.npc.getId()));
    }

    public void delete(){
        destroy();
        npcs.remove(this);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public EntityPlayer getNpc() {
        return npc;
    }

    public GameProfile getGameProfile() {
        return gameProfile;
    }

    public HashMap<EnumItemSlot, ItemStack> getEquiments() {
        return equiments;
    }

    public static void spawnAll(Player player){
        for(NPC NPCs : NPC.getNPCs()){
            NPCs.spawn(player);
        }
    }

    public static void spawnAll(){
        for(NPC NPCs : NPC.getNPCs()){
            NPCs.spawn();
        }
    }

    private static String[] getSkin(String name){

        try {

            String uuid = UUIDFetcher.getUUIDFromName(name);
            if(uuid != null){
                uuid = UUIDFetcher.deleteDashUUID(uuid);
                URL url2 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
                InputStreamReader reader2 = new InputStreamReader(url2.openStream());
                JsonObject property = new JsonParser().parse(reader2).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
                String texture = property.get("value").getAsString();
                String signature = property.get("signature").getAsString();
                return new String[]{texture, signature};
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    private static String[] getSkin(Player player){

        EntityPlayer p = ((CraftPlayer) player).getHandle();
        GameProfile gameProfile = p.getProfile();
        Property property = gameProfile.getProperties().get("textures").iterator().next();
        String texture = property.getValue();
        String signature = property.getSignature();

        return new String[] {texture, signature};
    }

    public static List<NPC> getNPCs(){
        return npcs;
    }
}
