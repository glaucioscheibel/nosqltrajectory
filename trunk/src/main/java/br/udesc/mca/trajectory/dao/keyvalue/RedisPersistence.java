package br.udesc.mca.trajectory.dao.keyvalue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import redis.clients.jedis.Jedis;
import br.udesc.mca.trajectory.model.Trajectory;

public class RedisPersistence extends KeyValuePersistence {
    private static RedisPersistence instance;
    private Jedis db;

    public static RedisPersistence getInstance() {
        if (instance == null) {
            instance = new RedisPersistence();
        }
        return instance;
    }

    private RedisPersistence() {
        this.log.info("Connecting Redis database");
        this.db = new Jedis("127.0.0.1");
        this.db.connect();
    }

    @Override
    public Trajectory store(Trajectory c) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(c);
            this.db.set(c.getId().toString().getBytes(), baos.toByteArray());
        } catch (Exception e) {
            this.log.error(e.getMessage(), e);
        }
        return c;
    }

    @Override
    public List<Trajectory> findAll() {
        List<Trajectory> lc = new ArrayList<>();
        Set<byte[]> sb = this.db.keys("*".getBytes());
        for (byte[] key : sb) {
            byte[] value = this.db.get(key);
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
        return lc;
    }

    @Override
    public Trajectory findById(UUID id) {
        Trajectory ret = null;
        try {
            byte[] value = this.db.get(id.toString().getBytes());
            if (value != null) {
                ByteArrayInputStream bais = new ByteArrayInputStream(value);
                ObjectInputStream ois = new ObjectInputStream(bais);
                ret = (Trajectory) ois.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            this.log.error(e.getMessage(), e);
        }
        return ret;
    }

    @Override
    public void deleteById(UUID id) {
        this.db.del(id.toString().getBytes());
    }

    @Override
    public void close() {
        this.db.disconnect();
        this.db = null;
    }
}
