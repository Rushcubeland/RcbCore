package fr.rushcubeland.rcbcore.bungee.provider;

import fr.rushcubeland.commons.AStats;
import fr.rushcubeland.commons.data.exceptions.AccountNotFoundException;
import fr.rushcubeland.commons.data.redis.RedisAccess;
import fr.rushcubeland.commons.data.sql.DatabaseManager;
import fr.rushcubeland.commons.data.sql.SQL;
import fr.rushcubeland.rcbcore.bungee.RcbAPI;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

public class StatsProvider {

    public static final String REDIS_KEY = "stats:";
    public static final AStats DEFAULT_ACCOUNT = new AStats(UUID.randomUUID(), 0, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()));

    private final ProxiedPlayer player;
    private final RedisAccess redisAccess;
    private final UUID uuid;

    public StatsProvider(ProxiedPlayer player) {
        this.player = player;
        this.redisAccess = RedisAccess.INSTANCE;
        this.uuid = player.getUniqueId();
    }

    public StatsProvider(UUID uuid) {
        this.player = null;
        this.redisAccess = RedisAccess.INSTANCE;
        this.uuid = uuid;
    }

    public AStats getAccount() throws AccountNotFoundException {

        AStats aStats = getAccountFromRedis();

        if(aStats == null){
            aStats = getAccountFromDatabase();
            sendAccountToRedis(aStats);
        }
        return aStats;
    }

    public void sendAccountToRedis(AStats account){
        ProxyServer.getInstance().getScheduler().runAsync(RcbAPI.getInstance(), () -> {
            final RedissonClient redissonClient = RedisAccess.INSTANCE.getRedissonClient();
            final String key = REDIS_KEY + uuid.toString();
            final RBucket<AStats> accountRBucket = redissonClient.getBucket(key);

            accountRBucket.set(account);
        });
    }

    private AStats getAccountFromRedis(){
        final RedissonClient redissonClient = redisAccess.getRedissonClient();
        String key = REDIS_KEY;
        if (this.player == null){
            key += this.uuid;
        }
        else
        {
            key += this.player.getUniqueId();
        }
        final RBucket<AStats> accountRBucket = redissonClient.getBucket(key);

        return accountRBucket.get();
    }

    private AStats getAccountFromDatabase() throws AccountNotFoundException {

        AStats account;

        try {

            final Connection connection = DatabaseManager.Main_BDD.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Stats WHERE uuid=?");

            preparedStatement.setString(1, uuid.toString());
            preparedStatement.executeQuery();

            final ResultSet rs = preparedStatement.getResultSet();

            if(rs.next()){

                final long parcoursTimer = rs.getLong("parcours_timer");
                final long firstConnectionMillis = rs.getLong("firstconnection_ms");
                final long lastconnectionMilLis = rs.getLong("lastconnection_ms");

                Date firstDate = new Date(firstConnectionMillis);
                Date lastDate = new Date(lastconnectionMilLis);

                account = new AStats(uuid, parcoursTimer, firstDate, lastDate);

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

    private AStats createNewAccount(UUID uuid){

        final AStats account = DEFAULT_ACCOUNT.clone();

        ProxyServer.getInstance().getScheduler().runAsync(RcbAPI.getInstance(), () -> {
            try {

                SQL.update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("INSERT INTO Stats (uuid, parcours_timer, firstconnection_ms, lastconnection_ms) VALUES ('%s', '%s', '%s', '%s')",
                        uuid.toString(), account.getParcoursTimer(), account.getFirstConnection().getTime(), account.getLastConnection().getTime()));

            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });

        account.setUuid(uuid);

        return account;
    }
}
