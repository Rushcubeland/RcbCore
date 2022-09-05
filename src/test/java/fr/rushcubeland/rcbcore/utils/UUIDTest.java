package fr.rushcubeland.rcbcore.utils;

import fr.rushcubeland.rcbcore.bungee.utils.UUIDFetcher;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class UUIDTest {


    @BeforeAll
    public static void setUp() {
        System.out.println("UUIDTest - starting");
    }


    @Test
    public void TestDash(){
        String uuid = "39378c1801e0410a95c74c0822a6fd21";
        String uuidDash = UUIDFetcher.insertDashUUID(uuid);
        Assertions.assertEquals("39378c18-01e0-410a-95c7-4c0822a6fd21", uuidDash);

        String uuidDash2 = "39378c18-01e0-410a-95c7-4c0822a6fd21";
        String uuidNoDash = UUIDFetcher.deleteDashUUID(uuidDash2);
        Assertions.assertEquals("39378c1801e0410a95c74c0822a6fd21", uuidNoDash);

    }

    @AfterAll
    public static void tearDown() {
        System.out.println("UUIDTest - ending");
    }


}
