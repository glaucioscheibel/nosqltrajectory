package br.udesc.mca.trajectory.dao.document;

import org.junit.BeforeClass;

import br.udesc.mca.trajectory.dao.PersistenceModel;
import br.udesc.mca.trajectory.dao.PersistenceTest;
import br.udesc.mca.trajectory.model.Trajectory;

/**
 *
 * @author Glaucio
 */
public class MongoPersistenceTest extends PersistenceTest {

    public MongoPersistenceTest() {
        super(PersistenceModel.DOCUMENT);
    }

    @BeforeClass
    public static void beforeClass() {
        Trajectory c = new Trajectory();
        MongoPersistence mp = MongoPersistence.getInstance();
        mp.store(c);
    }
}
