package br.udesc.mca.sec1.projeto.dao.keyvalue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import redis.clients.jedis.Jedis;
import br.udesc.mca.sec1.projeto.model.Customer;

import com.google.common.primitives.Ints;

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
    public Customer store(Customer c) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(c);
            this.db.set(Ints.toByteArray(c.getId()), baos.toByteArray());
        } catch (Exception e) {
            this.log.error(e.getMessage(), e);
        }
        return c;
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> lc = new ArrayList<>();
        Set<byte[]> sb = this.db.keys("*".getBytes());
        for (byte[] key : sb) {
            byte[] value = this.db.get(key);
            if (value != null) {
                try {
                    ByteArrayInputStream bais = new ByteArrayInputStream(value);
                    ObjectInputStream ois = new ObjectInputStream(bais);
                    lc.add((Customer) ois.readObject());
                } catch (Exception e) {
                    this.log.error(e.getMessage(), e);
                }
            }
        }
        return lc;
    }

    @Override
    public Customer findById(Integer id) {
        Customer ret = null;
        try {
            byte[] value = this.db.get(Ints.toByteArray(id));
            if (value != null) {
                ByteArrayInputStream bais = new ByteArrayInputStream(value);
                ObjectInputStream ois = new ObjectInputStream(bais);
                ret = (Customer) ois.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            this.log.error(e.getMessage(), e);
        }
        return ret;
    }

    @Override
    public void deleteById(Integer id) {
        this.db.del(Ints.toByteArray(id));
    }

    @Override
    public void close() {
        this.db.disconnect();
        this.db = null;
    }
}
