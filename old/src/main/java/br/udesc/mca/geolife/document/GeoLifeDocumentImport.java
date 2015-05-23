package br.udesc.mca.geolife.document;

import br.udesc.mca.geolife.importer.GeolifeImporter;
import br.udesc.mca.trajectory.dao.PersistenceDAO;
import br.udesc.mca.trajectory.dao.PersistenceModel;

public class GeoLifeDocumentImport extends GeolifeImporter {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        GeoLifeDocumentImport gld = new GeoLifeDocumentImport();
        gld.setDao(PersistenceDAO.getInstance(PersistenceModel.DOCUMENT));
        gld.importData();
    }
}
