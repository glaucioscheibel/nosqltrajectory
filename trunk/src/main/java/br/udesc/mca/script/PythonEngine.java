package br.udesc.mca.script;


import org.python.core.*;
import org.python.util.PythonInterpreter;

import java.util.ArrayList;
import java.util.List;

class PythonEngine extends ScriptEngine {
    private PythonInterpreter py;
    private PyObject executionContext;

    protected  PythonEngine(){
        py = new PythonInterpreter();
    }


    @Override
    public void setExecutionContext(String s) {
        py.exec("import " + s);
        executionContext = py.get(s);
    }

    @Override
    public void addToExecutionContext(String s) {
        py.exec("import " + s);
    }

    @Override
    public void setOnExecutionContext(String s, Object o) {
        if(o instanceof Integer || o instanceof Byte || o instanceof Short) {
            py.set(s, new PyInteger(((Number)o).intValue()));
        } else if( o instanceof Long){
            py.set(s, new PyLong(((Number)o).longValue()));
        }  else if( o instanceof List){
            StringBuilder sb = new StringBuilder();
            boolean c = false;
            sb.append("[");
            for(Object lo: (List)o){
                if(c){
                    sb.append(",");
                }
                sb.append("[");
                if(lo instanceof ITrajectory){
                    ITrajectory i =(ITrajectory) lo;
                    sb      .append(i.lat).append(",")//TODO: Ver se lat é y mesmo e ver quem iria primeiro no array
                            .append(i.lon).append(",")
                            .append(i.ano).append(",")
                            .append(i.mes).append(",")
                            .append(i.dia).append(",")
                            .append(i.hora).append(",")
                            .append(i.min).append(",")
                            .append(i.seg).append(",")
                            .append(i.precisao);
                } else {
                    sb.append(lo.toString());
                }
                sb.append("]");
                c=true;
            }
            sb.append("]");

            py.exec(s + " = " + sb.toString());
            PyObject pv = py.get(s);
            executionContext.__setattr__(s, pv);
        } else {
            throw  new UnsupportedOperationException("Need to  Implement datatype " + o.getClass().getName() );
        }
    }

    @Override
    public Object getFromExecutionContext(String s) {
        PyObject o = executionContext.__getattr__(s);

        //TODO: tratar outros retornos
        if(o !=null && o.getType() != null && "list".equals(o.getType().getName())){
            PyList pl = (PyList)o;
            ArrayList ret = new ArrayList();

            for(int i = 0; i < pl.size(); i++){
                PyObject oi = pl.__getitem__(i);
                if("list".equals(oi.getType().getName()) && ((PyList)oi).size() == 9){ //a principio o array da trajetoria tem 9 posicoes
                    PyList loi = (PyList)oi;
                    ITrajectory it = new ITrajectory();
                    it.lat = (float) loi.__getitem__(0).__float__().getValue();
                    it.lon = (float) loi.__getitem__(1).__float__().getValue();

                    it.ano = loi.__getitem__(2).asInt();
                    it.dia =  loi.__getitem__(3).asInt();
                    it.mes =  loi.__getitem__(4).asInt();

                    it.hora = loi.__getitem__(5).asInt();
                    it.min =  loi.__getitem__(6).asInt();
                    it.seg =  loi.__getitem__(7).asInt();

                    it.lon = (float) loi.__getitem__(8).__float__().getValue();


                    ret.add(it);
                }
            }

            return  ret;
        }




        return null;
    }

    @Override
    public void execute(String s) {
        executionContext.invoke(s, executionContext);
    }
}