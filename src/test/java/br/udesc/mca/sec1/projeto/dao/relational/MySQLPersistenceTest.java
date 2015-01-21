package br.udesc.mca.sec1.projeto.dao.relational;

import org.junit.BeforeClass;

import br.udesc.mca.sec1.projeto.dao.PersistenceModel;
import br.udesc.mca.sec1.projeto.dao.PersistenceTest;
import br.udesc.mca.sec1.projeto.model.Customer;

public class MySQLPersistenceTest extends PersistenceTest {

    public MySQLPersistenceTest() {
        super(PersistenceModel.RELATIONAL);
    }

    @BeforeClass
    public static void beforeClass() {
        Customer c = new Customer(1, "Fulano de Tal", "c1", "d1", "c2", "d2");
        MySQLPersistence pgp = new MySQLPersistence();
        pgp.deleteAll();
        pgp.store(c);
    }
}
