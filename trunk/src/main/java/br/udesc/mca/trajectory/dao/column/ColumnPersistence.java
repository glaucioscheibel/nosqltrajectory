package br.udesc.mca.trajectory.dao.column;

import br.udesc.mca.sec1.projeto.model.Customer;
import br.udesc.mca.trajectory.dao.PersistenceDAO;
import br.udesc.mca.trajectory.dao.PersistenceModel;

public abstract class ColumnPersistence extends PersistenceDAO<Customer> {
    public static ColumnPersistence getInstance() {
        return (ColumnPersistence) PersistenceDAO.getInstance(PersistenceModel.COLUMN_FAMILY);
    }
}
