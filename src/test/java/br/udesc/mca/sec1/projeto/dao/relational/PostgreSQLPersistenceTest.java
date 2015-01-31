package br.udesc.mca.sec1.projeto.dao.relational;

import org.junit.BeforeClass;

import br.udesc.mca.sec1.projeto.dao.PersistenceTest;
import br.udesc.mca.sec1.projeto.model.Customer;
import br.udesc.mca.trajectory.dao.PersistenceModel;
import br.udesc.mca.trajectory.dao.relational.PostgreSQLPersistence;

public class PostgreSQLPersistenceTest extends PersistenceTest {

    public PostgreSQLPersistenceTest() {
        super(PersistenceModel.RELATIONAL);
    }

    @BeforeClass
    public static void beforeClass() {
        Customer c = new Customer(1, "Fulano de Tal", "c1", "d1", "c2", "d2");
        PostgreSQLPersistence pgp = new PostgreSQLPersistence();
        pgp.deleteAll();
        pgp.store(c);
    }
}
