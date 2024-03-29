package fr.rushcubeland.rcbcore.bungee.sender;

import fr.rushcubeland.commons.*;
import fr.rushcubeland.commons.data.redis.RedisAccess;
import fr.rushcubeland.commons.data.sql.DatabaseManager;
import fr.rushcubeland.rcbcore.bungee.RcbAPI;
import fr.rushcubeland.rcbcore.bungee.provider.*;
import org.redisson.api.RBucket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class AccountsSender {

    public static void start(){
        RcbAPI.getInstance().getProxy().getScheduler().schedule(RcbAPI.getInstance(), AccountsSender::sendAccountData, 1, 1, TimeUnit.MINUTES);
        RcbAPI.getInstance().getProxy().getScheduler().schedule(RcbAPI.getInstance(), AccountsSender::sendStatsData, 1, 1, TimeUnit.MINUTES);
        RcbAPI.getInstance().getProxy().getScheduler().schedule(RcbAPI.getInstance(), AccountsSender::sendStatsDacData, 1, 1, TimeUnit.MINUTES);
        RcbAPI.getInstance().getProxy().getScheduler().schedule(RcbAPI.getInstance(), AccountsSender::sendOptionsData, 1, 1, TimeUnit.MINUTES);
        RcbAPI.getInstance().getProxy().getScheduler().schedule(RcbAPI.getInstance(), AccountsSender::sendFriendsAccountData, 1, 1, TimeUnit.MINUTES);
        RcbAPI.getInstance().getProxy().getScheduler().schedule(RcbAPI.getInstance(), AccountsSender::sendPermissionsData, 1, 1, TimeUnit.MINUTES);
        RcbAPI.getInstance().getProxy().getScheduler().schedule(RcbAPI.getInstance(), AccountsSender::sendCosmeticsData, 1, 1, TimeUnit.MINUTES);
    }

    public static void forceSend(){
        sendAccountData();
        sendStatsData();
        sendStatsDacData();
        sendOptionsData();
        sendFriendsAccountData();
        sendCosmeticsData();
        sendPermissionsData();
    }

    private static void sendAccountData() {
        ArrayList<RBucket<Account>> accounts = new ArrayList<>();
        RcbAPI.getInstance().getProxy().getScheduler().runAsync(RcbAPI.getInstance(), () -> {
            String pattern = AccountProvider.REDIS_KEY + "*";
            PreparedStatement preparedStatement;
            Connection connection = null;
            try {
                connection = DatabaseManager.Main_BDD.getDatabaseAccess().getConnection();
                connection.setAutoCommit(false);
                Iterable<String> keys = RedisAccess.INSTANCE.getRedissonClient().getKeys().getKeysByPattern(pattern);
                preparedStatement = connection.prepareStatement(
                        "UPDATE Accounts SET uuid=?, specialRank=?, secondaryRank=?, specialRank_end=?, secondaryRank_end=?, coins=? WHERE uuid=?");
                for (String key : keys) {
                    RBucket<Account> bucket = RedisAccess.INSTANCE.getRedissonClient().getBucket(key);
                    accounts.add(bucket);
                    Account account = bucket.get();
                    if (account != null) {
                        if (account.getSpecialRank() != null) {
                            preparedStatement.setString(2, account.getSpecialRank().getName());
                        }
                        else
                        {
                            preparedStatement.setNull(2, Types.VARCHAR);
                        }
                        preparedStatement.setString(1, account.getUuid().toString());
                        preparedStatement.setString(3, account.getSecondaryRank().getName());
                        preparedStatement.setLong(4, account.getSpecialRank_end());
                        preparedStatement.setLong(5, account.getSecondaryRank_end());
                        preparedStatement.setLong(6, account.getCoins());
                        preparedStatement.setString(7, account.getUuid().toString());
                        preparedStatement.addBatch();
                    }
                }
                if (preparedStatement != null) {
                    preparedStatement.executeBatch();
                    preparedStatement.close();
                    for(RBucket<Account> r : accounts){
                        r.deleteAsync();
                    }
                    accounts.clear();
                }
                connection.commit();
            }
            catch (SQLException exception){
                exception.printStackTrace();
                try {
                    if (connection != null) {
                        connection.rollback();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            finally {
                try {
                    if (connection != null) {
                        connection.setAutoCommit(true);
                        connection.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void sendFriendsAccountData(){
        ArrayList<RBucket<AFriends>> accounts = new ArrayList<>();
        RcbAPI.getInstance().getProxy().getScheduler().runAsync(RcbAPI.getInstance(), () -> {
            String pattern = FriendsProvider.REDIS_KEY + "*";
            PreparedStatement preparedStatement;
            Connection connection = null;
            try {
                connection = DatabaseManager.Main_BDD.getDatabaseAccess().getConnection();
                connection.setAutoCommit(false);
                Iterable<String> keys = RedisAccess.INSTANCE.getRedissonClient().getKeys().getKeysByPattern(pattern);
                preparedStatement = connection.prepareStatement(
                        "INSERT IGNORE INTO Friends (uuid, friend) VALUES (?, ?)");
                for (String key : keys) {
                    RBucket<AFriends> bucket = RedisAccess.INSTANCE.getRedissonClient().getBucket(key);
                    accounts.add(bucket);
                    AFriends account = bucket.get();
                    if (account != null) {
                        PreparedStatement preparedStatement2 = connection.prepareStatement(
                                "DELETE FROM Friends WHERE uuid=?");
                        preparedStatement2.setString(1, account.getUuid().toString());
                        preparedStatement2.executeUpdate();
                        for(UUID uuid : account.getFriends()){
                            preparedStatement.setString(1, account.getUuid().toString());
                            preparedStatement.setString(2, uuid.toString());
                            preparedStatement.addBatch();
                        }
                    }
                }
                if (preparedStatement != null) {
                    preparedStatement.executeBatch();
                    preparedStatement.close();
                    for(RBucket<AFriends> r : accounts){
                        r.deleteAsync();
                    }
                    accounts.clear();
                }
                connection.commit();
            }

            catch (SQLException exception){
                exception.printStackTrace();
                try {
                    if (connection != null) {
                        connection.rollback();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            finally {
                try {
                    if (connection != null) {
                        connection.setAutoCommit(true);
                        connection.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void sendPermissionsData(){
        ArrayList<RBucket<APermissions>> accounts = new ArrayList<>();
        RcbAPI.getInstance().getProxy().getScheduler().runAsync(RcbAPI.getInstance(), () -> {
            String pattern = PermissionsProvider.REDIS_KEY + "*";
            PreparedStatement preparedStatement;
            Connection connection = null;
            try {
                connection = DatabaseManager.Main_BDD.getDatabaseAccess().getConnection();
                connection.setAutoCommit(false);
                Iterable<String> keys = RedisAccess.INSTANCE.getRedissonClient().getKeys().getKeysByPattern(pattern);
                preparedStatement = connection.prepareStatement(
                        "INSERT IGNORE INTO Player_permissions (uuid, permission) VALUES (?, ?)");
                for (String key : keys) {
                    RBucket<APermissions> bucket = RedisAccess.INSTANCE.getRedissonClient().getBucket(key);
                    accounts.add(bucket);
                    APermissions account = bucket.get();
                    if (account != null) {
                        PreparedStatement preparedStatement2 = connection.prepareStatement(
                                "DELETE FROM Player_permissions WHERE uuid=?");
                        preparedStatement2.setString(1, account.getUuid().toString());
                        preparedStatement2.executeUpdate();
                        for(String perm : account.getPermissions()){
                            preparedStatement.setString(1, account.getUuid().toString());
                            preparedStatement.setString(2, perm);
                            preparedStatement.addBatch();
                        }
                    }
                }
                if (preparedStatement != null) {
                    preparedStatement.executeBatch();
                    preparedStatement.close();
                    for(RBucket<APermissions> r : accounts){
                        r.deleteAsync();
                    }
                    accounts.clear();
                }
                connection.commit();
            }
            catch (SQLException exception){
                exception.printStackTrace();
                try {
                    if (connection != null) {
                        connection.rollback();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            finally {
                try {
                    if (connection != null) {
                        connection.setAutoCommit(true);
                        connection.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void sendCosmeticsData(){
        // TODO
    }

    private static void sendStatsData(){
        ArrayList<RBucket<AStats>> accounts = new ArrayList<>();
        RcbAPI.getInstance().getProxy().getScheduler().runAsync(RcbAPI.getInstance(), () -> {
            String pattern = StatsProvider.REDIS_KEY + "*";
            PreparedStatement preparedStatement;
            Connection connection = null;
            try {
                connection = DatabaseManager.Main_BDD.getDatabaseAccess().getConnection();
                connection.setAutoCommit(false);
                Iterable<String> keys = RedisAccess.INSTANCE.getRedissonClient().getKeys().getKeysByPattern(pattern);
                preparedStatement = connection.prepareStatement(
                        "UPDATE Stats SET uuid=?, parcours_timer=?, firstconnection_ms=?, lastconnection_ms=? WHERE uuid=?");
                for (String key : keys) {
                    RBucket<AStats> bucket = RedisAccess.INSTANCE.getRedissonClient().getBucket(key);
                    accounts.add(bucket);
                    AStats account = bucket.get();
                    if (account != null) {
                        preparedStatement.setString(1, account.getUuid().toString());
                        preparedStatement.setLong(2, account.getParcoursTimer());
                        preparedStatement.setLong(3, account.getFirstConnection().getTime());
                        preparedStatement.setLong(4, account.getLastConnection().getTime());
                        preparedStatement.setString(5, account.getUuid().toString());
                        preparedStatement.addBatch();
                    }
                }
                if (preparedStatement != null) {
                    preparedStatement.executeBatch();
                    preparedStatement.close();
                    for(RBucket<AStats> r : accounts){
                        r.deleteAsync();
                    }
                    accounts.clear();
                }
            }
            catch (SQLException exception){
                exception.printStackTrace();
                try {
                    if (connection != null) {
                        connection.rollback();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            finally {
                try {
                    if (connection != null) {
                        connection.setAutoCommit(true);
                        connection.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void sendStatsDacData(){
        ArrayList<RBucket<AStatsDAC>> accounts = new ArrayList<>();
        RcbAPI.getInstance().getProxy().getScheduler().runAsync(RcbAPI.getInstance(), () -> {
            String pattern = StatsDACProvider.REDIS_KEY + "*";
            PreparedStatement preparedStatement;
            Connection connection = null;
            try {
                connection = DatabaseManager.Main_BDD.getDatabaseAccess().getConnection();
                connection.setAutoCommit(false);
                Iterable<String> keys = RedisAccess.INSTANCE.getRedissonClient().getKeys().getKeysByPattern(pattern);
                preparedStatement = connection.prepareStatement(
                        "UPDATE StatsDAC SET uuid=?, wins=?, loses=?, nbParties=?, nbSortsUsed=?, nbJumps=?, nbSuccessJumps=?, nbFailJumps=? WHERE uuid=?");
                for (String key : keys) {
                    RBucket<AStatsDAC> bucket = RedisAccess.INSTANCE.getRedissonClient().getBucket(key);
                    accounts.add(bucket);
                    AStatsDAC account = bucket.get();
                    if (account != null) {
                        preparedStatement.setString(1, account.getUuid().toString());
                        preparedStatement.setInt(2, account.getWins());
                        preparedStatement.setInt(3, account.getLoses());
                        preparedStatement.setInt(4, account.getNbParties());
                        preparedStatement.setInt(5, account.getNbSortsUsed());
                        preparedStatement.setInt(6, account.getNbJumps());
                        preparedStatement.setInt(7, account.getNbSuccessJumps());
                        preparedStatement.setInt(8, account.getNbFailJumps());
                        preparedStatement.setString(9, account.getUuid().toString());
                        preparedStatement.addBatch();
                    }
                }
                if (preparedStatement != null) {
                    preparedStatement.executeBatch();
                    preparedStatement.close();
                    for(RBucket<AStatsDAC> r : accounts){
                        r.deleteAsync();
                    }
                    accounts.clear();
                }
                connection.commit();
            }
            catch (SQLException exception){
                exception.printStackTrace();
                try {
                    if (connection != null) {
                        connection.rollback();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            finally {
                try {
                    if (connection != null) {
                        connection.setAutoCommit(true);
                        connection.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void sendOptionsData(){
        ArrayList<RBucket<AOptions>> accounts = new ArrayList<>();
        RcbAPI.getInstance().getProxy().getScheduler().runAsync(RcbAPI.getInstance(), () -> {
            String pattern = OptionsProvider.REDIS_KEY + "*";
            PreparedStatement preparedStatement;
            Connection connection = null;
            try {
                connection = DatabaseManager.Main_BDD.getDatabaseAccess().getConnection();
                connection.setAutoCommit(false);
                Iterable<String> keys = RedisAccess.INSTANCE.getRedissonClient().getKeys().getKeysByPattern(pattern);
                preparedStatement = connection.prepareStatement(
                        "UPDATE Options SET uuid=?, state_party_invite=?, state_friend_requests=?, state_chat=?, state_friends_statut_notif=?, state_private_msg=? WHERE uuid=?");
                for (String key : keys) {
                    RBucket<AOptions> bucket = RedisAccess.INSTANCE.getRedissonClient().getBucket(key);
                    accounts.add(bucket);
                    AOptions account = bucket.get();
                    if (account != null) {
                        preparedStatement.setString(1, account.getUuid().toString());
                        preparedStatement.setString(2, account.getStatePartyInvite().getName());
                        preparedStatement.setString(3, account.getStateFriendRequests().getName());
                        preparedStatement.setString(4, account.getStateChat().getName());
                        preparedStatement.setString(5, account.getStateFriendsStatutNotif().getName());
                        preparedStatement.setString(6, account.getStateMP().getName());
                        preparedStatement.setString(7, account.getUuid().toString());
                        preparedStatement.addBatch();
                    }
                }
                if (preparedStatement != null) {
                    preparedStatement.executeBatch();
                    preparedStatement.close();
                    for(RBucket<AOptions> r : accounts){
                        r.deleteAsync();
                    }
                    accounts.clear();
                }
                connection.commit();
            }
            catch (SQLException exception){
                exception.printStackTrace();
                try {
                    if (connection != null) {
                        connection.rollback();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            finally {
                try {
                    if (connection != null) {
                        connection.setAutoCommit(true);
                        connection.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
