package br.udesc.mca.sec1.projeto.dao.document;

import org.junit.BeforeClass;

import br.udesc.mca.sec1.projeto.dao.PersistenceTest;
import br.udesc.mca.trajectory.dao.PersistenceModel;
import br.udesc.mca.trajectory.dao.document.MongoPersistence;
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
