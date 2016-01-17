package br.udesc.mca.trajectory.dao.relational;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import br.udesc.mca.trajectory.model.Trajectory;
import br.udesc.mca.trajectory.model.TrajectoryVersion;
import br.udesc.mca.trajectory.model.User;

public class MySQLPersistence extends RelationalPersistence {
    private static MySQLPersistence instance;

    public static MySQLPersistence getInstance() {
        if (instance == null) {
            instance = new MySQLPersistence();
        }
        return instance;
    }
    
    private MySQLPersistence() {
        EntityManagerHolder.getInstance().getEntityManager();
    }

    @Override
    public Trajectory store(Trajectory c) {
        this.log.info("store(" + c + ")");
        EntityManager em = EntityManagerHolder.getInstance().getEntityManager();
        em.getTransaction().begin();
        List<TrajectoryVersion> ltv = c.getVersions();
        for (TrajectoryVersion tv : ltv) {
            User u = tv.getUser();
            User aux = em.find(User.class, u.getId());
            if (aux != null) {
                em.merge(u);
            } else {
                em.persist(u);;
            }
        }
        Trajectory aux = em.find(Trajectory.class, c.getId());
        if (aux != null) {
            em.merge(c);
        } else {
            em.persist(c);
        }
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
