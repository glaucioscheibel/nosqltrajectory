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
            this.db.execute("CREATE TYPE process (componentId int, executionDuration bigint, executionTime timestamp)");
            this.db.execute("CREATE TYPE version (id int, type text, lastModified timestamp, previousVersion int, version int, user frozen<user>, segments list<frozen<segment>>, process frozen<segment>, data list<frozen<data>>)");

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
            sb.append("insert into ").append(table).append("(id,lastModified,description,originalTrajectory,versions) values(?,?,?,?,?)");
            PreparedStatement st = this.db.prepare(sb.toString());
            BoundStatement bs = st.bind();
            fillBoundStatement(bs, o);

            this.db.execute(bs);
        } else {
            sb.append("update ").append(table).append(" set lastModified=?, description=?, originalTrajectory=?, versions=? where id=?");
            PreparedStatement st = this.db.prepare(sb.toString());
            BoundStatement bs = st.bind();
            fillBoundStatement(bs, o);

            this.db.execute(bs);
        }
        return o;
    }

    private void fillBoundStatement(BoundStatement bs, Trajectory trajectory){
        bs.setLong("id", trajectory.getId());
        bs.setTimestamp("lastModified", trajectory.getLastModified());
        bs.setString("description", trajectory.getDescription());
        bs.setInt("originalTrajectory", trajectory.getOriginalTrajectory());

        if(trajectory.getVersions() != null) {
            ArrayList<UDTValue> versions = new ArrayList<>();
            UserType versionUT = db.getCluster()
                    .getMetadata().getKeyspace("Trajectory").getUserType("version");
            UserType dataUT = db.getCluster()
                    .getMetadata().getKeyspace("Trajectory").getUserType("data");
            UserType userUT = db.getCluster()
                    .getMetadata().getKeyspace("Trajectory").getUserType("user");
            UserType pointUT = db.getCluster()
                    .getMetadata().getKeyspace("Trajectory").getUserType("point");
            UserType segmentUT = db.getCluster()
                    .getMetadata().getKeyspace("Trajectory").getUserType("segment");
            UserType processUT = db.getCluster()
                    .getMetadata().getKeyspace("Trajectory").getUserType("process");

            for (TrajectoryVersion tv : trajectory.getVersions()) {
                UDTValue version = versionUT.newValue();
                version.setInt("id", tv.getId());
                version.setInt("version", tv.getVersion());
                version.setString("type", tv.getType() == null ? null : tv.getType().name());
                version.setInt("previousVersion", tv.getPreviousVersion() == null ? -1 : tv.getPreviousVersion());
                if(tv.getUser() != null){
                    UDTValue user = userUT.newValue();
                    user.setInt("id", tv.getUser().getId() == null ? -1 : tv.getUser().getId());
                    user.setString("name", tv.getUser().getName());
                    version.setUDTValue("user", user);
                }
                if(tv.getProcess() != null){
                    UDTValue process = processUT.newValue();
                    process.setTimestamp("executionTime", tv.getProcess().getExecutionTime());
                    process.setLong("executionDuration", tv.getProcess().getExecutionDuration());
                    process.setInt("componentId", tv.getProcess().getComponentId());
                    version.setUDTValue("process", process);
                }
                if(tv.getData() != null){
                    ArrayList<UDTValue> datas = new ArrayList<>();
                    for (TrajectoryVersionData d: tv.getData()) {
                        UDTValue data = dataUT.newValue();
                        data.setInt("id", d.getId() == null ? -1 : d.getId());
                        data.setString("key", d.getKey());
                        data.setString("value", d.getValue());
                        datas.add(data);
                    }
                    version.setList("data", datas);
                }
                if(tv.getSegments() != null){
                    ArrayList<UDTValue> segments = new ArrayList<>();
                    for (TrajectorySegment s: tv.getSegments()) {
                        UDTValue segment = segmentUT.newValue();
                        segment.setString("transportationMode", s.getTransportationMode() == null? null: s.getTransportationMode().name());
                        if(s.getData() != null){
                            ArrayList<UDTValue> datas = new ArrayList<>();
                            for (TrajectorySegmentData d: s.getData()) {
                                UDTValue data = dataUT.newValue();
                                data.setInt("id", d.getId() == null ? -1 : d.getId());
                                data.setString("key", d.getKey());
                                data.setString("value", d.getValue());
                                datas.add(data);
                            }
                            segment.setList("data", datas);
                        }
                        if(s.getPoints() != null){
                            ArrayList<UDTValue> points = new ArrayList<>();
                            for (TrajectoryPoint p: s.getPoints()) {
                                UDTValue point = pointUT.newValue();
                                point.setInt("id", p.getId() == null ? -1 : p.getId());
                                point.setFloat("h", p.getH());
                                point.setFloat("lng", p.getLng());
                                point.setFloat("lat", p.getLat());
                                point.setLong("timestamp", p.getTimestamp());
                                if(p.getData() != null){
                                    ArrayList<UDTValue> datas = new ArrayList<>();
                                    for (TrajectoryPointData d: p.getData()) {
                                        UDTValue data = dataUT.newValue();
                                        data.setInt("id", d.getId() == null ? -1 : d.getId());
                                        data.setString("key", d.getKey());
                                        data.setString("value", d.getValue());
                                        datas.add(data);
                                    }
                                    point.setList("data", datas);
                                }
                                points.add(point);
                            }
                            segment.setList("points", points);
                        }
                        segments.add(segment);
                    }
                    version.setList("segments", segments);
                }
                versions.add(version);
            }
            bs.setList("versions", versions);
        }
    }

    private Trajectory readRow(Row row) throws Exception {
        Trajectory ret = new Trajectory();
        ret.setId(row.getLong("id"));
        ret.setDescription(row.getString("description"));
        ret.setOriginalTrajectory(row.getInt("originalTrajectory"));
        ret.setLastModified(row.getTimestamp("lastModified"));
        List<UDTValue> versions = row.getList("versions", UDTValue.class);
        if(versions != null) {
            for (UDTValue version : versions) {
                TrajectoryVersion v = new TrajectoryVersion();
                v.setId(version.getInt("id"));
                v.setPreviousVersion(version.getInt("previousVersion") == -1 ? null : version.getInt("previousVersion"));
                v.setVersion(version.getInt("version"));
                v.setLastModified(version.getTimestamp("lastModified"));
                String type = version.getString("type");
                v.setType(type == null? null: TrajectoryType.valueOf(type));
                UDTValue user = version.getUDTValue("user");
                if(user != null){
                    User u = new User();
                    u.setId(user.getInt("id") == -1 ? null : user.getInt("id"));
                    u.setName(user.getString("name"));
                    v.setUser(u);
                }
                UDTValue process = version.getUDTValue("process");
                if(process != null){
                    TrajectoryProcess p = new TrajectoryProcess();
                    p.setExecutionTime(process.getTimestamp("executionTime"));
                    p.setExecutionDuration(process.getLong("executionDuration"));
                    p.setComponentId(process.getInt("componentId"));
                    v.setProcess(p);
                }
                List<UDTValue> datas = version.getList("data", UDTValue.class);
                if(datas != null){
                    for(UDTValue data: datas){
                        TrajectoryVersionData d = new TrajectoryVersionData();
                        d.setId(data.getInt("id") == -1 ? null : data.getInt("id"));
                        d.setKey(data.getString("key"));
                        d.setValue(data.getString("value"));
                        v.addData(d);
                    }
                }
                List<UDTValue> segments = version.getList("segments", UDTValue.class);
                if(segments != null){
                    for(UDTValue segment: segments){
                        TrajectorySegment s = new TrajectorySegment();
                        String mode = segment.getString("transportationMode");
                        s.setTransportationMode(mode == null? null: TransportationMode.valueOf(mode));
                        datas = segment.getList("data", UDTValue.class);
                        if(datas != null){
                            for(UDTValue data: datas){
                                TrajectorySegmentData d = new TrajectorySegmentData();
                                d.setId(data.getInt("id") == -1 ? null : data.getInt("id"));
                                d.setKey(data.getString("key"));
                                d.setValue(data.getString("value"));
                                s.addData(d);
                            }
                        }
                        List<UDTValue> points = segment.getList("points", UDTValue.class);
                        if(points != null){
                            for(UDTValue point: points){
                                TrajectoryPoint p = new TrajectoryPoint();
                                p.setId(point.getInt("id") == -1 ? null : point.getInt("id"));
                                p.setH(point.getFloat("h"));
                                p.setLat(point.getFloat("lat"));
                                p.setLng(point.getFloat("lng"));
                                p.setTimestamp(point.getLong("timestamp"));
                                datas = point.getList("data", UDTValue.class);
                                if(datas != null){
                                    for(UDTValue data: datas){
                                        TrajectoryPointData d = new TrajectoryPointData();
                                        d.setId(data.getInt("id") == -1 ? null : data.getInt("id"));
                                        d.setKey(data.getString("key"));
                                        d.setValue(data.getString("value"));
                                        p.addData(d);
                                    }
                                }
                                s.addPoint(p);
                            }
                        }
                        v.addSegment(s);
                    }
                }
                ret.addVersion(v);
            }
        }
        return ret;
    }
}
