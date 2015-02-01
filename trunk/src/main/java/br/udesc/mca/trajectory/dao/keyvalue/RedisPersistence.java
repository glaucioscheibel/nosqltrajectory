package br.udesc.mca.trajectory.dao.keyvalue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import br.udesc.mca.trajectory.model.Trajectory;

public class RedisPersistence extends KeyValuePersistence {
    private static RedisPersistence instance;
    private JedisPool ds;

    public static RedisPersistence getInstance() {
        if (instance == null) {
            instance = new RedisPersistence();
        }
        return instance;
    }

    private RedisPersistence() {
        this.log.info("Connecting Redis database");
        this.ds = new JedisPool("127.0.0.1");
    }

    @Override
    public Trajectory store(Trajectory c) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(c);
            Jedis db = this.ds.getResource();
            db.set(Long.toString(c.getId()).getBytes(), baos.toByteArray());
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
        Set<byte[]> sb = db.keys("*".getBytes());
        for (byte[] key : sb) {
            byte[] value = db.get(key);
            if (value != null) {
                try {
                    ByteArrayInputStream bais = new ByteArrayInputStream(value);
                    ObjectInputStream ois = new ObjectInputStream(bais);
                    lc.add((Trajectory) ois.readObject());
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
            byte[] value = db.get(Long.toString(id).getBytes());
            if (value != null) {
                ByteArrayInputStream bais = new ByteArrayInputStream(value);
                ObjectInputStream ois = new ObjectInputStream(bais);
                ret = (Trajectory) ois.readObject();
            }
            this.ds.returnResource(db);
        } catch (IOException | ClassNotFoundException e) {
            this.log.error(e.getMessage(), e);
        }
        return ret;
    }

    @Override
    public void deleteById(long id) {
        Jedis db = this.ds.getResource();
        db.del(Long.toString(id).getBytes());
        this.ds.returnResource(db);
    }

    @Override
    public void close() {
        this.ds.close();
        this.ds.destroy();
        this.ds = null;
    }
}
