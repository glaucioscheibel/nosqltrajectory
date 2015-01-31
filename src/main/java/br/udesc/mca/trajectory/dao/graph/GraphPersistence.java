package br.udesc.mca.trajectory.dao.graph;

import br.udesc.mca.sec1.projeto.model.Customer;
import br.udesc.mca.trajectory.dao.PersistenceDAO;
import br.udesc.mca.trajectory.dao.PersistenceModel;

public abstract class GraphPersistence extends PersistenceDAO<Customer> {
    public static GraphPersistence getInstance() {
        return (GraphPersistence) PersistenceDAO.getInstance(PersistenceModel.GRAPH);
    }
}
