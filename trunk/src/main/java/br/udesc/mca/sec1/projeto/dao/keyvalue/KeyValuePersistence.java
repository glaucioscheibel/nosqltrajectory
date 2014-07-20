package br.udesc.mca.sec1.projeto.dao.keyvalue;

import br.udesc.mca.sec1.projeto.dao.PersistenceDAO;
import br.udesc.mca.sec1.projeto.dao.PersistenceModel;
import br.udesc.mca.sec1.projeto.model.Customer;

public abstract class KeyValuePersistence extends PersistenceDAO<Customer> {
    public static KeyValuePersistence getInstance() {
        return (KeyValuePersistence) PersistenceDAO.getInstance(PersistenceModel.KEY_VALUE);
    }
}
