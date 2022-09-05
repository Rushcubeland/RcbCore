package fr.rushcubeland.rcbcore.accounts;

import fr.rushcubeland.commons.Account;
import fr.rushcubeland.commons.rank.RankUnit;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class AccountTest {

    @BeforeAll
    public static void setUp() {
        System.out.println("AccountTest - starting");
    }

    @Test
    public void TestAccount(){
        Account account = new Account();
        account.setUuid(UUID.fromString("39378c18-01e0-410a-95c7-4c0822a6fd21"));
        Account account2 = account.clone();
        Assertions.assertEquals(account.getUuid(), account2.getUuid());
    }

    @Test
    public void TestAccountCoins(){
        Account account = new Account();
        account.setCoins(1000);
        Assertions.assertEquals(1000, account.getCoins());

        account.addCoins(1000);
        Assertions.assertEquals(2000, account.getCoins());

        account.removeCoins(1000);
        Assertions.assertEquals(1000, account.getCoins());

        account.removeCoins(1000);
        Assertions.assertEquals(0, account.getCoins());

        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> account.removeCoins(1000));

        IllegalArgumentException thrown2 = Assertions.assertThrows(IllegalArgumentException.class, () -> account.addCoins(-1000));

        Assertions.assertEquals("Coins can't be negative", thrown.getMessage());
        Assertions.assertEquals("Coins can't be negative", thrown2.getMessage());

    }

    @Test
    public void  TestRank(){
        Account account = new Account();
        account.setRank(RankUnit.ASSISTANT);
        account.removeSpecialRank();
        Assertions.assertNull(account.getSpecialRank());
        Assertions.assertEquals(-1, account.getSpecialRank_end());

        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> account.setRank(null));

        Assertions.assertEquals("Rank can't be null", thrown.getMessage());

        account.setRank(RankUnit.VIP);
        account.setRank(RankUnit.MODERATRICE);

        Assertions.assertEquals(RankUnit.VIP, account.getSecondaryRank());
        Assertions.assertEquals(-1, account.getSecondaryRank_end());

        Assertions.assertEquals(RankUnit.VIP, account.getSecondaryRank());

        Assertions.assertEquals(RankUnit.MODERATRICE, account.getRank());

    }

    @Test
    public void TestTemporaryRank(){
        Account account = new Account();
        account.setRank(RankUnit.VIP, 1000);
        Assertions.assertTrue(account.secondaryRankIsTemporary());

        account.setRank(RankUnit.MODERATRICE);
        Assertions.assertTrue(account.secondaryRankIsTemporary());
        Assertions.assertFalse(account.specialRankIsTemporary());

    }

    @AfterAll
    public static void tearDown() {
        System.out.println("AccountTest - ending");
    }
}
