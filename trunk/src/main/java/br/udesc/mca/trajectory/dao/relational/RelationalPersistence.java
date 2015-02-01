package br.udesc.mca.trajectory.dao.relational;

import br.udesc.mca.trajectory.dao.PersistenceDAO;
import br.udesc.mca.trajectory.dao.PersistenceModel;
import br.udesc.mca.trajectory.model.Trajectory;

public abstract class RelationalPersistence extends PersistenceDAO<Trajectory> {
    public static RelationalPersistence getInstance() {
        return (RelationalPersistence) PersistenceDAO.getInstance(PersistenceModel.RELATIONAL);
    }
}
