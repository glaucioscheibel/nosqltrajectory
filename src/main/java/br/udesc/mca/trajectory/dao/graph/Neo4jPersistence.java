package br.udesc.mca.trajectory.dao.graph;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;

import br.udesc.mca.trajectory.model.Trajectory;

public class Neo4jPersistence extends GraphPersistence {
    private static Neo4jPersistence instance;
    private GraphDatabaseService db;
    private Index<Node> dbindex;

    public static Neo4jPersistence getInstance() {
        if (instance == null) {
            instance = new Neo4jPersistence();
        }
        return instance;
    }

    public Neo4jPersistence() {
        GraphDatabaseFactory factory = new GraphDatabaseFactory();
        this.db = factory.newEmbeddedDatabase("/bancografo");
        this.dbindex = this.db.index().forNodes("nodes");
    }

    @Override
    public Trajectory store(Trajectory c) {
        /*
         * Trajectory aux = this.findById(c.getId()); Transaction t =
         * this.db.beginTx(); Node n = null; Node nd = null; if (aux != null) {
         * n = this.dbindex.get("id", c.getId()).getSingle(); } else { n =
         * this.db.createNode(); if (c.getTrajectoryData() != null &&
         * !c.getTrajectoryData().isEmpty()) { nd = this.db.createNode();
         * n.createRelationshipTo(nd, RelationType.Trajectory_DATA); } }
         * n.setProperty("id", c.getId()); //n.setProperty("name", c.getName());
         * this.dbindex.add(n, "id", c.getId()); List<TrajectoryData> lcd =
         * c.getTrajectoryData(); if (lcd != null && !lcd.isEmpty()) { for
         * (String s: nd.getPropertyKeys()) { nd.removeProperty(s); } for
         * (TrajectoryData cd : lcd) { //nd.setProperty(cd.getDataKey(),
         * cd.getDataValue()); } } t.success();
         */
        return c;
    }

    @Override
    public List<Trajectory> findAll() {
        List<Trajectory> lc = new ArrayList<>();
        IndexHits<Node> ihn = this.dbindex.query("id", "*");
        for (Node n : ihn) {
            Trajectory c = new Trajectory();
            // c.setId((Integer) n.getProperty("id"));
            // c.setName((String) n.getProperty("name"));
            this.addProperties(c, n);
            lc.add(c);
        }
        return lc;
    }

    @Override
    public Trajectory findById(long id) {
        Trajectory c = null;
        Node n = this.dbindex.get("id", id).getSingle();
        if (n != null) {
            c = new Trajectory();
            // c.setId((Integer) n.getProperty("id"));
            // c.setName((String) n.getProperty("name"));
            this.addProperties(c, n);
        }
        return c;
    }

    private void addProperties(Trajectory c, Node n) {
        Relationship r = n.getSingleRelationship(RelationType.CUSTOMER_DATA, Direction.OUTGOING);
        if (r != null) {
            Node nd = r.getEndNode();
            Iterable<String> is = nd.getPropertyKeys();
            for (String s : is) {
                // c.addTrajectoryData(s, (String) nd.getProperty(s));
            }
        }
    }

    @Override
    public void deleteById(long id) {
        Node n = this.dbindex.get("id", id).getSingle();
        if (n != null) {
            Transaction t = this.db.beginTx();
            Relationship r = n.getSingleRelationship(RelationType.CUSTOMER_DATA, Direction.OUTGOING);
            if (r != null) {
                Node nd = r.getEndNode();
                nd.delete();
                r.delete();
                this.dbindex.remove(n, "id", id);
            }
            n.delete();
            t.success();
        }
    }

    @Override
    public void close() {
        this.dbindex = null;
        this.db.shutdown();
        this.db = null;
    }
}
