package fr.rushcubeland.rcbcore.bukkit;

import fr.rushcubeland.commons.*;
import fr.rushcubeland.commons.data.callbacks.AsyncCallBack;
import fr.rushcubeland.commons.data.exceptions.AccountNotFoundException;
import fr.rushcubeland.commons.data.redis.RedisAccess;
import fr.rushcubeland.commons.data.sql.DatabaseManager;
import fr.rushcubeland.commons.data.sql.SQL;
import fr.rushcubeland.commons.rank.RankUnit;
import fr.rushcubeland.commons.utils.MessageUtil;
import fr.rushcubeland.rcbcore.bukkit.commands.*;
import fr.rushcubeland.rcbcore.bukkit.friends.FriendsGUIUpdater;
import fr.rushcubeland.rcbcore.bukkit.listeners.*;
import fr.rushcubeland.rcbcore.bukkit.mod.ModModeratorTask;
import fr.rushcubeland.rcbcore.bukkit.network.Network;
import fr.rushcubeland.rcbcore.bukkit.network.ServerGroup;
import fr.rushcubeland.rcbcore.bukkit.provider.*;
import fr.rushcubeland.rcbcore.bukkit.tools.PacketReader;
import fr.rushcubeland.rcbcore.bukkit.tools.ScoreboardSign;
import fr.rushcubeland.rcbcore.bukkit.tools.Tablist;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RcbAPI extends JavaPlugin {

    private static RcbAPI instance;
    public final static String channel = "rcbapi:main";

    private Tablist tablist;

    public Map<Player, ScoreboardSign> boards = new HashMap<>();
    @Override
    public void onEnable() {
        instance = this;

        getLogger().info("========================");
        getLogger().info("API initialization in progress...");
        getLogger().info("========================");

        saveDefaultConfig();

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, channel);
        Bukkit.getMessenger().registerIncomingPluginChannel(this, channel, new BukkitReceive());

        registerEvents();
        registerCommands();

        ServerGroup.initServerGroup();

        Tablist tablist = new Tablist();
        tablist.initTabListTeam();
        this.tablist = tablist;

        DatabaseManager.initAllDatabaseConnections();
        SQL.createTables();

        RedisAccess.init();

        initAllRankPermissions();

        //BattlePassUnit.getCurrentBattlePass().onEnableServer();

        Network.startTaskUpdateSlotsServer();

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new Network());

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new ModModeratorTask(), 1, 20L);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(RcbAPI.getInstance(), new FriendsGUIUpdater(), 2400L, 2400L);

        getLogger().info("RcbCore enabled");

    }

    @Override
    public void onDisable() {
        closeAllRankPermissions();
        for(Player player : Bukkit.getOnlinePlayers()){
            PacketReader reader = new PacketReader();
            reader.uninject(player);
        }
        this.getServer().getMessenger().unregisterIncomingPluginChannel(RcbAPI.getInstance());
        this.getServer().getMessenger().unregisterIncomingPluginChannel(RcbAPI.getInstance());

        //BattlePassUnit.getCurrentBattlePass().onDisableServer();

        RedisAccess.close();
        DatabaseManager.closeAllDatabaseConnection();

        instance = null;

        getLogger().info("RcbCore disabled");
    }
    
    private void registerEvents(){
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(new BukkitReceive(), this);
        pm.registerEvents(new ClickEvent(), this);
        pm.registerEvents(new JoinEvent(), this);
        pm.registerEvents(new FoodChange(), this);
        pm.registerEvents(new PlaceBlock(), this);
        pm.registerEvents(new BreackBlock(), this);
        pm.registerEvents(new DamageEvent(), this);
        pm.registerEvents(new PickupEvent(), this);
        pm.registerEvents(new DropEvent(), this);
        pm.registerEvents(new QuitEvent(), this);
        pm.registerEvents(new CommandBlocker(), this);
        pm.registerEvents(new FrameIntegrity(), this);
    }

    private void registerCommands(){
        getCommand("gamemode").setExecutor(new GMCommand());
        getCommand("reportmsg").setExecutor(new ReportMsgCommand());
        getCommand("reportconfirm").setExecutor(new ReportMsgConfirmCommand());
        getCommand("coins").setExecutor(new CoinsCommand());
        getCommand("apmsgb").setExecutor(new SanctionMsgCommand());
    }

    private void initAllRankPermissions(){
        for(RankUnit rank : RankUnit.values()){
            rank.onEnable();
        }
    }

    private void closeAllRankPermissions(){
        for(RankUnit rank : RankUnit.values()){
            rank.onDisable();
        }
    }

    public Account getAccount(Player player){
        Account account = null;

        try {

            final AccountProvider accountProvider = new AccountProvider(player);
            account = accountProvider.getAccount();

        } catch (AccountNotFoundException | RedisException exception) {
            exception.printStackTrace();
        }
        return account;
    }

    public void getAccount(Player player, final AsyncCallBack callback){
        Bukkit.getScheduler().runTaskAsynchronously(RcbAPI.getInstance(), () -> {
            Account account;

            try {

                final AccountProvider accountProvider = new AccountProvider(player);
                account = accountProvider.getAccount();

                Bukkit.getScheduler().runTask(RcbAPI.getInstance(), () -> {
                    callback.onQueryComplete(account);
                });

            } catch (AccountNotFoundException exception) {
                exception.printStackTrace();
            }
        });
    }

    public void getAccount(UUID uuid, final AsyncCallBack callback){
        Bukkit.getScheduler().runTaskAsynchronously(RcbAPI.getInstance(), () -> {
            Account account;

            try {

                final AccountProvider accountProvider = new AccountProvider(uuid);
                account = accountProvider.getAccount();

                Bukkit.getScheduler().runTask(RcbAPI.getInstance(), () -> {
                    callback.onQueryComplete(account);
                });


            } catch (AccountNotFoundException exception) {
                exception.printStackTrace();
            }
        });
    }

    public void getAccountOptions(Player player, final AsyncCallBack callback) {

        Bukkit.getScheduler().runTaskAsynchronously(RcbAPI.getInstance(), () -> {
            AOptions account;

            try {

                final OptionsProvider accountProvider = new OptionsProvider(player);
                account = accountProvider.getAccount();

                Bukkit.getScheduler().runTask(RcbAPI.getInstance(), () -> callback.onQueryComplete(account));


            } catch (AccountNotFoundException exception) {
                exception.printStackTrace();
            }
        });
    }

    public void getAccountOptions(UUID uuid, final AsyncCallBack callback) {

        Bukkit.getScheduler().runTaskAsynchronously(RcbAPI.getInstance(), () -> {
            AOptions account;

            try {

                final OptionsProvider accountProvider = new OptionsProvider(uuid);
                account = accountProvider.getAccount();

                Bukkit.getScheduler().runTask(RcbAPI.getInstance(), () -> {
                    callback.onQueryComplete(account);
                });


            } catch (AccountNotFoundException exception) {
                exception.printStackTrace();
            }
        });
    }

    public void getAccountFriends(Player player, final AsyncCallBack callback) {

        Bukkit.getScheduler().runTaskAsynchronously(RcbAPI.getInstance(), () -> {
            AFriends account;

            try {

                final FriendsProvider accountProvider = new FriendsProvider(player);
                account = accountProvider.getAccount();

                Bukkit.getScheduler().runTask(RcbAPI.getInstance(), () -> {
                    callback.onQueryComplete(account);
                });


            } catch (AccountNotFoundException exception) {
                exception.printStackTrace();
            }
        });
    }

    public void getAccountFriends(UUID uuid, final AsyncCallBack callback) {

        Bukkit.getScheduler().runTaskAsynchronously(RcbAPI.getInstance(), () -> {
            AFriends account;

            try {

                final FriendsProvider accountProvider = new FriendsProvider(uuid);
                account = accountProvider.getAccount();

                Bukkit.getScheduler().runTask(RcbAPI.getInstance(), () -> {
                    callback.onQueryComplete(account);
                });


            } catch (AccountNotFoundException exception) {
                exception.printStackTrace();
            }
        });
    }

    public void getAccountPermissions(Player player, final AsyncCallBack callback) {

        Bukkit.getScheduler().runTaskAsynchronously(RcbAPI.getInstance(), () -> {
            APermissions account;

            try {

                final PermissionsProvider accountProvider = new PermissionsProvider(player);
                account = accountProvider.getAccount();

                Bukkit.getScheduler().runTask(RcbAPI.getInstance(), () -> {
                    callback.onQueryComplete(account);
                });


            } catch (AccountNotFoundException exception) {
                exception.printStackTrace();
            }
        });
    }

    public void getAccountPermissions(UUID uuid, final AsyncCallBack callback) {

        Bukkit.getScheduler().runTaskAsynchronously(RcbAPI.getInstance(), () -> {
            APermissions account;

            try {

                final PermissionsProvider accountProvider = new PermissionsProvider(uuid);
                account = accountProvider.getAccount();

                Bukkit.getScheduler().runTask(RcbAPI.getInstance(), () -> {
                    callback.onQueryComplete(account);
                });

            } catch (AccountNotFoundException exception) {
                exception.printStackTrace();
            }
        });
    }

    public void getAccountStats(Player player, final AsyncCallBack callback) {

        Bukkit.getScheduler().runTaskAsynchronously(RcbAPI.getInstance(), () -> {
            AStats account;

            try {

                final StatsProvider accountProvider = new StatsProvider(player);
                account = accountProvider.getAccount();

                Bukkit.getScheduler().runTask(RcbAPI.getInstance(), () -> {
                    callback.onQueryComplete(account);
                });

            } catch (AccountNotFoundException exception) {
                exception.printStackTrace();
            }
        });
    }

    public void getAccountStats(UUID uuid, final AsyncCallBack callback) {

        Bukkit.getScheduler().runTaskAsynchronously(RcbAPI.getInstance(), () -> {
            AStats account;

            try {

                final StatsProvider accountProvider = new StatsProvider(uuid);
                account = accountProvider.getAccount();

                Bukkit.getScheduler().runTask(RcbAPI.getInstance(), () -> {
                    callback.onQueryComplete(account);
                });

            } catch (AccountNotFoundException exception) {
                exception.printStackTrace();
            }
        });
    }

    public void getAccountStatsDAC(Player player, final AsyncCallBack callback) {

        Bukkit.getScheduler().runTaskAsynchronously(RcbAPI.getInstance(), () -> {
            AStatsDAC account;

            try {

                final StatsDACProvider accountProvider = new StatsDACProvider(player);
                account = accountProvider.getAccount();

                Bukkit.getScheduler().runTask(RcbAPI.getInstance(), () -> {
                    callback.onQueryComplete(account);
                });

            } catch (AccountNotFoundException exception) {
                exception.printStackTrace();
            }
        });
    }

    public void getAccountStatsDAC(UUID uuid, final AsyncCallBack callback) {

        Bukkit.getScheduler().runTaskAsynchronously(RcbAPI.getInstance(), () -> {
            AStatsDAC account;

            try {

                final StatsDACProvider accountProvider = new StatsDACProvider(uuid);
                account = accountProvider.getAccount();

                Bukkit.getScheduler().runTask(RcbAPI.getInstance(), () -> {
                    callback.onQueryComplete(account);
                });

            } catch (AccountNotFoundException exception) {
                exception.printStackTrace();
            }
        });
    }

    public void sendAPermissionsToRedis(APermissions account){
        Bukkit.getScheduler().runTaskAsynchronously(RcbAPI.getInstance(), () -> {

            final RedissonClient redissonClient = RedisAccess.INSTANCE.getRedissonClient();
            final String key = "permissions:" + account.getUuid().toString();
            final RBucket<APermissions> accountRBucket = redissonClient.getBucket(key);

            accountRBucket.set(account);

        });
    }

    public void sendAStatsToRedis(AStats account){
        Bukkit.getScheduler().runTaskAsynchronously(RcbAPI.getInstance(), () -> {

            final RedissonClient redissonClient = RedisAccess.INSTANCE.getRedissonClient();
            final String key = "stats:" + account.getUuid().toString();
            final RBucket<AStats> accountRBucket = redissonClient.getBucket(key);

            accountRBucket.set(account);

        });
    }

    public void sendAStatsDACToRedis(AStatsDAC account){
        Bukkit.getScheduler().runTaskAsynchronously(RcbAPI.getInstance(), () -> {

            final RedissonClient redissonClient = RedisAccess.INSTANCE.getRedissonClient();
            final String key = "stats_dac:" + account.getUuid().toString();
            final RBucket<AStatsDAC> accountRBucket = redissonClient.getBucket(key);

            accountRBucket.set(account);

        });
    }

    public void sendACosmeticsToRedis(ACosmetics account){
        Bukkit.getScheduler().runTaskAsynchronously(RcbAPI.getInstance(), () -> {

            final RedissonClient redissonClient = RedisAccess.INSTANCE.getRedissonClient();
            final String key = "cosmetics:" + account.getUuid().toString();
            final RBucket<ACosmetics> accountRBucket = redissonClient.getBucket(key);

            accountRBucket.set(account);

        });
    }

    public void sendAFriendsToRedis(AFriends account){
        Bukkit.getScheduler().runTaskAsynchronously(RcbAPI.getInstance(), () -> {

            final RedissonClient redissonClient = RedisAccess.INSTANCE.getRedissonClient();
            final String key = "friends:" + account.getUuid().toString();
            final RBucket<AFriends> accountRBucket = redissonClient.getBucket(key);

            accountRBucket.set(account);

        });
    }

    public void sendAccountToRedis(Account account){
        Bukkit.getScheduler().runTaskAsynchronously(RcbAPI.getInstance(), () -> {

            final RedissonClient redissonClient = RedisAccess.INSTANCE.getRedissonClient();
            final String key = "account:" + account.getUuid().toString();
            final RBucket<Account> accountRBucket = redissonClient.getBucket(key);

            accountRBucket.set(account);

        });
    }

    public void deleteScoreboard(Player player) {
        if (this.boards.containsKey(player)) {
            this.boards.get(player).destroy();
            this.boards.remove(player);
        }
    }

    public Tablist getTablist() {
        return tablist;
    }


    public static RcbAPI getInstance() {
        return instance;
    }
}
