package br.udesc.mca;

import org.python.util.PythonInterpreter;

public class HelloWord {

    public static void main(String[] args) {
        System.out.println("Crazy Maths");
        System.out.println("Crazy Maths!");

        //http://www.jython.org/jythonbook/en/1.0/JythonAndJavaIntegration.html
        try {
            PythonInterpreter py = new PythonInterpreter();
            py.exec("print \"From Python: Crazy Maths\"");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
