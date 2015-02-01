package br.udesc.mca.trajectory.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import br.udesc.mca.trajectory.dao.PersistenceAbstractFactory;
import br.udesc.mca.trajectory.dao.PersistenceDAO;
import br.udesc.mca.trajectory.dao.PersistenceModel;
import br.udesc.mca.trajectory.model.Trajectory;

@Ignore
public abstract class PersistenceTest {
    private PersistenceModel model;

    public PersistenceTest(PersistenceModel model) {
        this.model = model;
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testFindAll() {
        PersistenceDAO<Trajectory> db = PersistenceAbstractFactory.getPersistenceDAO(this.model);
        List<Trajectory> lc = db.findAll();
        assertEquals(1, lc.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testFindById() {
        long id = 1L;
        PersistenceDAO<Trajectory> db = PersistenceAbstractFactory.getPersistenceDAO(this.model);
        Trajectory c = db.findById(id);
        assertNotNull(c);
        assertEquals(new Trajectory(id), c);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testUpdate() {
        long id = 1L;
        PersistenceDAO<Trajectory> db = PersistenceAbstractFactory.getPersistenceDAO(this.model);
        Trajectory c = db.findById(id);
        c.setDescription("Beltrano");
        db.store(c);
        c = db.findById(id);
        assertNotNull(c);
        assertEquals("Beltrano", c.getDescription());
        List<Trajectory> lc = db.findAll();
        assertEquals(1, lc.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testDeleteById() {
        long id = 2L;
        PersistenceDAO<Trajectory> db = PersistenceAbstractFactory.getPersistenceDAO(this.model);
        Trajectory c2 = new Trajectory(id, "Fulana de Tal");
        db.store(c2);
        List<Trajectory> lc = db.findAll();
        assertEquals(2, lc.size());
        db.deleteById(id);
        lc = db.findAll();
        assertEquals(1, lc.size());
    }
}
