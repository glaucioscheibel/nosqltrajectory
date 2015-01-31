package br.udesc.mca.sec1.projeto.dao.graph;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;

import br.udesc.mca.sec1.projeto.dao.PersistenceTest;
import br.udesc.mca.sec1.projeto.model.Customer;
import br.udesc.mca.trajectory.dao.PersistenceModel;
import br.udesc.mca.trajectory.dao.graph.Neo4jPersistence;

public class Neo4jPersistenceTest extends PersistenceTest {

    public Neo4jPersistenceTest() {
        super(PersistenceModel.GRAPH);
    }

    @BeforeClass
    public static void beforeClass() throws Exception {
        Customer c = new Customer(1, "Fulano de Tal");
        try {
            FileUtils.cleanDirectory(new File("/bancografo"));
        } catch (Exception e) {}
        Neo4jPersistence.getInstance().store(c);
    }
}
