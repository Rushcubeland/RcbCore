package fr.rushcubeland.rcbcore.bungee.provider;

import fr.rushcubeland.commons.AStatsDAC;
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
import java.util.UUID;

public class StatsDACProvider {

    public static final String REDIS_KEY = "stats_dac:";
    public static final AStatsDAC DEFAULT_ACCOUNT = new AStatsDAC(UUID.randomUUID(), 0, 0, 0, 0, 0, 0, 0);

    private final ProxiedPlayer player;
    private final RedisAccess redisAccess;
    private final UUID uuid;

    public StatsDACProvider(ProxiedPlayer player) {
        this.player = player;
        this.uuid = player.getUniqueId();
        this.redisAccess = RedisAccess.INSTANCE;
    }

    public StatsDACProvider(UUID uuid) {
        this.player = null;
        this.redisAccess = RedisAccess.INSTANCE;
        this.uuid = uuid;
    }

    public AStatsDAC getAccount() throws AccountNotFoundException {

        AStatsDAC aStatsDAC = getAccountFromRedis();

        if(aStatsDAC == null){
            aStatsDAC = getAccountFromDatabase();
            sendAccountToRedis(aStatsDAC);
        }
        return aStatsDAC;
    }

    public void sendAccountToRedis(AStatsDAC account){
        ProxyServer.getInstance().getScheduler().runAsync(RcbAPI.getInstance(), () -> {
            final RedissonClient redissonClient = RedisAccess.INSTANCE.getRedissonClient();
            final String key = REDIS_KEY + uuid.toString();
            final RBucket<AStatsDAC> accountRBucket = redissonClient.getBucket(key);

            accountRBucket.set(account);
        });
    }

    private AStatsDAC getAccountFromRedis(){
        final RedissonClient redissonClient = redisAccess.getRedissonClient();
        String key = REDIS_KEY;
        if (this.player == null){
            key += this.uuid;
        }
        else
        {
            key += this.player.getUniqueId();
        }
        final RBucket<AStatsDAC> accountRBucket = redissonClient.getBucket(key);

        return accountRBucket.get();
    }

    private AStatsDAC getAccountFromDatabase() throws AccountNotFoundException {

        AStatsDAC account;

        try {

            final Connection connection = DatabaseManager.Main_BDD.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM StatsDAC WHERE uuid=?");

            preparedStatement.setString(1, uuid.toString());
            preparedStatement.executeQuery();

            final ResultSet rs = preparedStatement.getResultSet();

            if(rs.next()){

                final int wins = rs.getInt("wins");
                final int loses = rs.getInt("loses");
                final int nbParties = rs.getInt("nbParties");
                final int nbSortsUsed = rs.getInt("nbSortsUsed");
                final int nbJumps = rs.getInt("nbJumps");
                final int nbSuccessJumps = rs.getInt("nbSuccessJumps");
                final int nbFailJumps = rs.getInt("nbFailJumps");

                account = new AStatsDAC(uuid, wins, loses, nbParties, nbSortsUsed, nbJumps, nbSuccessJumps, nbFailJumps);

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

    private AStatsDAC createNewAccount(UUID uuid){

        final AStatsDAC account = DEFAULT_ACCOUNT.clone();

        ProxyServer.getInstance().getScheduler().runAsync(RcbAPI.getInstance(), () -> {
            try {

                SQL.update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("INSERT INTO StatsDAC " +
                                "(uuid, wins, loses, nbParties, nbSortsUsed, nbJumps, nbSuccessJumps, nbFailJumps)" +
                                " VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
                        uuid.toString(), account.getWins(), account.getLoses(), account.getNbParties(), account.getNbSortsUsed(),
                        account.getNbJumps(), account.getNbSuccessJumps(), account.getNbFailJumps()));

            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });

        account.setUuid(uuid);

        return account;
    }
}
