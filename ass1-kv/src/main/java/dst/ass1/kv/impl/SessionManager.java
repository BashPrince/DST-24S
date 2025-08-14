package dst.ass1.kv.impl;

import dst.ass1.kv.ISessionManager;
import dst.ass1.kv.SessionCreationFailedException;
import dst.ass1.kv.SessionNotFoundException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.exceptions.JedisException;

import java.util.Map;
import java.util.UUID;

public class SessionManager implements ISessionManager {
    private final Jedis jedis;
    public SessionManager(Jedis jedis) {
        this.jedis = jedis;
    }

    private Map<String, String> getSessionMap(String sessionId) throws SessionNotFoundException {
        Map<String, String> map = jedis.hgetAll(sessionId);

        // Empty map indicates session does not exist
        if (map.isEmpty()) {
            throw new SessionNotFoundException();
        }

        return map;
    }

    @Override
    public String createSession(Long userId, int timeToLive) throws SessionCreationFailedException {
        String sessionId = UUID.randomUUID().toString();

        try {
            Transaction t = jedis.multi();
            String userIdStr = userId.toString();
            // Create a hash data structure from sessionId to session values
            t.hmset(sessionId, Map.of("userId", userIdStr, "timeToLive", String.valueOf(timeToLive)));
            // Reverse mapping from userId to sessionId
            t.set(userIdStr, sessionId);
            // This does not guarantee simultaneous expiry
            t.expire(sessionId, timeToLive);
            t.expire(userIdStr, timeToLive);
            t.exec();
        } catch (JedisException e) {
            throw new SessionCreationFailedException(e);
        }

        return sessionId;
    }

    @Override
    public void setSessionVariable(String sessionId, String key, String value) throws SessionNotFoundException {
        jedis.watch(sessionId);
        if (!jedis.exists(sessionId)) {
            jedis.unwatch();

            throw new SessionNotFoundException();
        }

        Transaction t = jedis.multi();
        t.hset(sessionId, key, value);
        t.exec();
    }

    @Override
    public String getSessionVariable(String sessionId, String key) throws SessionNotFoundException {
        return getSessionMap(sessionId).getOrDefault(key, null);
    }

    @Override
    public Long getUserId(String sessionId) throws SessionNotFoundException {
        return Long.parseLong(getSessionVariable(sessionId,"userId"));
    }

    @Override
    public int getTimeToLive(String sessionId) throws SessionNotFoundException {
        return Integer.parseInt(getSessionVariable(sessionId,"timeToLive"));

    }

    @Override
    public String requireSession(Long userId, int timeToLive) throws SessionCreationFailedException {
        String userIdStr = userId.toString();
        jedis.watch(userIdStr);
        // Check if a session for the given userId exists
        String sessionId = jedis.get(userIdStr);

        if (sessionId != null) {
            jedis.unwatch();

            return sessionId;
        }

        return createSession(userId, timeToLive);
    }

    @Override
    public void close() {
        jedis.close();
    }
}
