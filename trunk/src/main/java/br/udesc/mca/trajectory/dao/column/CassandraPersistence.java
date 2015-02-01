package br.udesc.mca.trajectory.dao.column;

import java.util.ArrayList;
import java.util.List;

import br.udesc.mca.trajectory.model.Trajectory;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class CassandraPersistence extends ColumnPersistence {
    // private static final String KEYSPACE = "dba";
    private static final String COLUMN_FAMILY = "dba.customer";
    private static CassandraPersistence instance;
    // private static int dataid = 1;
    private Cluster cluster;
    private Session db;

    public static CassandraPersistence getInstance() {
        if (instance == null) {
            System.out.println(CassandraPersistence.class);
            instance = new CassandraPersistence();
        }
        return instance;
    }

    private CassandraPersistence() {
        this.cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
        Metadata metadata = this.cluster.getMetadata();
        System.out.printf("Connected to cluster: %s\n", metadata.getClusterName());
        for (Host host : metadata.getAllHosts()) {
            System.out.printf("Datacenter: %s; Host: %s; Rack: %s\n", host.getDatacenter(), host.getAddress(),
                    host.getRack());
        }
        this.db = this.cluster.connect();
    }

    @Override
    public void createDB() {
        try {
            this.db.execute("CREATE KEYSPACE dba WITH replication = {'class':'SimpleStrategy',"
                    + " 'replication_factor':3};");
            this.db.execute("create table dba.customer (id int primary key, name text);");
            this.db.execute("create table dba.customerdata (id int primary key, customerid int, key text, value text);");
            this.db.execute("create index customerid on dba.customerdata (customerid)");
        } catch (Exception e) {
            this.log.error(e.getMessage(), e);
        }
    }

    @Override
    public Trajectory store(Trajectory c) {
        /*
         * try { PreparedStatement ps =
         * this.db.prepare("insert into dba.customer(id, name) values(?, ?)");
         * BoundStatement bs = new BoundStatement(ps); bs.setInt(0, c.getId());
         * bs.setString(1, c.getName()); this.db.execute(bs); List<CustomerData>
         * lcd = c.getCustomerData(); if (lcd != null && !lcd.isEmpty()) { ps =
         * this.db.prepare(
         * "insert into dba.customerdata(id, customerid, key, value) values(?, ?, ?, ?)"
         * ); bs = new BoundStatement(ps); for (CustomerData cd : lcd) {
         * bs.setInt(0, dataid++); bs.setInt(1, c.getId()); bs.setString(2,
         * cd.getDataKey()); bs.setString(3, cd.getDataValue());
         * this.db.execute(bs); } } } catch (Exception e) {
         * this.log.error(e.getMessage(), e); }
         */
        return null;
    }

    @Override
    public List<Trajectory> findAll() {
        List<Trajectory> ret = new ArrayList<>();
        try {
            ResultSet rs = this.db.execute("select id, name from dba.customer");
            for (Row row : rs) {}
        } catch (Exception e) {
            this.log.error(e.getMessage(), e);
        }
        return ret;
    }

    @Override
    public Trajectory findById(long id) {
        Trajectory ret = null;
        try {
            PreparedStatement ps = this.db.prepare("select id, name from dba.customer where id=?");
            BoundStatement bs = new BoundStatement(ps);
            // bs.setInt(0, id);
            ResultSet rs = this.db.execute(bs);
            Row row = rs.one();
            if (row != null) {
                // ret = new Customer(row.getInt(0), row.getString(1));
                ps = this.db.prepare("select key, value from dba.customerdata where customerid=?");
                bs = new BoundStatement(ps);
                // bs.setInt(0, id);
                rs = this.db.execute(bs);
                for (Row rd : rs) {
                    // ret.addCustomerData(rd.getString(0), rd.getString(1));
                }
            }

        } catch (Exception e) {
            this.log.error(e.getMessage(), e);
        }
        return ret;
    }

    @Override
    public void deleteById(long id) {
        try {
            PreparedStatement ps = this.db.prepare("delete from " + COLUMN_FAMILY + " where id=?");
            BoundStatement bs = new BoundStatement(ps);
            bs.setLong(0, id);
            this.db.execute(bs);
        } catch (Exception e) {
            this.log.error(e.getMessage(), e);
        }
    }

    @Override
    public void close() {
        this.db.execute("drop schema dba");
        this.db.close();
        this.db = null;
        this.cluster.close();
        this.cluster = null;
    }
}
