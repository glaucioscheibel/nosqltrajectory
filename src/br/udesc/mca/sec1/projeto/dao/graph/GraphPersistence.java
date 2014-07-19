package br.udesc.mca.sec1.projeto.dao.graph;

import br.udesc.mca.sec1.projeto.dao.PersistenceDAO;
import br.udesc.mca.sec1.projeto.dao.PersistenceModel;
import br.udesc.mca.sec1.projeto.model.Customer;

public abstract class GraphPersistence extends PersistenceDAO<Customer> {
    public static GraphPersistence getInstance() {
        return (GraphPersistence) PersistenceDAO.getInstance(PersistenceModel.GRAPH);
    }
}
