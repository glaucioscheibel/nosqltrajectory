package br.udesc.mca.sec1.projeto.dao.column;

import org.junit.BeforeClass;

import br.udesc.mca.sec1.projeto.dao.PersistenceTest;
import br.udesc.mca.trajectory.dao.PersistenceModel;
import br.udesc.mca.trajectory.dao.column.CassandraPersistence;
import br.udesc.mca.trajectory.model.Trajectory;

public class CassandraPersistenceTest extends PersistenceTest {

    public CassandraPersistenceTest() {
        super(PersistenceModel.COLUMN_FAMILY);
    }

    @BeforeClass
    public static void beforeClass() {
        Trajectory c = new Trajectory();
        CassandraPersistence cp = CassandraPersistence.getInstance();
        System.out.println(cp.getClass().getName());
        cp.createDB();
        cp.store(c);
    }
}
