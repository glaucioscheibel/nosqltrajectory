package br.udesc.mca.sec1.projeto.dao.relational;

import org.junit.BeforeClass;

import br.udesc.mca.sec1.projeto.dao.PersistenceTest;
import br.udesc.mca.trajectory.dao.PersistenceModel;
import br.udesc.mca.trajectory.dao.relational.PostgreSQLPersistence;
import br.udesc.mca.trajectory.model.Trajectory;

public class PostgreSQLPersistenceTest extends PersistenceTest {

    public PostgreSQLPersistenceTest() {
        super(PersistenceModel.RELATIONAL);
    }

    @BeforeClass
    public static void beforeClass() {
        Trajectory c = new Trajectory();
        PostgreSQLPersistence pgp = new PostgreSQLPersistence();
        pgp.deleteAll();
        pgp.store(c);
    }
}
