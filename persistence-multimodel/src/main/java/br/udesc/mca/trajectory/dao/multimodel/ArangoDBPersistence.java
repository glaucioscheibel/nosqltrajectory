package br.udesc.mca.trajectory.dao.multimodel;

import java.util.List;
import br.udesc.mca.trajectory.model.Trajectory;

public class ArangoDBPersistence extends MultiModelPersistence {
    private static ArangoDBPersistence instance;
    
    public static ArangoDBPersistence getInstance() {
        if (instance == null) {
            instance = new ArangoDBPersistence();
        }
        return instance;
    }

    private ArangoDBPersistence() {}

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
