package fr.rushcubeland.rcbcore.utils;

import fr.rushcubeland.rcbcore.bukkit.utils.MathUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class MathUtilsTests {

    @BeforeAll
    public static void setUp() {
        System.out.println("MathUtilsTest - starting");
    }

    @Test
    public void testIsZero() {
        float f = 0.0000000000001f;
        Assertions.assertTrue(MathUtils.isZero(f));
    }

    @AfterAll
    public static void tearDown() {
        System.out.println("MathUtilsTest - ending");
    }
}
