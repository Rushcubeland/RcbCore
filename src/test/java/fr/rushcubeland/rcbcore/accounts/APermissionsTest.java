package fr.rushcubeland.rcbcore.accounts;

import fr.rushcubeland.commons.APermissions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

public class APermissionsTest {

    @BeforeAll
    public static void setUp() {
        System.out.println("APermissionsTest - starting");
    }

    @Test
    public void testPermissions() {
        UUID uuid = UUID.fromString("39378c18-01e0-410a-95c7-4c0822a6fd21");
        APermissions permissions = new APermissions(uuid, new ArrayList<>());

        Assertions.assertEquals(0, permissions.getPermissions().size());
        permissions.addPermission("test", "test2");

        Assertions.assertEquals(2, permissions.getPermissions().size());
        permissions.removePermission("test");

        Assertions.assertEquals(1, permissions.getPermissions().size());
        Assertions.assertEquals(uuid, permissions.getUuid());
    }


    @AfterAll
    public static void tearDown() {
        System.out.println("APermissionsTest - ending");
    }
}
