package br.udesc.mca.script;

import br.udesc.mca.trajectory.model.TrajectoryPoint;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class PythonScripTest {

    @Test
    public void testInicial() {
        try (ScriptEngine engine = ScriptEngine.initEngine("python")){
            engine.setExecutionContext("processListTest");//nome do arquivo .py  (OBS: tem que estar no path da execucao do java)


            List<TrajectoryPoint> pl = new ArrayList<>();
            TrajectoryPoint p = new TrajectoryPoint();
            p.setY(80.0f);
            p.setX(80.0f);
            p.setTimestamp(System.currentTimeMillis());
            pl.add(p);

            List<TrajectoryPoint> rpl = null;

            rpl = engine.processTrajectoryList(pl);

            assertTrue(rpl.get(0).getY() == 79.0f);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException(e);
        }

    }
}
