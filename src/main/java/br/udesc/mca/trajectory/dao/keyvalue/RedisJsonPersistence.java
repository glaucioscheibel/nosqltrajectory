package br.udesc.mca.trajectory.dao.keyvalue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import br.udesc.mca.trajectory.model.Trajectory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RedisJsonPersistence extends KeyValuePersistence {
    private static RedisJsonPersistence instance;
    private JedisPool ds;
    private ObjectMapper om;

    public static RedisJsonPersistence getInstance() {
        if (instance == null) {
            instance = new RedisJsonPersistence();
        }
        return instance;
    }

    private RedisJsonPersistence() {
        this.log.info("Connecting Redis database");
        this.ds = new JedisPool("127.0.0.1");
        this.om = new ObjectMapper();
    }

    @Override
    public Trajectory store(Trajectory c) {
        try {
            Jedis db = this.ds.getResource();
            String json = this.om.writeValueAsString(c);
            db.set(String.valueOf(c.getId()), json);
            this.ds.returnResource(db);
        } catch (Exception e) {
            this.log.error(e.getMessage(), e);
        }
        return c;
    }

    @Override
    public List<Trajectory> findAll() {
        Jedis db = this.ds.getResource();
        List<Trajectory> lc = new ArrayList<>();
        Set<String> keys = db.keys("*");
        for (String key : keys) {
            String value = db.get(key);
            if (value != null) {
                try {
                    lc.add(this.om.readValue(value, Trajectory.class));
                } catch (Exception e) {
                    this.log.error(e.getMessage(), e);
                }
            }
        }
        this.ds.returnResource(db);
        return lc;
    }

    @Override
    public Trajectory findById(long id) {
        Trajectory ret = null;
        try {
            Jedis db = this.ds.getResource();
            String json = db.get(String.valueOf(id));
            if (json != null) {
                ret = this.om.readValue(json, Trajectory.class);
            }
            this.ds.returnResource(db);
        } catch (IOException e) {
            this.log.error(e.getMessage(), e);
        }
        return ret;
    }

    @Override
    public void deleteById(long id) {
        Jedis db = this.ds.getResource();
        db.del(String.valueOf(id));
        this.ds.returnResource(db);
    }

    @Override
    public void close() {
        this.ds.close();
        this.ds.destroy();
        this.ds = null;
    }
}
