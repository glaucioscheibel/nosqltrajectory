package br.udesc.mca.trajectory.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import br.udesc.mca.trajectory.dao.PersistenceAbstractFactory;
import br.udesc.mca.trajectory.dao.PersistenceDAO;
import br.udesc.mca.trajectory.dao.PersistenceModel;
import br.udesc.mca.trajectory.model.Trajectory;
import br.udesc.mca.trajectory.model.TrajectoryPoint;
import br.udesc.mca.trajectory.model.TrajectorySegment;
import br.udesc.mca.trajectory.model.TrajectoryType;
import br.udesc.mca.trajectory.model.TrajectoryVersion;

/**
 * Servico para inserir trajetorias das mais diversas formas nos diferentes
 * modos de persistencia
 */
// TODO: verificar se eh para converter a lat e a long para X e Y ou se X e Y
// sao exatamente a lat e a long (em angulos e nao em pontos)
public class TrajectoryInputService implements TrajectoryInput {

    /**
     * Inclui um ponto em uma nova trajetoria e retorna o ID da trajetoria
     * criada
     * 
     * @param latitude
     *            eixo y
     * @param longitude
     *            eixo x
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public long insertSinglePointWithDateTime(float latitude, float longitude, int ano, int mes, int dia, int hora,
            int min, int seg, String descricao) {
        TrajectoryPoint p1 = new TrajectoryPoint();
        p1.setLng(longitude);
        p1.setLat(latitude);

        Calendar c = Calendar.getInstance();
        Date now = c.getTime();

        c.set(Calendar.YEAR, ano);
        c.set(Calendar.MONTH, mes - 1);
        c.set(Calendar.DAY_OF_MONTH, dia);

        c.set(Calendar.HOUR_OF_DAY, hora);
        c.set(Calendar.MINUTE, min);
        c.set(Calendar.SECOND, seg);
        c.set(Calendar.MILLISECOND, 0);

        p1.setTimestamp(c.getTimeInMillis());

        Trajectory trajectory = new Trajectory();
        trajectory.setDescription(descricao);

        TrajectoryVersion versao = new TrajectoryVersion();
        versao.setVersion(1);
        versao.setType(TrajectoryType.RAW);
        versao.setTrajectory(trajectory);
        versao.setLastModified(now);
        TrajectorySegment segmento = new TrajectorySegment();
        segmento.addPoint(p1);
        versao.addSegment(segmento);

        trajectory.addVersion(versao);
        trajectory.setLastModified(now);

        PersistenceDAO dao = PersistenceAbstractFactory.getPersistenceDAO(PersistenceModel.RELATIONAL);
        trajectory = (Trajectory) dao.store(trajectory);
        dao.close();

        return trajectory.getId();
    }

    /**
     * Iclui um novo ponto em uma trajetoria usando a data/hora atual. Para uma
     * nova trajetoria informar um id < 1 para <code>idTrajetoria</code>
     */
    @SuppressWarnings("unchecked")
    @Override
    public long insertSinglePoint(long idTrajetoria, float latitude, float longitude) {
        PersistenceDAO<Trajectory> dao = PersistenceAbstractFactory.getPersistenceDAO(PersistenceModel.RELATIONAL);

        Trajectory trajectory = null;
        if (idTrajetoria > 0) {
            trajectory = (Trajectory) dao.findById(idTrajetoria);
        } else {
            Random r = new Random();
            long id = r.nextLong();
            if (id < 0)
                id *= -1;
            trajectory = new Trajectory(id); // TODO: implementar AutoIncremento
                                             // ou sequencia nos IDs?
            TrajectoryVersion versao = new TrajectoryVersion();
            versao.setType(TrajectoryType.RAW);
            versao.setVersion(1);

            trajectory.addVersion(versao);
        }

        TrajectoryPoint p = new TrajectoryPoint();
        p.setLng(longitude);
        p.setLat(latitude);
        p.setTimestamp(System.currentTimeMillis());
        Date now = new Date();

        trajectory.setLastModified(now);
        List<TrajectoryVersion> versoes = trajectory.getVersions();
        TrajectoryVersion versao1 = null;

        for (TrajectoryVersion versao : versoes) {
            if (versao.getVersion() == 1) {
                versao1 = versao;
                break;
            }
        }

        TrajectorySegment segmento = new TrajectorySegment();

        segmento.addPoint(p);

        versao1.addSegment(segmento);

        trajectory = (Trajectory) dao.store(trajectory);

        return trajectory.getId();
    }

}
