package br.udesc.mca.trajectory.dao.document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.util.JSON;
import br.udesc.mca.trajectory.model.Trajectory;

public class MongoPersistence extends DocumentPersistence {

    private static final String colName = "Trajectories";
    private static MongoPersistence instance;
    private MongoClient mongo;
    private MongoDatabase db;
    private ObjectMapper om;

    public static MongoPersistence getInstance() {
        if (instance == null) {
            instance = new MongoPersistence();
        }
        return instance;
    }

    private MongoPersistence() {
        this.mongo = new MongoClient();
        this.db = this.mongo.getDatabase(DBNAME);
        this.om = new ObjectMapper();
    }

    @Override
    public Trajectory store(Trajectory c) {
        if (this.log.isInfoEnabled()) {
            this.log.info("Storing " + c);
        }

        try {
            String json = this.om.writeValueAsString(c);
            MongoCollection<Document> dbc = this.db.getCollection(colName);
            Document aux = dbc.find(Filters.eq("_id", c.getId())).first();
            if (aux == null) {
                dbc.insertOne(new Document((BasicDBObject) JSON.parse(json)));
            } else {
                dbc.updateOne(aux, new Document((BasicDBObject) JSON.parse(json)));
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
            MongoCollection<Document> dbc = this.db.getCollection(colName);
            for (Document d : dbc.find()) {
                ret.add(this.om.readValue(JSON.serialize(d), Trajectory.class));
            }
        } catch (Exception e) {
            this.log.error(e.getMessage(), e);
        }
        return ret;
    }

    @Override
    public Trajectory findById(long id) {
        if (this.log.isInfoEnabled()) {
            this.log.info("findById(" + id + ")");
        }

        MongoCollection<Document> dbc = this.db.getCollection(colName);
        FindIterable<Document> result = dbc.find(Filters.eq("_id", id));

        Document first = result.first();
        if (first != null) {
            try {
                return this.om.readValue(JSON.serialize(first), Trajectory.class);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        return null;
    }

    @Override
    public void deleteById(long id) {
        if (this.log.isInfoEnabled()) {
            this.log.info("deleteById(" + id + ")");
        }
        MongoCollection<Document> dbc = this.db.getCollection(colName);
        dbc.deleteOne(Filters.eq("_id", id));
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
