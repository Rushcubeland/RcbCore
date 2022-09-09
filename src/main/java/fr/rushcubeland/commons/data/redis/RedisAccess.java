package fr.rushcubeland.commons.data.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.config.TransportMode;

public class RedisAccess {

    public static RedisAccess INSTANCE;

    private final RedissonClient redissonClient;

    public RedisAccess(RedisCredentials redisCredentials) {
        INSTANCE = this;
        this.redissonClient = initRedisson(redisCredentials);
        System.out.println("[RcbCore] Connection established with Redis Server");
    }

    public static void init(){
        new RedisAccess(new RedisCredentials("192.168.1.2", "********", 6379));
    }

    public static void close(){
        RedisAccess.INSTANCE.getRedissonClient().shutdown();
    }

    public RedissonClient initRedisson(RedisCredentials redisCredentials){
        final Config config = new Config();

        config.setCodec(new JsonJacksonCodec());
        config.setTransportMode(TransportMode.EPOLL);
        config.setThreads(2);
        config.setNettyThreads(2);
        config.useSingleServer()
                .setAddress(redisCredentials.toRedisURL())
                .setPassword(redisCredentials.getPassword())
                .setDatabase(3)
                .setClientName(redisCredentials.getClientName());

        return Redisson.create(config);

    }

    public RedissonClient getRedissonClient() {
        return redissonClient;
    }
}
