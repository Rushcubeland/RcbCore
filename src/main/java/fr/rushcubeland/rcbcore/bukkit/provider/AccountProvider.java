package fr.rushcubeland.rcbcore.bukkit.provider;

import fr.rushcubeland.commons.Account;
import fr.rushcubeland.commons.data.exceptions.AccountNotFoundException;
import fr.rushcubeland.commons.data.redis.RedisAccess;
import fr.rushcubeland.commons.data.sql.DatabaseManager;
import fr.rushcubeland.commons.data.sql.SQL;
import fr.rushcubeland.commons.rank.RankUnit;
import fr.rushcubeland.rcbcore.bukkit.RcbAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AccountProvider {

    public static final String REDIS_KEY = "account:";
    public static final Account DEFAULT_ACCOUNT = new Account(UUID.randomUUID(), null, RankUnit.JOUEUR, 0);

    private final Player player;
    private final RedisAccess redisAccess;
    private final UUID uuid;

    public AccountProvider(Player player) {
        this.player = player;
        this.redisAccess = RedisAccess.INSTANCE;
        this.uuid = player.getUniqueId();
    }

    public AccountProvider(UUID uuid) {
        this.player = null;
        this.redisAccess = RedisAccess.INSTANCE;
        this.uuid = uuid;
    }

    public Account getAccount() throws AccountNotFoundException {

        Account account = getAccountFromRedis();

        if(account == null){
            account = getAccountFromDatabase();
            sendAccountToRedis(account);
        }
        return account;
    }

    public void sendAccountToRedis(Account account){
        Bukkit.getScheduler().runTaskAsynchronously(RcbAPI.getInstance(), () -> {
            final RedissonClient redissonClient = RedisAccess.INSTANCE.getRedissonClient();
            final String key = REDIS_KEY + uuid.toString();
            final RBucket<Account> accountRBucket = redissonClient.getBucket(key);

            accountRBucket.set(account);
        });
    }

    private Account getAccountFromRedis(){
        final RedissonClient redissonClient = redisAccess.getRedissonClient();
        String key = REDIS_KEY;
        if (this.player == null){
            key += this.uuid;
        }
        else
        {
            key += this.player.getUniqueId();
        }
        final RBucket<Account> accountRBucket = redissonClient.getBucket(key);

        return accountRBucket.get();
    }

    private Account getAccountFromDatabase() throws AccountNotFoundException {

        Account account;

        try {

            final Connection connection = DatabaseManager.Main_BDD.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Accounts WHERE uuid=?");

            preparedStatement.setString(1, uuid.toString());
            preparedStatement.executeQuery();

            final ResultSet rs = preparedStatement.getResultSet();

            if(rs.next()){

                RankUnit rank = null;
                if(!rs.getString("primaryRank").equals("null")){
                    rank = RankUnit.getByName(rs.getString("primaryRank"));
                }
                final RankUnit rank2 = RankUnit.getByName(rs.getString("secondaryRank"));
                final long rank_end = rs.getLong("primaryRank_end");
                final long rank2_end = rs.getLong("secondaryRank_end");
                final long coins = rs.getLong("coins");

                account = new Account(uuid, rank, rank2, rank_end, rank2_end, coins);

            }
            else
            {
                account = createNewAccount(uuid);
            }

            preparedStatement.close();
            connection.close();

        } catch (SQLException e){
            e.printStackTrace();
            throw new AccountNotFoundException(uuid);
        }

        return account;
    }

    private Account createNewAccount(UUID uuid){

        final Account account = DEFAULT_ACCOUNT.clone();

        try {

            SQL.update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("INSERT INTO Accounts (uuid, primaryRank, secondaryRank, primaryRank_end, secondaryRank_end, coins) VALUES ('%s', '%s', '%s', '%s', '%s', '%s')",
                    uuid.toString(), "null", account.getSecondaryRank().getName(), account.getPrimaryRank_end(), account.getSecondaryRank_end(), account.getCoins()));

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        account.setUuid(uuid);

        return account;
    }
}