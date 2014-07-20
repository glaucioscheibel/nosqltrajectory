package br.udesc.mca.sec1.projeto.dao.relational;

import br.udesc.mca.sec1.projeto.dao.PersistenceDAO;
import br.udesc.mca.sec1.projeto.dao.PersistenceModel;
import br.udesc.mca.sec1.projeto.model.Customer;

public abstract class RelationalPersistence extends PersistenceDAO<Customer> {
    public static RelationalPersistence getInstance() {
        return (RelationalPersistence) PersistenceDAO.getInstance(PersistenceModel.RELATIONAL);
    }
}
