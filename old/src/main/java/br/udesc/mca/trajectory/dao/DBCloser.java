package br.udesc.mca.trajectory.dao;

public class DBCloser extends Thread {

    @Override
    public void run() {
        PersistenceAbstractFactory.closeAll();
    }
}
