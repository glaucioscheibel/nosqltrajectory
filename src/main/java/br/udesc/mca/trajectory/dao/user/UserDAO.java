package br.udesc.mca.trajectory.dao.user;

import javax.persistence.EntityManager;

import br.udesc.mca.trajectory.dao.relational.EntityManagerHolder;
import br.udesc.mca.trajectory.model.User;

public class UserDAO {

    public void create(User user) {
        EntityManager em = EntityManagerHolder.getInstance().getEntityManager();
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
    }
}
