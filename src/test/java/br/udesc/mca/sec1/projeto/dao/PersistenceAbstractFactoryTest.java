package br.udesc.mca.sec1.projeto.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import br.udesc.mca.trajectory.dao.PersistenceAbstractFactory;
import br.udesc.mca.trajectory.dao.PersistenceDAO;
import br.udesc.mca.trajectory.dao.PersistenceModel;
import br.udesc.mca.trajectory.dao.column.CassandraPersistence;
import br.udesc.mca.trajectory.dao.column.ColumnPersistence;
import br.udesc.mca.trajectory.dao.document.DocumentPersistence;
import br.udesc.mca.trajectory.dao.document.MongoPersistence;
import br.udesc.mca.trajectory.dao.graph.GraphPersistence;
import br.udesc.mca.trajectory.dao.graph.Neo4jPersistence;
import br.udesc.mca.trajectory.dao.keyvalue.KeyValuePersistence;
import br.udesc.mca.trajectory.dao.keyvalue.RedisPersistence;
import br.udesc.mca.trajectory.dao.relational.PostgreSQLPersistence;
import br.udesc.mca.trajectory.dao.relational.RelationalPersistence;
import br.udesc.mca.trajectory.model.Trajectory;

public class PersistenceAbstractFactoryTest {

    @Test
    @SuppressWarnings("unchecked")
    public void documentTest() {
        PersistenceDAO<Trajectory> dao = PersistenceAbstractFactory.getPersistenceDAO(PersistenceModel.DOCUMENT);
        assertNotNull(dao);
        assertTrue(dao instanceof DocumentPersistence);
        assertEquals(MongoPersistence.class, dao.getClass());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void keyvalueTest() {
        PersistenceDAO<Trajectory> dao = PersistenceAbstractFactory.getPersistenceDAO(PersistenceModel.KEY_VALUE);
        assertNotNull(dao);
        assertTrue(dao instanceof KeyValuePersistence);
        assertEquals(RedisPersistence.class, dao.getClass());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void relationalTest() {
        PersistenceDAO<Trajectory> dao = PersistenceAbstractFactory.getPersistenceDAO(PersistenceModel.RELATIONAL);
        assertNotNull(dao);
        assertTrue(dao instanceof RelationalPersistence);
        assertEquals(PostgreSQLPersistence.class, dao.getClass());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void columnTest() {
        PersistenceDAO<Trajectory> dao = PersistenceAbstractFactory.getPersistenceDAO(PersistenceModel.COLUMN_FAMILY);
        assertNotNull(dao);
        assertTrue(dao instanceof ColumnPersistence);
        assertEquals(CassandraPersistence.class, dao.getClass());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void graphTest() {
        PersistenceDAO<Trajectory> dao = PersistenceAbstractFactory.getPersistenceDAO(PersistenceModel.GRAPH);
        assertNotNull(dao);
        assertTrue(dao instanceof GraphPersistence);
        assertEquals(Neo4jPersistence.class, dao.getClass());
    }

    @Test(expected=NullPointerException.class)
    @SuppressWarnings({"unchecked", "unused"})
    public void nullTest() {
        PersistenceDAO<Trajectory> dao = PersistenceAbstractFactory.getPersistenceDAO(null);
    }
}
