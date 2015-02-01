package br.udesc.mca.trajectory.dao.relational;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import br.udesc.mca.trajectory.model.Trajectory;

public class PostgreSQLPersistence extends RelationalPersistence {
    private static PostgreSQLPersistence instance;

    private DataSource ds;

    public static PostgreSQLPersistence getInstance() {
        if (instance == null) {
            instance = new PostgreSQLPersistence();
        }
        return instance;
    }

    public PostgreSQLPersistence() {
        BasicDataSource bds = new BasicDataSource();
        bds.setDriverClassName("");
        bds.setUsername("postgres");
        bds.setPassword("senha");
        bds.setUrl("jdbc:postgresql://127.0.0.1/" + DBNAME);
    }

    @Override
    public Trajectory store(Trajectory c) {
        this.log.info("store(" + c + ")");
        /*
         * Trajectory aux = this.findById(c.getId()); try {
         * this.db.setAutoCommit(false); PreparedStatement ps = null; if (aux ==
         * null) { ps =
         * this.db.prepareStatement("insert into Trajectory(id, name) values(?, ?)"
         * ); ps.setInt(1, c.getId()); ps.setString(2, c.getName()); } else { ps
         * =
         * this.db.prepareStatement("update Trajectory set name=? where id=?");
         * ps.setString(1, c.getName()); ps.setInt(2, c.getId()); }
         * ps.executeUpdate(); ps.close(); ps =
         * this.db.prepareStatement("delete from Trajectorydata where id=?");
         * ps.setInt(1, c.getId()); ps.executeUpdate(); List<TrajectoryData> lcd
         * = c.getTrajectoryData(); if (lcd != null && !lcd.isEmpty()) { ps =
         * this.db.prepareStatement(
         * "insert into Trajectorydata(id, key, value) values(?, ?, ?)"); for
         * (TrajectoryData cd : lcd) { ps.clearParameters(); ps.setInt(1,
         * c.getId()); ps.setString(2, cd.getDataKey()); ps.setString(3,
         * cd.getDataValue()); ps.executeUpdate(); } } this.db.commit(); } catch
         * (SQLException e) { try { this.db.rollback(); } catch (SQLException
         * e1) {} log.error(e.getMessage(), e); } finally { try {
         * this.db.setAutoCommit(true); } catch (SQLException e) {} }
         */
        return null;
    }

    @Override
    public List<Trajectory> findAll() {
        this.log.info("findAll()");
        List<Trajectory> lc = new LinkedList<>();
        try {
            Connection db = this.ds.getConnection();
            Statement st = db.createStatement();
            ResultSet rs = st
                    .executeQuery("SELECT Trajectory.id, Trajectory.name, Trajectorydata.key, Trajectorydata.value "
                            + "FROM Trajectory LEFT JOIN Trajectorydata ON Trajectory.id = Trajectorydata.id");
            while (rs.next()) {
                Trajectory c = new Trajectory();
                // c.setId(rs.getInt(1));
                // c.setName(rs.getString(2));
                int i = lc.indexOf(c);
                if (i >= 0) {
                    c = lc.get(i);
                } else {
                    lc.add(c);
                }
                String key = rs.getString(3);
                if (!rs.wasNull()) {
                    // c.addTrajectoryData(key, rs.getString(4));
                }
            }
            rs.close();
            st.close();
            db.close();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return lc;
    }

    @Override
    public Trajectory findById(long id) {
        this.log.info("findById(" + id + ")");
        Trajectory c = null;
        try {
            Connection db = this.ds.getConnection();
            PreparedStatement ps = db
                    .prepareStatement("SELECT Trajectory.id, Trajectory.name, Trajectorydata.key, "
                            + "Trajectorydata.value FROM Trajectory LEFT JOIN Trajectorydata ON Trajectory.id = Trajectorydata.id where "
                            + "Trajectory.id = ?");
            // ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                c = new Trajectory();
                // c.setId(rs.getInt(1));
                // c.setName(rs.getString(2));
                String key = rs.getString(3);
                if (!rs.wasNull()) {
                    // c.addTrajectoryData(key, rs.getString(4));
                }
            }
            while (rs.next()) {
                // c.addTrajectoryData(rs.getString(3), rs.getString(4));
            }
            rs.close();
            ps.close();
            db.close();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return c;
    }

    @Override
    public void deleteById(long id) {
        this.log.info("deleteById(" + id + ")");
        Connection db = null;
        try {
            db = this.ds.getConnection();
            db.setAutoCommit(false);
            PreparedStatement ps = db.prepareStatement("delete from Trajectory where id=?");
            // ps.setInt(1, id);
            ps.executeUpdate();
            ps = db.prepareStatement("delete from Trajectorydata where id=?");
            // ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();
            db.commit();
        } catch (SQLException e) {
            try {
                if (db != null) {
                    db.rollback();
                }
            } catch (SQLException e1) {}
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (db != null) {
                    db.setAutoCommit(true);
                }
            } catch (SQLException e) {}
        }
        if (db != null) {
            try {
                db.close();
            } catch (SQLException e) {}
        }
    }

    public void deleteAll() {
        try {
            Connection db = this.ds.getConnection();
            Statement st = db.createStatement();
            st.executeUpdate("truncate table Trajectory");
            st.executeUpdate("truncate table Trajectorydata");
            st.close();
            db.close();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void close() {
        if (this.ds != null) {
            try {
                ((BasicDataSource) this.ds).close();
            } catch (SQLException e) {}
            this.ds = null;
        }
    }
}
