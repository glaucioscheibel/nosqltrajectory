package br.udesc.mca.trajectory.dao.document;

import br.udesc.mca.trajectory.dao.PersistenceDAO;
import br.udesc.mca.trajectory.dao.PersistenceModel;

public abstract class DocumentPersistence extends PersistenceDAO {
    public static DocumentPersistence getInstance() {
        return (DocumentPersistence) PersistenceDAO.getInstance(PersistenceModel.DOCUMENT);
    }
}
