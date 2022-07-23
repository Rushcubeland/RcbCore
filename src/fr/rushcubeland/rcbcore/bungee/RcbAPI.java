package fr.rushcubeland.rcbcore.bungee;

import fr.rushcubeland.commons.*;
import fr.rushcubeland.commons.data.exceptions.AccountNotFoundException;
import fr.rushcubeland.commons.data.redis.RedisAccess;
import fr.rushcubeland.commons.data.sql.DatabaseManager;
import fr.rushcubeland.commons.data.sql.SQL;
import fr.rushcubeland.commons.rank.RankUnit;
import fr.rushcubeland.commons.utils.MessageUtil;
import fr.rushcubeland.rcbcore.bungee.commands.*;
import fr.rushcubeland.rcbcore.bungee.listeners.*;
import fr.rushcubeland.rcbcore.bungee.maintenance.Maintenance;
import fr.rushcubeland.rcbcore.bungee.network.Network;
import fr.rushcubeland.rcbcore.bungee.network.ServerGroup;
import fr.rushcubeland.rcbcore.bungee.parties.Party;
import fr.rushcubeland.rcbcore.bungee.provider.*;
import fr.rushcubeland.rcbcore.bungee.sanctions.SanctionsUpdateTask;
import fr.rushcubeland.rcbcore.bungee.sanctions.ban.BanManager;
import fr.rushcubeland.rcbcore.bungee.sanctions.mute.CheckMuteStateTask;
import fr.rushcubeland.rcbcore.bungee.sanctions.mute.MuteManager;
import fr.rushcubeland.rcbcore.bungee.sender.AccountsSender;
import fr.rushcubeland.rcbcore.bungee.utils.TimeUnit;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.util.*;

public class RcbAPI extends Plugin {

    private static RcbAPI instance;

    private final List<Party> parties = new ArrayList<>();
    private final List<AParty> accountParty = new ArrayList<>();

    private final HashMap<ProxiedPlayer, ProxiedPlayer> mpData = new HashMap<>();

    public final static String channel = "rcbapi:main";

    private BanManager banManager;
    private MuteManager muteManager;

    @Override
    public void onEnable() {
        instance = this;

        // INIT DATABASE & CACHING CONNECTION
        DatabaseManager.initAllDatabaseConnections();
        SQL.createTables();
        RedisAccess.init();

        Maintenance.initMaintenanceSystem();

        // INIT LISTENERS
        ProxyServer.getInstance().registerChannel(channel);
        ProxyServer.getInstance().getPluginManager().registerListener(this, new BungeeReceive());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new AutoCompletion());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new ProxiedPlayerJoin());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new ProxiedPlayerQuit());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new ProxyPing());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new ServerConnect());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new OnChat());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new ServerKick());

        ServerGroup.initServerGroup();

        initCommands();
        TimeUnit.initTimeUnit();

        initAllRankPermissions();

        this.banManager = new BanManager();
        banManager.load();
        this.muteManager = new MuteManager();
        muteManager.load();

        Network.startUpdateServersTask();
        SanctionsUpdateTask.update();
        ProxyServer.getInstance().getScheduler().schedule(this, new CheckMuteStateTask(), 1, 3, java.util.concurrent.TimeUnit.SECONDS);

        // START CUSTOM WRITE BEHIND CACHING STRATEGIE
        AccountsSender accountSender = new AccountsSender();
        accountSender.start();

    }

    @Override
    public void onDisable() {
        closeAllRankPermissions();
        getBanManager().update();
        getMuteManager().update();

        Maintenance.stopMaintenanceSystem();

        DatabaseManager.closeAllDatabaseConnection();

        RedisAccess.close();

        instance = null;
    }

    public static RcbAPI getInstance() {
        return instance;
    }

    private void initCommands(){
        for(String cmd : Btp.ALIASES) {
            ProxyServer.getInstance().getPluginManager()
                    .registerCommand(this, new Btp(cmd));
        }
        for(String cmd : FriendCommand.ALIASES) {
            ProxyServer.getInstance().getPluginManager()
                    .registerCommand(this, new FriendCommand(cmd));
        }
        for(String cmd : HubCommand.ALIASES){
            ProxyServer.getInstance().getPluginManager()
                    .registerCommand(this, new HubCommand(cmd));
        }
        for(String cmd : ReplyCommand.ALIASES){
            ProxyServer.getInstance().getPluginManager()
                    .registerCommand(this, new ReplyCommand(cmd));
        }
        for(String cmd : PartyCommand.ALIASES){
            ProxyServer.getInstance().getPluginManager()
                    .registerCommand(this, new PartyCommand(cmd));
        }
        for(String cmd : OptionsCommand.ALIASES){
            ProxyServer.getInstance().getPluginManager()
                    .registerCommand(this, new OptionsCommand(cmd));
        }
        for(String cmd : ReportCommand.ALIASES){
            ProxyServer.getInstance().getPluginManager()
                    .registerCommand(this, new ReportCommand(cmd));
        }
        for(String cmd : StaffChatCommand.ALIASES){
            ProxyServer.getInstance().getPluginManager()
                    .registerCommand(this, new StaffChatCommand(cmd));
        }
        for(String cmd : PlayersListCommand.ALIASES){
            ProxyServer.getInstance().getPluginManager()
                    .registerCommand(this, new PlayersListCommand(cmd));
        }
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new BanCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new UnbanCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new WhoisCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new MuteCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new UnmuteCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new PunishGUICommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new KickCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new ModModeratorCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new MPCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new PunishGUIMsgCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new MaintenanceCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new StaffListCommand());
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

    public Account getAccount(ProxiedPlayer player) {

        Account account = null;

        try {

            final AccountProvider accountProvider = new AccountProvider(player);
            account = accountProvider.getAccount();


        } catch (AccountNotFoundException exception) {
            System.err.println(exception.getMessage());
            player.disconnect(new TextComponent(MessageUtil.ACCOUNT_NOT_FOUND.getMessage()));
        }

        return account;
    }

    public Account getAccount(UUID uuid) {

        Account account = null;

        try {

            final AccountProvider accountProvider = new AccountProvider(uuid);
            account = accountProvider.getAccount();


        } catch (AccountNotFoundException exception) {
            System.err.println(exception.getMessage());
        }

        return account;
    }

    public AOptions getAccountOptions(ProxiedPlayer player) {

        AOptions account = null;

        try {

            final OptionsProvider optionsProvider = new OptionsProvider(player);
            account = optionsProvider.getAccount();


        } catch (AccountNotFoundException exception) {
            System.err.println(exception.getMessage());
        }

        return account;
    }

    public AStatsDAC getAccountStatsDAC(ProxiedPlayer player) {

        AStatsDAC account = null;

        try {

            final StatsDACProvider statsDACProvider = new StatsDACProvider(player);
            account = statsDACProvider.getAccount();


        } catch (AccountNotFoundException exception) {
            System.err.println(exception.getMessage());
        }

        return account;
    }

    public AOptions getAccountOptions(UUID uuid) {

        AOptions account = null;

        try {

            final OptionsProvider optionsProvider = new OptionsProvider(uuid);
            account = optionsProvider.getAccount();


        } catch (AccountNotFoundException exception) {
            System.err.println(exception.getMessage());
        }

        return account;
    }

    public AFriends getAccountFriends(ProxiedPlayer player) {

        AFriends account = null;

        try {

            final FriendsProvider friendsProvider = new FriendsProvider(player);
            account = friendsProvider.getAccount();


        } catch (AccountNotFoundException exception) {
            System.err.println(exception.getMessage());
        }

        return account;
    }

    public AFriends getAccountFriends(UUID uuid) {

        AFriends account = null;

        try {

            final FriendsProvider friendsProvider = new FriendsProvider(uuid);
            account = friendsProvider.getAccount();


        } catch (AccountNotFoundException exception) {
            System.err.println(exception.getMessage());
        }

        return account;
    }

    public APermissions getAccountPermissions(ProxiedPlayer player) {

        APermissions account = null;

        try {

            final PermissionsProvider permissionsProvider = new PermissionsProvider(player);
            account = permissionsProvider.getAccount();


        } catch (AccountNotFoundException exception) {
            System.err.println(exception.getMessage());
        }

        return account;
    }

    public APermissions getAccountPermissions(UUID uuid) {

        APermissions account = null;

        try {

            final PermissionsProvider permissionsProvider = new PermissionsProvider(uuid);
            account = permissionsProvider.getAccount();


        } catch (AccountNotFoundException exception) {
            System.err.println(exception.getMessage());
        }

        return account;
    }

    public AStats getAccountStats(ProxiedPlayer player) {

        AStats account = null;

        try {

            final StatsProvider statsProvider = new StatsProvider(player);
            account = statsProvider.getAccount();


        } catch (AccountNotFoundException exception) {
            System.err.println(exception.getMessage());
        }
        return account;
    }

    public AStats getAccountStats(UUID uuid) {

        AStats account = null;

        try {

            final StatsProvider statsProvider = new StatsProvider(uuid);
            account = statsProvider.getAccount();


        } catch (AccountNotFoundException exception) {
            System.err.println(exception.getMessage());
        }
        return account;
    }

    public void sendAccountToRedis(Account account){
        ProxyServer.getInstance().getScheduler().runAsync(RcbAPI.getInstance(), () -> {
            final RedissonClient redissonClient = RedisAccess.INSTANCE.getRedissonClient();
            final String key = AccountProvider.REDIS_KEY + account.getUuid().toString();
            final RBucket<Account> accountRBucket = redissonClient.getBucket(key);

            accountRBucket.set(account);
        });
    }

    public void sendAccountStatsToRedis(AStats account){
        ProxyServer.getInstance().getScheduler().runAsync(RcbAPI.getInstance(), () -> {
            final RedissonClient redissonClient = RedisAccess.INSTANCE.getRedissonClient();
            final String key = StatsProvider.REDIS_KEY + account.getUuid().toString();
            final RBucket<AStats> accountRBucket = redissonClient.getBucket(key);

            accountRBucket.set(account);
        });
    }
    

    public List<Party> getParties() {
        return parties;
    }

    public BanManager getBanManager() {
        return banManager;
    }

    public MuteManager getMuteManager() {
        return muteManager;
    }

    public String getChannel() {
        return channel;
    }

    public List<AParty> getAPartyList() {
        return accountParty;
    }

    public HashMap<ProxiedPlayer, ProxiedPlayer> getMpData() {
        return mpData;
    }

    public Optional<AParty> getAccountParty(ProxiedPlayer player){
        return new ArrayList<>(accountParty).stream().filter(a -> a.getUuid().toString().equals(player.getUniqueId().toString())).findFirst();
    }
}
