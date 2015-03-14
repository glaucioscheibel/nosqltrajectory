package br.udesc.mca.geolife.document;

import br.udesc.mca.geolife.importer.GeolifeImporter;
import br.udesc.mca.trajectory.dao.PersistenceDAO;
import br.udesc.mca.trajectory.dao.PersistenceModel;
import br.udesc.mca.trajectory.model.Trajectory;

public class GeoLifeDocumentImport extends GeolifeImporter {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        GeoLifeDocumentImport gld = new GeoLifeDocumentImport();
        gld.setDao((PersistenceDAO<Trajectory>) PersistenceDAO.getInstance(PersistenceModel.DOCUMENT));
        gld.importData();
    }
}
