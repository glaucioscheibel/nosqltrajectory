package br.udesc.mca.trajectory.dao.keyvalue;

import org.junit.BeforeClass;

import br.udesc.mca.trajectory.dao.PersistenceModel;
import br.udesc.mca.trajectory.dao.PersistenceTest;
import br.udesc.mca.trajectory.model.Trajectory;

public class RedisPersistenceTest extends PersistenceTest {

    public RedisPersistenceTest() {
        super(PersistenceModel.KEY_VALUE);
    }

    @BeforeClass
    public static void beforeClass() {
        Trajectory c = new Trajectory(1L, "teste");
        RedisPersistence db = RedisPersistence.getInstance();
        db.store(c);
    }
}
