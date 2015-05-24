package br.udesc.mca.geolife.relational;

import br.udesc.mca.geolife.importer.GeolifeImporter;
import br.udesc.mca.trajectory.dao.PersistenceDAO;
import br.udesc.mca.trajectory.dao.PersistenceModel;
import br.udesc.mca.trajectory.dao.relational.UserRelationalDAO;

public class GeoLifeRelationalImport extends GeolifeImporter {

    public static void main(String[] args) throws Exception {
        GeoLifeRelationalImport gri = new GeoLifeRelationalImport();
        gri.setUserDao(new UserRelationalDAO());
        gri.setDao(PersistenceDAO.getInstance(PersistenceModel.RELATIONAL));
        gri.importData();
    }
}
