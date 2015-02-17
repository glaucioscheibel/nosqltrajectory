package br.udesc.mca.trajectory.service;


import org.apache.tuscany.sca.Node;
import org.apache.tuscany.sca.TuscanyRuntime;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.oasisopen.sca.NoSuchServiceException;

public class TrajectoryInputServiceTest {

    @Test
    public void testInsertSinglePoint(){
        Node node = TuscanyRuntime.runComposite("trajectoryinputservice.composite", "target/classes");
        try {
            TrajectoryInput service = node.getService(TrajectoryInput.class, "TrajectoryInput");

            long id = service.insertSinglePoint(0,10.0f,10.0f);

            assertTrue(id > 0);
        } catch (NoSuchServiceException e) {
            throw new RuntimeException(e);
        } finally {
            node.stop();
        }

    }

    @Test
    public void testInsertWS(){
        assertTrue(true);
    }
}
