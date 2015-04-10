package br.udesc.mca.trajectory.dao;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class PersistenceDAO<E> {
    protected static final String DBNAME = "trajectories";

    protected static Properties persistenceImpl;

    protected Logger log;

    public abstract E store(E e);

    public abstract List<E> findAll();

    public abstract E findById(long id);

    public abstract void deleteById(long id);

    public void close() {};

    public void createDB() {}

    static {
        persistenceImpl = new Properties();
        try (FileReader fr = new FileReader("storage.properties")) {
            persistenceImpl.load(fr);
            System.out.println(persistenceImpl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PersistenceDAO<?> getInstance(PersistenceModel model) {
        try {
            Class<?> persistenceClass = Class.forName(persistenceImpl.getProperty(model.toString().toLowerCase()));
            Method getInstance = persistenceClass.getMethod("getInstance");
            return (PersistenceDAO<?>) getInstance.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PersistenceDAO<?> getInstance(PersistenceModel model, Class<?> type) {
        try {
            Class<?> persistenceClass = Class.forName(persistenceImpl.getProperty(model.toString().toLowerCase()));
            Method getInstance = persistenceClass.getMethod("getInstance", Class.class);
            return (PersistenceDAO<?>) getInstance.invoke(null, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public PersistenceDAO() {
        this.log = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public String toString() {
        return "PersistenceDAO{" + this.getClass().getSimpleName() + '}';
    }

    @Override
    protected void finalize() throws Throwable {
        this.log.info("finalizing " + this.getClass().getSimpleName());
        this.close();
        super.finalize();
    }
}
