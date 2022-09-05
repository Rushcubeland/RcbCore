package fr.rushcubeland.commons;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AFriends implements Cloneable {

    private UUID uuid;
    private List<UUID> friends;
    private int maxFriends;

    public AFriends(UUID uuid, int maxFriends, ArrayList<UUID> friends) {
        this.uuid = uuid;
        this.maxFriends = maxFriends;
        this.friends = friends;
    }

    public AFriends(){}

    public UUID getUuid() {
        return uuid;
    }

    public List<UUID> getFriends() {
        return friends;
    }

    public int getMaxFriends() {
        return maxFriends;
    }

    public void setMaxFriends(int maxFriends) {
        this.maxFriends = maxFriends;
    }

    public void setFriends(List<UUID> friends) {
        this.friends = friends;
    }

    public void addFriend(UUID uuid){
        if(hasReachedMaxFriends()){
            throw new UnsupportedOperationException("You have reached the maximum number of friends");
        }
        if(!friends.contains(uuid)){
            friends.add(uuid);
        }
    }

    public void removeFriend(UUID uuid){
        friends.remove(uuid);
    }

    public boolean areFriendWith(UUID uuid){
        return friends.contains(uuid);
    }

    public boolean hasReachedMaxFriends(){
        return friends.size() >= this.maxFriends;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public AFriends clone(){
        try {

            return (AFriends) super.clone();

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

}
