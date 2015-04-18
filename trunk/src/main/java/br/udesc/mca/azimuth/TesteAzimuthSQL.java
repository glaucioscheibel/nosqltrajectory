package br.udesc.mca.azimuth;

import java.util.List;

import br.udesc.mca.trajectory.dao.PersistenceDAO;
import br.udesc.mca.trajectory.dao.PersistenceModel;
import br.udesc.mca.trajectory.model.Trajectory;
import br.udesc.mca.trajectory.model.TrajectoryPoint;
import br.udesc.mca.trajectory.model.TrajectorySegment;
import br.udesc.mca.trajectory.model.TrajectoryVersion;

public class TesteAzimuthSQL {

	public static void main(String[] args) {

		PersistenceDAO<Trajectory> daoRelac = (PersistenceDAO<Trajectory>) PersistenceDAO
				.getInstance(PersistenceModel.RELATIONAL);
		Trajectory trajectory = daoRelac.findById(1);
		List<TrajectoryVersion> listaVersao = (List<TrajectoryVersion>) trajectory
				.getVersions();
		
		for (TrajectoryVersion trajectoryVersion : listaVersao) {
			List<TrajectorySegment> listaSegmento = trajectoryVersion.getSegments();  
			for (TrajectorySegment trajectorySegment : listaSegmento) {
				List<TrajectoryPoint> listaPonto = trajectorySegment.getPoints();				
				for (TrajectoryPoint trajectoryPoint : listaPonto) {
					//
					int cont = 0;
					
				}
			}
		}

	}

}
