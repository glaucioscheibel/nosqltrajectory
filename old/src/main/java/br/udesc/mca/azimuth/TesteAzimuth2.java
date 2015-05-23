package br.udesc.mca.azimuth;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class TesteAzimuth2 {
    public static void main(String[] args) throws Exception {
        BigDecimal v1 = new BigDecimal(-48.8814309);

        BigDecimal v2 = new BigDecimal(-48.8807443);

        BigDecimal v3 = v1.subtract(v2);

        System.out.println(v3);

        DecimalFormat df = new DecimalFormat("0.0000000000000000000");

        System.out.println(df.format(v3));

        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("JavaScript");

        String s = "print(-48.8814309 - -48.8807443);";

        engine.eval(s);

    }
}
