package br.udesc.mca.trajectory.dao.column;

import br.udesc.mca.trajectory.dao.PersistenceDAO;
import br.udesc.mca.trajectory.dao.PersistenceModel;
import br.udesc.mca.trajectory.model.Trajectory;

public abstract class ColumnPersistence {
    public static PersistenceDAO getInstance() {
        return PersistenceDAO.getInstance(PersistenceModel.COLUMN_FAMILY);
    }
    public static PersistenceDAO getInstance(Class klass) {
        return PersistenceDAO.getInstance(PersistenceModel.COLUMN_FAMILY, klass);
    }
}
