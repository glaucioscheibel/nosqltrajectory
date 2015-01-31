package br.udesc.mca.sec1.projeto.dao.document;

import org.junit.BeforeClass;

import br.udesc.mca.sec1.projeto.dao.PersistenceTest;
import br.udesc.mca.sec1.projeto.model.Customer;
import br.udesc.mca.trajectory.dao.PersistenceModel;
import br.udesc.mca.trajectory.dao.document.MongoPersistence;

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
        Customer c = new Customer(1, "Fulano de Tal");
        MongoPersistence mp = MongoPersistence.getInstance();
        mp.store(c);
    }
}
