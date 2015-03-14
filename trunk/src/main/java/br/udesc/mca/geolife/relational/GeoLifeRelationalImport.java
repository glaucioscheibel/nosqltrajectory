package br.udesc.mca.geolife.relational;

import br.udesc.mca.geolife.importer.GeolifeImporter;
import br.udesc.mca.trajectory.dao.PersistenceDAO;
import br.udesc.mca.trajectory.dao.PersistenceModel;
import br.udesc.mca.trajectory.dao.user.UserDAO;
import br.udesc.mca.trajectory.model.Trajectory;

public class GeoLifeRelationalImport extends GeolifeImporter {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        GeoLifeRelationalImport gri = new GeoLifeRelationalImport();
        gri.setUserDao(new UserDAO());
        gri.setDao((PersistenceDAO<Trajectory>) PersistenceDAO.getInstance(PersistenceModel.RELATIONAL));
        gri.importData();
    }
}
