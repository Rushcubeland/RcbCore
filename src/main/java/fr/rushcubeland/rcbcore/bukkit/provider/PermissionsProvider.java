package fr.rushcubeland.rcbcore.bukkit.provider;

import fr.rushcubeland.commons.APermissions;
import fr.rushcubeland.commons.data.exceptions.AccountNotFoundException;
import fr.rushcubeland.commons.data.redis.RedisAccess;
import fr.rushcubeland.commons.data.sql.DatabaseManager;
import fr.rushcubeland.rcbcore.bukkit.RcbAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PermissionsProvider {

    public static final String REDIS_KEY = "permissions:";

    private final Player player;
    private final RedisAccess redisAccess;
    private final UUID uuid;

    public PermissionsProvider(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
        this.redisAccess = RedisAccess.INSTANCE;
    }

    public PermissionsProvider(UUID uuid) {
        this.player = null;
        this.redisAccess = RedisAccess.INSTANCE;
        this.uuid = uuid;
    }

    public APermissions getAccount() throws AccountNotFoundException {

        APermissions account = getPermissionsFromRedis();

        if(account == null){
            account = getPermissionsFromDatabase();

            sendPermissionsToRedis(account);

        }
        return account;
    }

    public void sendPermissionsToRedis(APermissions account){
        Bukkit.getScheduler().runTaskAsynchronously(RcbAPI.getInstance(), () -> {
            final RedissonClient redissonClient = RedisAccess.INSTANCE.getRedissonClient();
            final String key = REDIS_KEY + uuid.toString();
            final RBucket<APermissions> accountRBucket = redissonClient.getBucket(key);

            accountRBucket.set(account);
        });
    }

    private APermissions getPermissionsFromRedis(){
        final RedissonClient redissonClient = redisAccess.getRedissonClient();
        String key = REDIS_KEY;
        if (this.player == null){
            key += this.uuid;
        }
        else
        {
            key += this.player.getUniqueId();
        }
        final RBucket<APermissions> accountRBucket = redissonClient.getBucket(key);

        return accountRBucket.get();
    }

    private APermissions getPermissionsFromDatabase() throws AccountNotFoundException {

        List<String> perms = new ArrayList<>();
        APermissions aPermissions;

        try {
            final Connection connection = DatabaseManager.Main_BDD.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Player_permissions WHERE uuid=?");
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.executeQuery();

            final ResultSet rs = preparedStatement.getResultSet();

            while(rs.next()) {

                String permission = rs.getString("permission");
                perms.add(permission);

            }

            aPermissions = new APermissions(uuid, perms);

            preparedStatement.close();
            connection.close();


        } catch (SQLException e) {
            e.printStackTrace();
            throw new AccountNotFoundException(uuid);
        }
        return aPermissions;
    }

}
