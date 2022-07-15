package fr.rushcubeland.rcbcore.bukkit.provider;

import fr.rushcubeland.commons.ACosmetics;
import fr.rushcubeland.commons.cosmetics.ParticlesUnit;
import fr.rushcubeland.commons.cosmetics.PetsUnit;
import fr.rushcubeland.commons.data.exceptions.AccountNotFoundException;
import fr.rushcubeland.commons.data.redis.RedisAccess;
import fr.rushcubeland.commons.data.sql.DatabaseManager;
import fr.rushcubeland.commons.data.sql.SQL;
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

public class CosmeticsProvider {

    public static final String REDIS_KEY = "cosmetics:";
    public static final ACosmetics DEFAULT_ACCOUNT = new ACosmetics(UUID.randomUUID(), null, null);

    private final Player player;
    private final RedisAccess redisAccess;
    private final UUID uuid;

    public CosmeticsProvider(Player player) {
        this.player = player;
        this.redisAccess = RedisAccess.INSTANCE;
        this.uuid = player.getUniqueId();
    }

    public CosmeticsProvider(UUID uuid) {
        this.player = null;
        this.redisAccess = RedisAccess.INSTANCE;
        this.uuid = uuid;
    }

    public ACosmetics getAccount() throws AccountNotFoundException {

        ACosmetics account = getAccountFromRedis();

        if(account == null){
            account = getAccountFromDatabase();
            sendAccountToRedis(account);
        }
        return account;
    }

    public void sendAccountToRedis(ACosmetics account){
        Bukkit.getScheduler().runTaskAsynchronously(RcbAPI.getInstance(), () -> {
            final RedissonClient redissonClient = RedisAccess.INSTANCE.getRedissonClient();
            final String key = REDIS_KEY + uuid.toString();
            final RBucket<ACosmetics> accountRBucket = redissonClient.getBucket(key);

            accountRBucket.set(account);
        });
    }

    private ACosmetics getAccountFromRedis(){
        final RedissonClient redissonClient = redisAccess.getRedissonClient();
        String key = REDIS_KEY;
        if (this.player == null){
            key += this.uuid;
        }
        else
        {
            key += this.player.getUniqueId();
        }
        final RBucket<ACosmetics> accountRBucket = redissonClient.getBucket(key);

        return accountRBucket.get();
    }

    private ACosmetics getAccountFromDatabase() throws AccountNotFoundException {

        ACosmetics account;

        try {

            final Connection connection = DatabaseManager.Main_BDD.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Cosmetics WHERE uuid=?");

            preparedStatement.setString(1, uuid.toString());
            preparedStatement.executeQuery();

            final ResultSet rs = preparedStatement.getResultSet();

            if(rs.next()){

                String particlesName = rs.getString("current_particles");
                String petName = rs.getString("current_pet");
                ParticlesUnit particles = null;
                PetsUnit pet = null;
                if(!particlesName.equals("null")){
                    particles = ParticlesUnit.getByName(particlesName);
                }
                if(!petName.equals("null")){
                    pet = PetsUnit.getByName(petName);
                }

                account = new ACosmetics(uuid, particles, pet);

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

    private ACosmetics createNewAccount(UUID uuid){

        final ACosmetics account = DEFAULT_ACCOUNT.clone();

        try {

            SQL.update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("INSERT INTO Cosmetics (uuid, current_particles, current_pet) VALUES ('%s', '%s', '%s')",
                    uuid.toString(), "null", "null"));

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        account.setUuid(uuid);

        return account;
    }

}
