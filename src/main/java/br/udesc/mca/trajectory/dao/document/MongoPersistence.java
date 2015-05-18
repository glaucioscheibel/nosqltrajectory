package br.udesc.mca.trajectory.dao.document;

import java.util.ArrayList;
import java.util.List;

import br.udesc.mca.trajectory.model.Trajectory;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.BasicBSONObject;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;

public class MongoPersistence extends DocumentPersistence {

    private static final String colName = "Trajectories";
    private static MongoPersistence instance;
    private MongoClient mongo;
    private MongoDatabase db;

    public static MongoPersistence getInstance() {
        if (instance == null) {
            instance = new MongoPersistence();
        }
        return instance;
    }

    private MongoPersistence() {
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
                MongoClient.getDefaultCodecRegistry(),
                CodecRegistries.fromCodecs(new TrajectoryCodec()));

        MongoClientOptions options =
                MongoClientOptions.builder()
                        .codecRegistry(codecRegistry)
                        .build();

        this.mongo = new MongoClient("localhost:27017", options);
        this.db = this.mongo.getDatabase(DBNAME);
    }

    @Override
    public Trajectory store(Trajectory c) {
        if (this.log.isInfoEnabled()) {
            this.log.info("Storing " + c);
        }
        try {
            MongoCollection<Trajectory> dbc = this.db.getCollection(colName, Trajectory.class);
            Trajectory aux = this.findById(c.getId());
            if(aux == null) {
                dbc.insertOne(c);
            } else {
                dbc.updateOne(Filters.eq("_id", c.getId()), new Document("$set", c));
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
            MongoCollection<Trajectory> dbc = this.db.getCollection(colName, Trajectory.class);
            for (Trajectory trajectory : dbc.find()) {
                ret.add(trajectory);
            }
        } catch (Exception e) {
            this.log.error(e.getMessage(), e);
        }
        return ret;
    }

    public Trajectory findById(long id) {
        if (this.log.isInfoEnabled()) {
            this.log.info("findById(" + id + ")");
        }

        MongoCollection<Trajectory> dbc = this.db.getCollection(colName, Trajectory.class);

        BasicBSONObject dbo = new BasicBSONObject();
        dbo.put("_id", id);

        FindIterable<Trajectory>  result = dbc.find(Filters.eq("_id", id));

        return result.first();
    }

    @Override
    public void deleteById(long id) {
        if (this.log.isInfoEnabled()) {
            this.log.info("deleteById(" + id + ")");
        }
        MongoCollection<Trajectory> dbc = this.db.getCollection(colName, Trajectory.class);
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
