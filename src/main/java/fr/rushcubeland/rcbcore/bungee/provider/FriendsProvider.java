package fr.rushcubeland.rcbcore.bungee.provider;

import fr.rushcubeland.commons.AFriends;
import fr.rushcubeland.commons.data.exceptions.AccountNotFoundException;
import fr.rushcubeland.commons.data.redis.RedisAccess;
import fr.rushcubeland.commons.data.sql.DatabaseManager;
import fr.rushcubeland.rcbcore.bungee.RcbAPI;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class FriendsProvider {

    public static final String REDIS_KEY = "friends:";
    public static final AFriends DEFAULT_ACCOUNT = new AFriends(UUID.randomUUID(), 20, new ArrayList<>());

    private final ProxiedPlayer player;
    private final RedisAccess redisAccess;
    private final UUID uuid;

    public FriendsProvider(ProxiedPlayer player) {
        this.player = player;
        this.uuid = player.getUniqueId();
        this.redisAccess = RedisAccess.INSTANCE;
    }

    public FriendsProvider(UUID uuid) {
        this.player = null;
        this.redisAccess = RedisAccess.INSTANCE;
        this.uuid = uuid;
    }

    public AFriends getAccount() throws AccountNotFoundException {

        AFriends account = getFriendsFromRedis();

        if(account == null){
            account = getFriendsFromDatabase();
            sendFriendsToRedis(account);
        }
        return account;
    }

    public void sendFriendsToRedis(AFriends account){
        ProxyServer.getInstance().getScheduler().runAsync(RcbAPI.getInstance(), () -> {
            final RedissonClient redissonClient = RedisAccess.INSTANCE.getRedissonClient();
            final String key = REDIS_KEY + uuid.toString();
            final RBucket<AFriends> accountRBucket = redissonClient.getBucket(key);

            accountRBucket.set(account);
        });
    }

    private AFriends getFriendsFromRedis(){
        final RedissonClient redissonClient = redisAccess.getRedissonClient();
        String key = REDIS_KEY;
        if (this.player == null){
            key += this.uuid;
        }
        else
        {
            key += this.player.getUniqueId();
        }
        final RBucket<AFriends> accountRBucket = redissonClient.getBucket(key);

        return accountRBucket.get();
    }

    private AFriends getFriendsFromDatabase() throws AccountNotFoundException {

        AFriends aFriends;
        ArrayList<UUID> friends = new ArrayList<>();

        try {

            final Connection connection = DatabaseManager.Main_BDD.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Friends WHERE uuid=?");

            preparedStatement.setString(1, uuid.toString());
            preparedStatement.executeQuery();

            final ResultSet rs = preparedStatement.getResultSet();

            while(rs.next()){

                final UUID uuid = UUID.fromString(rs.getString("friend"));
                friends.add(uuid);

            }

            aFriends = new AFriends(uuid, 20, friends);

            preparedStatement.close();
            connection.close();

        } catch (SQLException e){
            e.printStackTrace();
            throw new AccountNotFoundException(uuid);
        }

        return aFriends;
    }

}
