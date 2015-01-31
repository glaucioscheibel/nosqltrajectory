package br.udesc.mca.sec1.projeto.dao.column;

import org.junit.BeforeClass;

import br.udesc.mca.sec1.projeto.dao.PersistenceTest;
import br.udesc.mca.sec1.projeto.model.Customer;
import br.udesc.mca.trajectory.dao.PersistenceModel;
import br.udesc.mca.trajectory.dao.column.CassandraPersistence;

public class CassandraPersistenceTest extends PersistenceTest {

    public CassandraPersistenceTest() {
        super(PersistenceModel.COLUMN_FAMILY);
    }

    @BeforeClass
    public static void beforeClass() {
        Customer c = new Customer(1, "Fulano de Tal");
        CassandraPersistence cp = CassandraPersistence.getInstance();
        System.out.println(cp.getClass().getName());
        cp.createDB();
        cp.store(c);
    }
}
