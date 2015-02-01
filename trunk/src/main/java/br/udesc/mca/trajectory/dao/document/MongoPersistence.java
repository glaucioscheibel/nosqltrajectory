package br.udesc.mca.trajectory.dao.document;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import br.udesc.mca.trajectory.model.Trajectory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

public class MongoPersistence extends DocumentPersistence {

    private static final String colName = "Trajectories";
    private static MongoPersistence instance;
    private ObjectMapper om;
    private MongoClient mongo;
    private DB db;

    public static MongoPersistence getInstance() {
        if (instance == null) {
            instance = new MongoPersistence();
        }
        return instance;
    }

    private MongoPersistence() {
        try {
            this.mongo = new MongoClient();
            this.db = this.mongo.getDB(DBNAME);
            this.om = new ObjectMapper();
        } catch (UnknownHostException e) {
            this.log.error(e.getMessage(), e);
        }
    }

    @Override
    public Trajectory store(Trajectory c) {
        if (this.log.isInfoEnabled()) {
            this.log.info("Storing " + c);
        }
        try {
            String json = this.om.writeValueAsString(c);
            DBCollection dbc = this.db.getCollection(colName);
            DBObject aux = this._findById(c.getId());
            DBObject dbo = (DBObject) JSON.parse(json);
            if (aux == null) {
                dbc.insert(dbo);
            } else {
                dbc.update(aux, dbo);
            }
        } catch (Exception e) {
            this.log.error(e.getMessage(), e);
        }
        return c;
    }

    @Override
    public List<Trajectory> findAll() {
        this.log.info("findAll");
        List<Trajectory> ret = new ArrayList<>();
        try {
            DBCollection dbc = this.db.getCollection(colName);
            DBCursor cursor = dbc.find();
            for (DBObject dbo : cursor) {
                ret.add(this.om.readValue(dbo.toString(), Trajectory.class));
            }
        } catch (Exception e) {
            this.log.error(e.getMessage(), e);
        }
        return ret;
    }

    private DBObject _findById(long id) {
        DBCollection dbc = this.db.getCollection(colName);
        DBObject dbo = new BasicDBObject();
        dbo.put("_id", id);
        dbo = dbc.findOne(dbo);
        return dbo;
    }

    @Override
    public Trajectory findById(long id) {
        if (this.log.isInfoEnabled()) {
            this.log.info("findById(" + id + ")");
        }
        Trajectory ret = null;
        try {
            DBObject dbo = this._findById(id);
            if (dbo != null) {
                ret = this.om.readValue(dbo.toString(), Trajectory.class);
            }
        } catch (Exception e) {
            this.log.error(e.getMessage(), e);
        }
        return ret;
    }

    @Override
    public void deleteById(long id) {
        if (this.log.isInfoEnabled()) {
            this.log.info("deleteById(" + id + ")");
        }
        DBCollection dbc = this.db.getCollection(colName);
        DBObject dbo = new BasicDBObject();
        dbo.put("_id", id);
        dbc.findAndRemove(dbo);
    }

    @Override
    public void close() {
        if (this.mongo != null) {
            this.db = null;
            this.mongo.close();
            this.mongo = null;
        }
    }
}
