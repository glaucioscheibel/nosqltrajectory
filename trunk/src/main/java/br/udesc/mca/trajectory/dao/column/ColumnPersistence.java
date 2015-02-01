package br.udesc.mca.trajectory.dao.column;

import br.udesc.mca.trajectory.dao.PersistenceDAO;
import br.udesc.mca.trajectory.dao.PersistenceModel;
import br.udesc.mca.trajectory.model.Trajectory;

public abstract class ColumnPersistence extends PersistenceDAO<Trajectory> {
    public static ColumnPersistence getInstance() {
        return (ColumnPersistence) PersistenceDAO.getInstance(PersistenceModel.COLUMN_FAMILY);
    }
}
