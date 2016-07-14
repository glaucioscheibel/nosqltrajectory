package br.udesc.mca.trajectory.dao.multimodel;

import java.util.List;
import com.arangodb.ArangoConfigure;
import com.arangodb.ArangoDriver;
import com.arangodb.ArangoException;
import com.arangodb.entity.DocumentEntity;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import br.udesc.mca.trajectory.model.Trajectory;

public class ArangoDBPersistence extends MultiModelPersistence {
    private static final String COLLECTION = "Trajectories";
    private static ArangoDBPersistence instance;
    private ArangoConfigure config;
    private ArangoDriver db;
    private ObjectMapper om;

    public static ArangoDBPersistence getInstance() {
        if (instance == null) {
            instance = new ArangoDBPersistence();
        }
        return instance;
    }

    private ArangoDBPersistence() {
        this.om = new ObjectMapper();
        this.om.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        this.om.setSerializationInclusion(Include.NON_NULL);
        this.om.setSerializationInclusion(Include.NON_EMPTY);
        this.config = new ArangoConfigure();
        this.config.init();
        this.config.setUser("root");
        this.config.setPassword("root");
        this.config.setDefaultDatabase(DBNAME);
        this.db = new ArangoDriver(this.config);
        this.createDB();
    }

    @Override
    public Trajectory store(Trajectory t) {
        if (this.log.isInfoEnabled()) {
            this.log.info("Storing " + t);
        }
        try {
            String json = this.om.writeValueAsString(t);
            DocumentEntity<String> de = this.db.createDocumentRaw(COLLECTION, json, true);
            t.setId(Long.valueOf(de.getDocumentKey()));
            return t;
        } catch (ArangoException | JsonProcessingException e) {
            this.log.error(e.getMessage(), e);
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
        } catch (ArangoException e) {
            this.log.error(e.getMessage(), e);
        }
        try {
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
