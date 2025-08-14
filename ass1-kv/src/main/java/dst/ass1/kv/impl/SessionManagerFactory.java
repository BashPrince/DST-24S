package dst.ass1.kv.impl;

import dst.ass1.kv.ISessionManager;
import dst.ass1.kv.ISessionManagerFactory;
import redis.clients.jedis.Jedis;

import java.util.Properties;

public class SessionManagerFactory implements ISessionManagerFactory {

    @Override
    public ISessionManager createSessionManager(Properties properties) {
        // read "redis.host" and "redis.port" from the properties

        Jedis jedis = new Jedis(properties.getProperty("redis.host"), Integer.parseInt(properties.getProperty("redis.port")));

        return new SessionManager(jedis);
    }
}
