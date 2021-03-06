package br.udesc.mca.trajectory.dao.relational;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.udesc.mca.trajectory.model.Trajectory;

public class PostgreSQLPersistence extends RelationalPersistence {
    private static PostgreSQLPersistence instance;

    public static PostgreSQLPersistence getInstance() {
        if (instance == null) {
            instance = new PostgreSQLPersistence();
        }
        return instance;
    }

    @Override
    public Trajectory store(Trajectory c) {
        this.log.info("store(" + c + ")");
        EntityManager em = EntityManagerHolder.getInstance().getEntityManager();
        em.getTransaction().begin();
        em.persist(c);
        em.getTransaction().commit();
        return c;
    }

    @Override
    public List<Trajectory> findAll() {
        this.log.info("findAll()");
        EntityManager em = EntityManagerHolder.getInstance().getEntityManager();
        TypedQuery<Trajectory> tq = em.createQuery("select t from Trajectory t", Trajectory.class);
        return tq.getResultList();
    }

    @Override
    public Trajectory findById(long id) {
        this.log.info("findById(" + id + ")");
        EntityManager em = EntityManagerHolder.getInstance().getEntityManager();
        return em.find(Trajectory.class, id);
    }

    @Override
    public void deleteById(long id) {
        this.log.info("deleteById(" + id + ")");
        EntityManager em = EntityManagerHolder.getInstance().getEntityManager();
        em.getTransaction().begin();
        em.remove(this.findById(id));
        em.getTransaction().commit();
    }

    public void deleteAll() {}
}
