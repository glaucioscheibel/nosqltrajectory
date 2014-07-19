package br.udesc.mca.sec1.projeto.dao.document;

import br.udesc.mca.sec1.projeto.dao.PersistenceDAO;
import br.udesc.mca.sec1.projeto.dao.PersistenceModel;
import br.udesc.mca.sec1.projeto.model.Customer;

public abstract class DocumentPersistence extends PersistenceDAO<Customer> {
    public static DocumentPersistence getInstance() {
        return (DocumentPersistence) PersistenceDAO.getInstance(PersistenceModel.DOCUMENT);
    }
}
