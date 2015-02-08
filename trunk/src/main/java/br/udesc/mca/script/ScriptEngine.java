package br.udesc.mca.script;

import br.udesc.mca.trajectory.model.TrajectoryPoint;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
  A ideia dessa classe é ter metodos abstratos para conversao dos objetos e metodos concretos para a  possivel regra na execucao de um script
 */
public abstract class ScriptEngine {

    public static ScriptEngine initEngine(String engine){
        /*
           TODO: pensei em usar reflection ou SPI para instanciar a classe do pacote "acima" mas nao sei se é realmente necessatio... :)
         */
        if("python".equals(engine) || "py".equals(engine)){
            return new PythonEngine();

        }
        throw  new UnsupportedOperationException("Invalid Engine");
    }

    /**
     * O contexto de execucao do python devera ser o arquivo .py
     * @param s
     */
    public abstract void setExecutionContext(String s);

    /**
     * para o python importa um .py (ex.  engine.addToExecutionContext("sys"))
     * @param s
     */
    public abstract void addToExecutionContext(String s);

    /**
     * Adiciona ou seta um objeto no contexto de execucao do scrip (para o python seta uma variavel....por exemplo)
     * @param s
     */
    public abstract void setOnExecutionContext(String s, Object o);

    /**
     *Retorna um objeto do contexto de execucao do scrip (para o python recupera uma variavel....por exemplo)
     * @param s
     */
    public abstract Object getFromExecutionContext(String s);

    /**
     * executa um metodo ou  funcao. minha primeira ideia eh nao ter paramentros de entrada nem de saida... Os interpretadores trabalham melhor com variaveis(entao poderiamos predefinir algumas para cada tipo de execucao)...
     * @param s
     */
    public abstract void execute(String s);

    /**
     * Os metodos de conversao de-para da entidade talvez sejam desnecessarios mas por enquanto acho melhor ficarem  aqui...o Glaucio nao para de mudar tudo
     */
    protected ITrajectory toITrajectory(TrajectoryPoint t){
        ITrajectory i = new ITrajectory();

        i.lat = t.getY();
        i.lon = t.getX();
        i.precisao = 0.9f; // TODO: nao deveria ter isso na trajetorioa?

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(t.getTimestamp());

        i.ano = c.get(Calendar.YEAR);
        i.mes = c.get(Calendar.MONTH) + 1;
        i.dia = c.get(Calendar.DAY_OF_MONTH);

        i.hora = c.get(Calendar.HOUR_OF_DAY);
        i.min = c.get(Calendar.MINUTE);
        i.seg = c.get(Calendar.SECOND);

        return i;
    }
    protected List<ITrajectory> toITrajectory(List<TrajectoryPoint> ts){
        ArrayList<ITrajectory> r = new ArrayList<>();
        for(TrajectoryPoint t: ts){
            r.add(toITrajectory(t));
        }
        return r;
    }

    protected TrajectoryPoint fromITrajectory(ITrajectory i){
        TrajectoryPoint t = new TrajectoryPoint();

        t.setY(i.lat);
        t.setX(i.lon);
        //i.precisao

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(t.getTimestamp());

        c.set(Calendar.YEAR, i.ano);
        c.set(Calendar.MONTH ,i.mes -1);
        c.set(Calendar.DAY_OF_MONTH, i.dia);

        c.set(Calendar.HOUR_OF_DAY, i.hora);
        c.set(Calendar.MINUTE, i.min);
        c.set(Calendar.SECOND, i.seg);
        c.set(Calendar.MILLISECOND, 0);

        t.setTimestamp(c.getTimeInMillis());

        return t;
    }



    /**
     * Esse é o primeiro exemplo/ideia de execução de scrip. Dado uma lista de trajetorias (e mais tarde as informacoes complementares dos sensores)
     * Cria uma nova versao da lista fazendo uma transformacao na mesma (exemplo remover itens impressisos ou duplicados)
     */
    public List<TrajectoryPoint> processTrajectoryList(List<TrajectoryPoint> toProcess) throws Exception {
        List<TrajectoryPoint> toReturn = new ArrayList<>();

        List<ITrajectory> trajectories = toITrajectory(toProcess);

        this.setOnExecutionContext("listToProcess", trajectories);

        this.execute("processList");

        List trajectoriesReturned = (List) this.getFromExecutionContext("listToReturn");

        for(Object i: trajectoriesReturned){
            toReturn.add(fromITrajectory((ITrajectory)i));
        }

        return toReturn;
    }
}
