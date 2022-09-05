package fr.rushcubeland.rcbcore.accounts;

import fr.rushcubeland.commons.AFriends;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

public class AFriendsTest {

    @BeforeAll
    public static void setUp() {
        System.out.println("AFriendsTest - starting");
    }

    @Test
    public void testFriendship(){
        AFriends aFriends = new AFriends(UUID.fromString("39378c18-01e0-410a-95c7-4c0822a6fd21"), 10, new ArrayList<>());

        AFriends aFriends2 = new AFriends(UUID.randomUUID(), 10, new ArrayList<>());

        aFriends.addFriend(aFriends2.getUuid());
        aFriends2.addFriend(aFriends.getUuid());

        Assertions.assertTrue(aFriends.areFriendWith(aFriends2.getUuid()));

    }

    @Test
    public void testMaxFriends(){
        AFriends aFriends = new AFriends(UUID.fromString("39378c18-01e0-410a-95c7-4c0822a6fd21"), 10, new ArrayList<>());

        for(int i = 0; i < 10; i++){
            aFriends.addFriend(UUID.randomUUID());
        }

        UnsupportedOperationException thrown = Assertions.assertThrows(UnsupportedOperationException.class, () -> aFriends.addFriend(UUID.randomUUID()));

        Assertions.assertEquals("You have reached the maximum number of friends", thrown.getMessage());

    }

    @AfterAll
    public static void tearDown() {
        System.out.println("AFriendsTest - ending");
    }
}
