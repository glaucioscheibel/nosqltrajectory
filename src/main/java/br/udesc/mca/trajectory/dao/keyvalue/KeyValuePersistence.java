package br.udesc.mca.trajectory.dao.keyvalue;

import br.udesc.mca.trajectory.dao.PersistenceDAO;
import br.udesc.mca.trajectory.dao.PersistenceModel;
import br.udesc.mca.trajectory.model.Trajectory;

public abstract class KeyValuePersistence extends PersistenceDAO<Trajectory> {
    public static KeyValuePersistence getInstance() {
        return (KeyValuePersistence) PersistenceDAO.getInstance(PersistenceModel.KEY_VALUE);
    }
}
