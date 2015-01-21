package br.udesc.mca.sec1.projeto.dao.keyvalue;

import org.junit.BeforeClass;

import br.udesc.mca.sec1.projeto.dao.PersistenceModel;
import br.udesc.mca.sec1.projeto.dao.PersistenceTest;
import br.udesc.mca.sec1.projeto.model.Customer;

public class RedisPersistenceTest extends PersistenceTest {

    public RedisPersistenceTest() {
        super(PersistenceModel.KEY_VALUE);
    }

    @BeforeClass
    public static void beforeClass() {
        Customer c = new Customer(1, "Fulano de Tal");
        RedisPersistence db = RedisPersistence.getInstance();
        db.store(c);
    }
}
