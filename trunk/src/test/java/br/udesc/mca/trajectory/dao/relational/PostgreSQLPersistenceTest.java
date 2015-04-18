package br.udesc.mca.trajectory.dao.relational;

import org.junit.BeforeClass;
import br.udesc.mca.trajectory.dao.PersistenceModel;
import br.udesc.mca.trajectory.dao.PersistenceTest;
import br.udesc.mca.trajectory.model.Trajectory;

public class PostgreSQLPersistenceTest extends PersistenceTest {

    public PostgreSQLPersistenceTest() {
        super(PersistenceModel.RELATIONAL);
    }

    @BeforeClass
    public static void beforeClass() {
        Trajectory c = new Trajectory(1L);
        PostgreSQLPersistence pgp = new PostgreSQLPersistence();
        pgp.deleteAll();
        pgp.store(c);
    }
}
