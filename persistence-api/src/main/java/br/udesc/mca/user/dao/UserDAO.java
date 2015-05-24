package br.udesc.mca.user.dao;

import br.udesc.mca.trajectory.model.User;

public abstract class UserDAO {
    public abstract void create(User user);
}
