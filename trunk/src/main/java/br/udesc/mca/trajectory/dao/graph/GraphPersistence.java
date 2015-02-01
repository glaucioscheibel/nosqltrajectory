package br.udesc.mca.trajectory.dao.graph;

import br.udesc.mca.trajectory.dao.PersistenceDAO;
import br.udesc.mca.trajectory.dao.PersistenceModel;
import br.udesc.mca.trajectory.model.Trajectory;

public abstract class GraphPersistence extends PersistenceDAO<Trajectory> {
    public static GraphPersistence getInstance() {
        return (GraphPersistence) PersistenceDAO.getInstance(PersistenceModel.GRAPH);
    }
}
