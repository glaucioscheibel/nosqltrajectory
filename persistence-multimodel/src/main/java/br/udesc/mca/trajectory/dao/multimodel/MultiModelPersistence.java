package br.udesc.mca.trajectory.dao.multimodel;

import br.udesc.mca.trajectory.dao.PersistenceDAO;
import br.udesc.mca.trajectory.dao.PersistenceModel;

public abstract class MultiModelPersistence extends PersistenceDAO {
    public static MultiModelPersistence getInstance() {
        return (MultiModelPersistence) PersistenceDAO.getInstance(PersistenceModel.MULTI_MODEL);
    }
}
