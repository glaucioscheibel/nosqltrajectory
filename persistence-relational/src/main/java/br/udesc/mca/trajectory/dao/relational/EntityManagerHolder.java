package br.udesc.mca.trajectory.dao.relational;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerHolder {
    private static EntityManagerHolder instance;
    private EntityManager em;

    private EntityManagerHolder() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("TrajectoryPU");
        this.em = emf.createEntityManager();
    }

    public static EntityManagerHolder getInstance() {
        if (instance == null) {
            instance = new EntityManagerHolder();
        }
        return instance;
    }

    public EntityManager getEntityManager() {
        return this.em;
    }

    @Override
    protected void finalize() throws Throwable {
        this.em.close();
        super.finalize();
    }
}
