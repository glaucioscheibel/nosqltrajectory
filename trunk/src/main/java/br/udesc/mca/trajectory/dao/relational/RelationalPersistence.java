package br.udesc.mca.trajectory.dao.relational;

import br.udesc.mca.sec1.projeto.model.Customer;
import br.udesc.mca.trajectory.dao.PersistenceDAO;
import br.udesc.mca.trajectory.dao.PersistenceModel;

public abstract class RelationalPersistence extends PersistenceDAO<Customer> {
    public static RelationalPersistence getInstance() {
        return (RelationalPersistence) PersistenceDAO.getInstance(PersistenceModel.RELATIONAL);
    }
}
