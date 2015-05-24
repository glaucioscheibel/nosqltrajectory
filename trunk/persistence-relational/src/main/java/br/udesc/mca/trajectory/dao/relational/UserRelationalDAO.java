package br.udesc.mca.trajectory.dao.relational;

import javax.persistence.EntityManager;
import br.udesc.mca.trajectory.model.User;
import br.udesc.mca.user.dao.UserDAO;

public class UserRelationalDAO extends UserDAO {

    @Override
    public void create(User user) {
        EntityManager em = EntityManagerHolder.getInstance().getEntityManager();
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
    }
}
