package br.udesc.mca.compression.time;

import java.util.List;

import br.udesc.mca.compression.entities.Trajectory;

public class SpeedDiffThreshold {

	private static double maxError;

	private SpeedDiffThreshold() {
		throw new UnsupportedOperationException("Instanciation not allowed");
	}
	
	public static List<Trajectory> compress(List<Trajectory> trajectories, double maxTimeRatioError){
		return null;
	}	
}
