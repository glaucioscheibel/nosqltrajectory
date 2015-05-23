package br.udesc.mca.geolife.keyvalue;

import br.udesc.mca.geolife.importer.GeolifeImporter;
import br.udesc.mca.trajectory.dao.PersistenceDAO;
import br.udesc.mca.trajectory.dao.PersistenceModel;

public class GeoLifeKeyValueImport extends GeolifeImporter {

    public static void main(String[] args) throws Exception {
        GeoLifeKeyValueImport glk = new GeoLifeKeyValueImport();
        glk.setDao(PersistenceDAO.getInstance(PersistenceModel.KEY_VALUE));
        glk.importData();
    }
}
