package br.udesc.mca.trajectory.dao.relational;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import br.udesc.mca.trajectory.model.Trajectory;

public class PostgreSQLPersistence extends RelationalPersistence {
    private static PostgreSQLPersistence instance;

    private EntityManager em;

    public static PostgreSQLPersistence getInstance() {
        if (instance == null) {
            instance = new PostgreSQLPersistence();
        }
        return instance;
    }

    public PostgreSQLPersistence() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("TrajectoryPU");
        this.em = emf.createEntityManager();
    }

    @Override
    public Trajectory store(Trajectory c) {
        this.log.info("store(" + c + ")");
        this.em.getTransaction().begin();
        this.em.persist(c);
        this.em.getTransaction().commit();
        return c;
    }

    @Override
    public List<Trajectory> findAll() {
        this.log.info("findAll()");
        TypedQuery<Trajectory> tq = this.em.createQuery("select t from Trajectory t", Trajectory.class);
        return tq.getResultList();
    }

    @Override
    public Trajectory findById(long id) {
        this.log.info("findById(" + id + ")");
        return this.em.find(Trajectory.class, id);
    }

    @Override
    public void deleteById(long id) {
        this.log.info("deleteById(" + id + ")");
        this.em.getTransaction().begin();
        this.em.remove(this.findById(id));
        this.em.getTransaction().commit();
    }

    public void deleteAll() {}

    @Override
    public void close() {
        if (this.em != null) {
            try {
                this.em.close();
            } catch (Exception e) {}
            this.em = null;
        }
    }
}
