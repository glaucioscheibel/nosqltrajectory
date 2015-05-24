package br.udesc.mca.trajectory.dao;

import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import br.udesc.mca.trajectory.dao.column.ColumnPersistence;
import br.udesc.mca.trajectory.dao.document.DocumentPersistence;
import br.udesc.mca.trajectory.dao.graph.GraphPersistence;
import br.udesc.mca.trajectory.dao.keyvalue.KeyValuePersistence;
import br.udesc.mca.trajectory.dao.relational.RelationalPersistence;

public final class PersistenceAbstractFactory {
    private static boolean[] dbs;
    protected static Properties persistenceImpl;
    private static Logger log = LoggerFactory.getLogger(PersistenceAbstractFactory.class);

    static {
        dbs = new boolean[PersistenceModel.values().length];
        Runtime.getRuntime().addShutdownHook(new DBCloser());
    }

    private PersistenceAbstractFactory() {}

    public static PersistenceDAO getPersistenceDAO(PersistenceModel model) {
        log.info("Getting model " + model);
        switch (model) {
        case DOCUMENT:
            dbs[PersistenceModel.DOCUMENT.ordinal()] = true;
            return DocumentPersistence.getInstance();
        case KEY_VALUE:
            dbs[PersistenceModel.KEY_VALUE.ordinal()] = true;
            return KeyValuePersistence.getInstance();
        case RELATIONAL:
            dbs[PersistenceModel.RELATIONAL.ordinal()] = true;
            return RelationalPersistence.getInstance();
        case COLUMN_FAMILY:
            dbs[PersistenceModel.COLUMN_FAMILY.ordinal()] = true;
            return ColumnPersistence.getInstance();
        case GRAPH:
            dbs[PersistenceModel.GRAPH.ordinal()] = true;
            return GraphPersistence.getInstance();
        default:
            return null;
        }
    }

    public static void closeAll() {
        log.info("Closing db's");
        for (PersistenceModel model : PersistenceModel.values()) {
            if (dbs[model.ordinal()]) {
                getPersistenceDAO(model).close();
            }
        }
    }
}
