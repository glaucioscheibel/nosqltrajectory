package br.udesc.mca.trajectory.dao.document;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.udesc.mca.trajectory.model.Trajectory;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class MongoPersistence extends DocumentPersistence {

    private static final String colName = "Trajectorys";
    private static MongoPersistence instance;
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
        } catch (UnknownHostException e) {
            this.log.error(e.getMessage(), e);
        }
    }

    @Override
    public Trajectory store(Trajectory c) {
        if (this.log.isInfoEnabled()) {
            this.log.info("Storing " + c);
        }
        /*
        DBCollection dbc = this.db.getCollection(colName);
        DBObject aux = this._findById(c.getId());
        DBObject dbo = new BasicDBObject();
        dbo.put("id", c.getId());
        dbo.put("name", c.getName());
        if (c.getTrajectoryData() != null && c.getTrajectoryData().size() > 0) {
            DBObject data = new BasicDBObject();
            for (TrajectoryData cd : c.getTrajectoryData()) {
                data.put(cd.getDataKey(), cd.getDataValue());
            }
            dbo.put("TrajectoryData", data);
        }
        if (aux == null) {
            dbc.insert(dbo);
        } else {
            dbc.update(aux, dbo);
        }
        */
        return c;
    }

    @Override
    public List<Trajectory> findAll() {
        this.log.info("findAll");
        List<Trajectory> ret = new ArrayList<>();
        DBCollection dbc = this.db.getCollection(colName);
        DBCursor cursor = dbc.find();
        for (DBObject dbo : cursor) {
            ret.add(toTrajectory(dbo));
        }
        return ret;
    }

    private DBObject _findById(UUID id) {
        DBCollection dbc = this.db.getCollection(colName);
        DBObject dbo = new BasicDBObject();
        dbo.put("id", id);
        dbo = dbc.findOne(dbo);
        return dbo;
    }

    @Override
    public Trajectory findById(UUID id) {
        if (this.log.isInfoEnabled()) {
            this.log.info("findById(" + id + ")");
        }
        Trajectory ret = null;
        //ret = toTrajectory(this._findById(id));
        return ret;
    }

    @Override
    public void deleteById(UUID id) {
        if (this.log.isInfoEnabled()) {
            this.log.info("deleteById(" + id + ")");
        }
        DBCollection dbc = this.db.getCollection(colName);
        DBObject dbo = new BasicDBObject();
        dbo.put("id", id);
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

    @SuppressWarnings("unchecked")
    private static Trajectory toTrajectory(DBObject dbo) {
        Trajectory c = null;
        /*
        if (dbo != null) {
            c = new Trajectory();
            c.setId((Integer) dbo.get("id"));
            c.setName((String) dbo.get("name"));
            if (dbo.containsField("TrajectoryData")) {
                DBObject data = (DBObject) dbo.get("TrajectoryData");
                Map<String, String> mss = data.toMap();
                for (String key : mss.keySet()) {
                    c.addTrajectoryData(key, mss.get(key));
                }
            }
        }
        */
        return c;
    }
}
