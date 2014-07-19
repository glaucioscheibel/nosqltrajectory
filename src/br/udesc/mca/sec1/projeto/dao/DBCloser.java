package br.udesc.mca.sec1.projeto.dao;

public class DBCloser extends Thread {

    @Override
    public void run() {
        PersistenceAbstractFactory.closeAll();
    }
}
