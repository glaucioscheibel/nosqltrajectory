package br.udesc.mca.sec1.projeto.dao.document;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.udesc.mca.sec1.projeto.model.Customer;
import br.udesc.mca.sec1.projeto.model.CustomerData;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class MongoPersistence extends DocumentPersistence {

    private static final String colName = "Customers";
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
            this.db = this.mongo.getDB("mda");
        } catch (UnknownHostException e) {
            this.log.error(e.getMessage(), e);
        }
    }

    @Override
    public Customer store(Customer c) {
        if (this.log.isInfoEnabled()) {
            this.log.info("Storing " + c);
        }
        DBCollection dbc = this.db.getCollection(colName);
        DBObject aux = this._findById(c.getId());
        DBObject dbo = new BasicDBObject();
        dbo.put("id", c.getId());
        dbo.put("name", c.getName());
        if (c.getCustomerData() != null && c.getCustomerData().size() > 0) {
            DBObject data = new BasicDBObject();
            for (CustomerData cd : c.getCustomerData()) {
                data.put(cd.getDataKey(), cd.getDataValue());
            }
            dbo.put("customerData", data);
        }
        if (aux == null) {
            dbc.insert(dbo);
        } else {
            dbc.update(aux, dbo);
        }
        return c;
    }

    @Override
    public List<Customer> findAll() {
        this.log.info("findAll");
        List<Customer> ret = new ArrayList<>();
        DBCollection dbc = this.db.getCollection(colName);
        DBCursor cursor = dbc.find();
        for (DBObject dbo : cursor) {
            ret.add(toCustomer(dbo));
        }
        return ret;
    }

    private DBObject _findById(Integer id) {
        DBCollection dbc = this.db.getCollection(colName);
        DBObject dbo = new BasicDBObject();
        dbo.put("id", id);
        dbo = dbc.findOne(dbo);
        return dbo;
    }

    @Override
    public Customer findById(Integer id) {
        if (this.log.isInfoEnabled()) {
            this.log.info("findById(" + id + ")");
        }
        Customer ret;
        ret = toCustomer(this._findById(id));
        return ret;
    }

    @Override
    public void deleteById(Integer id) {
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
    private static Customer toCustomer(DBObject dbo) {
        Customer c = null;
        if (dbo != null) {
            c = new Customer();
            c.setId((Integer) dbo.get("id"));
            c.setName((String) dbo.get("name"));
            if (dbo.containsField("customerData")) {
                DBObject data = (DBObject) dbo.get("customerData");
                Map<String, String> mss = data.toMap();
                for (String key : mss.keySet()) {
                    c.addCustomerData(key, mss.get(key));
                }
            }
        }
        return c;
    }
}
