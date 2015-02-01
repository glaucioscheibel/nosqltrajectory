package br.udesc.mca.trajectory.dao.document;

import br.udesc.mca.trajectory.dao.PersistenceDAO;
import br.udesc.mca.trajectory.dao.PersistenceModel;
import br.udesc.mca.trajectory.model.Trajectory;

public abstract class DocumentPersistence extends PersistenceDAO<Trajectory> {
    public static DocumentPersistence getInstance() {
        return (DocumentPersistence) PersistenceDAO.getInstance(PersistenceModel.DOCUMENT);
    }
}
