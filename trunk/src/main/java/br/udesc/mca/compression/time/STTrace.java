package br.udesc.mca.compression.time;

import java.util.ArrayList;
import java.util.List;

import br.udesc.mca.compression.entities.TrackPoint;

public class STTrace {

	private STTrace() {
		throw new UnsupportedOperationException("Instanciation not allowed");
	}
	
	public static TrackPoint[] compress(TrackPoint[] stream, int memorySize){
		
		double minorSed 	 = 0;
		int minorSedPosition = -1;
		
		List<TrackPoint> memory = new ArrayList<TrackPoint>();
		
		for (int i = 0; i < stream.length; i++) {
			
			double probingSed = 0;
			
			if(i > 2) probingSed = ErrorMetrics.getSynchronizedEuclideanDistance(stream[i], memory.get(memory.size() - 3), memory.get(memory.size() - 1));
			
			if(probingSed < minorSed) continue;
			
			if(memory.size() > memorySize){
				
				minorSed 			= memory.get(1).getSed();
				minorSedPosition 	= 1;
				
				TrackPoint[] memoryArray = memory.toArray(new TrackPoint[memory.size()]);
				
				//Ignore the first and last point; They don't have a calculated SED;
				for (int j = 1; j < memoryArray.length - 1; j++) {
					
					if(memoryArray[j].getSed() < minorSed){
						minorSed 		 = memoryArray[j].getSed();
						minorSedPosition = j;
					}
				}
				
				//Delete the point with the minor sed;
				memory.remove(minorSedPosition);
				
				//Update neighbors' sed (if they exist);
				if(minorSedPosition - 2 >= 0 && minorSedPosition < memory.size()) memory.get(minorSedPosition - 1).setSed(ErrorMetrics.getSynchronizedEuclideanDistance(memory.get(minorSedPosition - 1), memory.get(minorSedPosition - 2), memory.get(minorSedPosition)));
				if(minorSedPosition - 1 >= 0 && minorSedPosition + 1 < memory.size()) memory.get(minorSedPosition).setSed(ErrorMetrics.getSynchronizedEuclideanDistance(memory.get(minorSedPosition), memory.get(minorSedPosition - 1), memory.get(minorSedPosition + 1)));
			}
			
			memory.add(stream[i]);
			
			if(memory.size() > 2){
				memory.get(memory.size() - 2).setSed(ErrorMetrics.getSynchronizedEuclideanDistance(memory.get(memory.size() - 2), memory.get(memory.size() - 3), memory.get(memory.size() - 1)));
			}
		}

		return memory.toArray(new TrackPoint[memory.size()]);
	}
}