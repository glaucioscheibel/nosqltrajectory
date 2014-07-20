package br.udesc.test;

import static org.junit.Assert.assertEquals;

import org.apache.tuscany.sca.TuscanyRuntime;
import org.apache.tuscany.sca.Node;
import org.junit.Test;

import br.udesc.mca.sca.math.CrazyMaths;

public class CrazyMathsTests {

    @Test
    public void test() throws Exception {
        Node node = null;
        try {
            node = TuscanyRuntime.runComposite("crazymaths.composite", "bin");
            CrazyMaths cm = node.getService(CrazyMaths.class, "CrazyMaths");
            double r = cm.add(1, 2);
            assertEquals(3.0, r, 0.0);
        } finally {
            if (node != null) {
                node.stop();
            }
        }
    }
}
