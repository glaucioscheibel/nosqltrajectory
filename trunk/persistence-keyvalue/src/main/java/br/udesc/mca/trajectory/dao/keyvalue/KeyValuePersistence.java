package br.udesc.mca.trajectory.dao.keyvalue;

import br.udesc.mca.trajectory.dao.PersistenceDAO;
import br.udesc.mca.trajectory.dao.PersistenceModel;

public abstract class KeyValuePersistence extends PersistenceDAO {
    public static KeyValuePersistence getInstance() {
        return (KeyValuePersistence) PersistenceDAO.getInstance(PersistenceModel.KEY_VALUE);
    }
}
