package br.udesc.mca.geolife.keyvalue;

import br.udesc.mca.geolife.importer.GeolifeImporter;
import br.udesc.mca.trajectory.dao.PersistenceDAO;
import br.udesc.mca.trajectory.dao.PersistenceModel;
import br.udesc.mca.trajectory.model.Trajectory;

public class GeoLifeKeyValueJsonImport extends GeolifeImporter {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        GeoLifeKeyValueJsonImport glk = new GeoLifeKeyValueJsonImport();
        glk.setDao((PersistenceDAO<Trajectory>) PersistenceDAO.getInstance(PersistenceModel.KEY_VALUE_JSON));
        glk.importData();
    }
}
