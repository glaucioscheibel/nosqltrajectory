package br.udesc.mca.sec1.projeto.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import br.udesc.mca.sec1.projeto.model.Customer;
import br.udesc.mca.trajectory.dao.PersistenceAbstractFactory;
import br.udesc.mca.trajectory.dao.PersistenceDAO;
import br.udesc.mca.trajectory.dao.PersistenceModel;

@Ignore
public abstract class PersistenceTest {

    private PersistenceModel model;

    public PersistenceTest(PersistenceModel model) {
        this.model = model;
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testFindAll() {
        PersistenceDAO<Customer> db = PersistenceAbstractFactory.getPersistenceDAO(this.model);
        List<Customer> lc = db.findAll();
        assertEquals(1, lc.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testFindById() {
        PersistenceDAO<Customer> db = PersistenceAbstractFactory.getPersistenceDAO(this.model);
        Customer c = db.findById(1);
        assertNotNull(c);
        assertEquals(new Customer(1), c);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testUpdate() {
        PersistenceDAO<Customer> db = PersistenceAbstractFactory.getPersistenceDAO(this.model);
        Customer c = db.findById(1);
        c.setName("Beltrano");
        db.store(c);
        c = db.findById(1);
        assertNotNull(c);
        assertEquals("Beltrano", c.getName());
        List<Customer> lc = db.findAll();
        assertEquals(1, lc.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testDeleteById() {
        PersistenceDAO<Customer> db = PersistenceAbstractFactory.getPersistenceDAO(this.model);
        Customer c2 = new Customer(2, "Fulana de Tal");
        db.store(c2);
        List<Customer> lc = db.findAll();
        assertEquals(2, lc.size());
        db.deleteById(2);
        lc = db.findAll();
        assertEquals(1, lc.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testInsertWithData() {
        PersistenceDAO<Customer> db = PersistenceAbstractFactory.getPersistenceDAO(this.model);
        Customer c3 = new Customer(3, "Fulano dos Dados");
        c3.addCustomerData("telefone1", "222222");
        c3.addCustomerData("telefone2", "333333");
        c3.addCustomerData("telefone3", "444444");
        db.store(c3);
        c3 = db.findById(3);
        assertNotNull(c3.getCustomerData());
        assertEquals(3, c3.getCustomerData().size());
        db.deleteById(3);
        c3 = db.findById(3);
        assertNull(c3);
    }
}
