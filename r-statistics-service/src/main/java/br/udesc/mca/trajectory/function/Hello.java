package br.udesc.mca.trajectory.function;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import org.renjin.sexp.SEXP;

public class Hello {

    public String hello() throws Exception {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("Renjin");
        if (engine == null) {
            throw new RuntimeException("Renjin Script Engine not found on the classpath.");
        }
        SEXP res = (SEXP) engine.eval("a <- 2; b <- 3; a*b");
        return String.valueOf(res.asReal());
    }

}
