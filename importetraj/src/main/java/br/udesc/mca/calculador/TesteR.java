package br.udesc.mca.calculador;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.renjin.sexp.SEXP;

public class TesteR {

	public static void main(String[] args) throws Exception {
		ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("Renjin");
        if (engine == null) {
            throw new RuntimeException("Renjin Script Engine not found on the classpath.");
        }
        SEXP res = (SEXP) engine.eval("a <- 2; b <- 3; a*b");
        System.out.println(String.valueOf(res.asReal()));

	}

}
