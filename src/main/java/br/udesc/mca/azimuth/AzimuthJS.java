package br.udesc.mca.azimuth;

import java.io.InputStream;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.python.jline.internal.InputStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AzimuthJS {
    private static Logger log = LoggerFactory.getLogger(AzimuthJS.class);

    public static double calculateLengthInKM(double lat1, double lon1, double lat2, double lon2) {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("JavaScript");
        try {
            InputStream is = AzimuthJS.class.getClassLoader().getResourceAsStream("latlon.js");
            engine.eval(new InputStreamReader(is));
            engine.put("lat1", lat1);
            engine.put("lon1", lon1);
            engine.put("lat2", lat2);
            engine.put("lon2", lon2);
            engine.eval("var p1 = new LatLon(lat1, lon1);");
            engine.eval("var p2 = new LatLon(lat2, lon2);");
            String result = (String) engine.eval("p1.distanceTo(p2);");
            return Double.valueOf(result);
        } catch (ScriptException e) {
            log.error(e.getMessage(), e);
        }
        return 0D;
    }

    public static double azimuth(double lat1, double lon1, double lat2, double lon2) {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("JavaScript");
        try {
            InputStream is = AzimuthJS.class.getClassLoader().getResourceAsStream("latlon.js");
            engine.eval(new InputStreamReader(is));
            engine.put("lat1", lat1);
            engine.put("lon1", lon1);
            engine.put("lat2", lat2);
            engine.put("lon2", lon2);
            engine.eval("var p1 = new LatLon(lat1, lon1);");
            engine.eval("var p2 = new LatLon(lat2, lon2);");
            Number result = (Number) engine.eval("p1.bearingTo(p2);");
            return result.doubleValue();
        } catch (ScriptException e) {
            log.error(e.getMessage(), e);
        }
        return 0D;
    }

    public static double azimuthDifference(double azimute1, double azimute2) {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("JavaScript");
        try {
            engine.put("azimute1", azimute1);
            engine.put("azimute2", azimute2);
            Number result = (Number) engine.eval("azimute1 - azimute2");
            return result.doubleValue();
        } catch (ScriptException e) {
            log.error(e.getMessage(), e);
        }
        return 0D;
    }
}
