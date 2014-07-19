package br.udesc.mca.sec1.projeto.dao.column;

import br.udesc.mca.sec1.projeto.dao.PersistenceDAO;
import br.udesc.mca.sec1.projeto.dao.PersistenceModel;
import br.udesc.mca.sec1.projeto.model.Customer;

public abstract class ColumnPersistence extends PersistenceDAO<Customer> {
    public static ColumnPersistence getInstance() {
        return (ColumnPersistence) PersistenceDAO.getInstance(PersistenceModel.COLUMN_FAMILY);
    }
}
