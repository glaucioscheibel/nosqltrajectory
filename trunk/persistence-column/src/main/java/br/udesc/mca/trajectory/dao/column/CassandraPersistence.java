package br.udesc.mca.trajectory.dao.column;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import br.udesc.mca.trajectory.dao.PersistenceDAO;
import br.udesc.mca.trajectory.model.*;
import com.datastax.driver.core.*;

public class CassandraPersistence extends PersistenceDAO {
    private static final String space = "Trajectory";
    private static final String table = "Trajectory";

    private static CassandraPersistence instance;
    private Cluster cluster;
    private Session db;

    public static CassandraPersistence getInstance() {
        if (instance == null) {
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
        Metadata meta = this.cluster.getMetadata();
        KeyspaceMetadata ks = null;
        TableMetadata tb = null;
        try {
            ks = meta.getKeyspace(space);
        } catch (Exception e) {}

        if (ks != null) {
            try {
                tb = ks.getTable(table);
            } catch (Exception e) {}
        } else {
            this.db.execute("CREATE KEYSPACE " + space + " WITH replication = {'class':'SimpleStrategy',"
                    + " 'replication_factor':3};");
        }
        this.db.execute("use " +space);

        if (tb == null) {
            this.db.execute("CREATE TYPE data (id int, key text, value text)");
           this.db.execute("CREATE TYPE user (id int, name text)");
            this.db.execute("CREATE TYPE point (id int, h float, lat float, lng float, timestamp bigint, data list<frozen<data>>)");
            this.db.execute("CREATE TYPE segment (transportationMode text, points list<frozen<point>>, data list<frozen<data>>)");
            this.db.execute("CREATE TYPE process (componentId int, executionDuration bigint, executionTime bigint)");
            this.db.execute("CREATE TYPE version (id int, lastModified timestamp, previousVersion int, version int, user map<frozen<user>>, segments list<frozen<segment>>, process list<frozen<segment>>)");

            this.db.execute("create table " + table + " (id bigint primary key, lastModified timestamp, description text, originalTrajectory int, versions list<frozen<version>>)");

        }
    }

    @Override
    public List<Trajectory> findAll() {
        List<Trajectory> ret = new ArrayList<>();
        try {
            ResultSet rs = this.db.execute("select * from " + table);
            for (Row row : rs) {
                ret.add(readRow(row));
            }
        } catch (Exception e) {
            this.log.error(e.getMessage(), e);
        }
        return ret;
    }

    @Override
    public Trajectory findById(long id) {
        Trajectory ret = null;
        try {
            PreparedStatement ps = this.db.prepare("select * from " + table + " where id=?");
            BoundStatement bs = new BoundStatement(ps);
            bs.setLong(0, id);
            ResultSet rs = this.db.execute(bs);
            Row row = rs.one();
            if (row != null) {
                ret = readRow(row);
            }

        } catch (Exception e) {
            this.log.error(e.getMessage(), e);
        }
        return ret;
    }

    @Override
    public void deleteById(long id) {
        try {
            PreparedStatement ps = this.db.prepare("delete from " + table + " where id=?");
            BoundStatement bs = new BoundStatement(ps);
            bs.setLong(0, id);
            this.db.execute(bs);
        } catch (Exception e) {
            this.log.error(e.getMessage(), e);
        }
    }

    @Override
    public void close() {
        this.db.execute("drop schema " + space);
        this.db.close();
        this.db = null;
        this.cluster.close();
        this.cluster = null;
    }

    @Override
    public Trajectory store(Trajectory o) {
        StringBuilder sb = new StringBuilder();

        Trajectory t = this.findById(o.getId());
        if(t == null) {
            //sb.append("insert into ").append(table).append("(id,lastModified,description,originalTrajectory,versions) values(?,?,?,?,[{id:1,version:1},{id:2,version:2}])");
            sb.append("insert into ").append(table).append("(id,lastModified,description,originalTrajectory,versions) values(?,?,?,?,?)");
            PreparedStatement st = this.db.prepare(sb.toString());
            BoundStatement bs = st.bind();
            bs.setLong("id", o.getId());
            bs.setTimestamp("lastModified", o.getLastModified());
            bs.setString("description", o.getDescription());
            bs.setInt("originalTrajectory", o.getOriginalTrajectory());

            ArrayList<UDTValue> versions = new ArrayList<>();
            UserType ut = db.getCluster()
                    .getMetadata().getKeyspace("Trajectory").getUserType("version");

            for(TrajectoryVersion tv:o.getVersions()) {
                UDTValue version = ut.newValue();
                version.setInt("id", tv.getId());
                version.setInt("version", tv.getVersion());

                versions.add(version);
            }

                bs.setList("versions", versions);

            this.db.execute(bs);

        } else {
            sb.append("update ").append(table).append("");
        }
        return o;
    }

    private Trajectory readRow(Row row) throws Exception {
        Trajectory ret = new Trajectory();
        for (ColumnDefinitions.Definition def : row.getColumnDefinitions().asList()) {
            ret.setId(row.getLong("id"));
            ret.setDescription(row.getString("description"));
            ret.setOriginalTrajectory(row.getInt("originalTrajectory"));
            ret.setLastModified(row.getTimestamp("lastModified"));
            List<UDTValue> versions = row.getList("versions", UDTValue.class);
            for(UDTValue version:versions){
                TrajectoryVersion v = new TrajectoryVersion();
                v.setId(version.getInt("id"));
                v.setPreviousVersion(version.getInt("previousVersion"));
                v.setVersion(version.getInt("version"));
                v.setLastModified(version.getTimestamp("lastModified"));
                //id int, lastModified timestamp, previousVersion int, version int, user map<text, frozen<user>>, segments set<frozen<segment>>, process map<text, frozen<segment>>
                ret.addVersion(v);
            }
        }

        return ret;
    }

    public  static void main(String... a){
        CassandraPersistence p = CassandraPersistence.getInstance();
        try {

            p.createDB();

            Trajectory t = new Trajectory();

            t.setId(2l);
            t.setDescription("desc");
            t.setLastModified(new Date());
            t.setOriginalTrajectory(0);


            TrajectoryVersion v = new TrajectoryVersion();
            v.setId(1);
            v.setVersion(1);
            t.addVersion(v);
            p.store(t);


            System.out.println(p.findById(2).getDescription());
            System.out.println(p.findById(2).getVersions().get(0).getId());


        }catch (Exception e){
            e.printStackTrace();
        } finally {
            p.close();
        }
    }
}
