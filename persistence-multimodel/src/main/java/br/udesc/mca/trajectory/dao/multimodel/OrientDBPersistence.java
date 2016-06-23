package br.udesc.mca.trajectory.dao.multimodel;

import java.util.List;
import br.udesc.mca.trajectory.model.Trajectory;

public class OrientDBPersistence extends MultiModelPersistence {
    private static OrientDBPersistence instance;
    
    public static OrientDBPersistence getInstance() {
        if (instance == null) {
            instance = new OrientDBPersistence();
        }
        return instance;
    }

    private OrientDBPersistence() {}

    @Override
    public Trajectory store(Trajectory t) {
        return null;
    }

    @Override
    public List<Trajectory> findAll() {
        return null;
    }

    @Override
    public Trajectory findById(long id) {
        return null;
    }

    @Override
    public void deleteById(long id) {}
}
