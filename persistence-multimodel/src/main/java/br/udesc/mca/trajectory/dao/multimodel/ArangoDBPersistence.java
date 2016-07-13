package br.udesc.mca.trajectory.dao.multimodel;

import java.util.List;
import com.arangodb.ArangoConfigure;
import com.arangodb.ArangoDriver;
import com.arangodb.ArangoException;
import br.udesc.mca.trajectory.model.Trajectory;

public class ArangoDBPersistence extends MultiModelPersistence {
    private static final String COLLECTION = "Trajectories";
    private static ArangoDBPersistence instance;
    private ArangoConfigure config;
    private ArangoDriver db;

    public static ArangoDBPersistence getInstance() {
        if (instance == null) {
            instance = new ArangoDBPersistence();
        }
        return instance;
    }

    private ArangoDBPersistence() {
        this.config = new ArangoConfigure();
        this.config.init();
        this.db = new ArangoDriver(this.config);
    }

    @Override
    public Trajectory store(Trajectory t) {
        if (this.log.isInfoEnabled()) {
            this.log.info("Storing " + t);
        }
        return null;
    }

    @Override
    public List<Trajectory> findAll() {
        this.log.info("findAll");
        return null;
    }

    @Override
    public Trajectory findById(long id) {
        if (this.log.isInfoEnabled()) {
            this.log.info("findById(" + id + ")");
        }
        return null;
    }

    @Override
    public void deleteById(long id) {
        if (this.log.isInfoEnabled()) {
            this.log.info("deleteById(" + id + ")");
        }
    }

    @Override
    public void createDB() {
        try {
            this.db.createDatabase(DBNAME);
            this.db.createCollection(COLLECTION);
        } catch (ArangoException e) {
            this.log.error(e.getMessage(), e);
        }
    }

    @Override
    public void close() {
        this.db = null;
        this.config.shutdown();
        this.config = null;
    }
}
