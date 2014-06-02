package br.udesc.mca.compression;

import br.udesc.mca.compression.geo.EarthGeometry;
import br.udesc.mca.compression.support.TrackPoint;


public class DeadReckoning {
	private static double maxError;
    private DeadReckoning() {
        throw new UnsupportedOperationException("Instanciation not allowed");
    }

    /**
     * Compress a track by removing points, using the Dead Reckoning algorithm.
     * @param track points of the track
     * @param epsilon tolerance, in meters
     * @return the points of the compressed track
     */
    public static TrackPoint[] compress(TrackPoint[] track, double epsilon){
    	if (track.length>2){
    		int i;
    		maxError = epsilon;
    		int end = track.length;
	    	boolean[] keep = new boolean[end];
	    	deadReckoning(track, keep);
	        int count = 0;
	        for (i=0; i<end; ++i){
	            if (keep[i]) {
	                ++count;
	            }
	        }
	
	        TrackPoint[] result = new TrackPoint[count];
	        int k = 0;
	        for (i=0; i<end; ++i){
	            if (keep[i]) {
	                result[k] = track[i];
	                k++;
	            }
	        }
	        return result;
    	}
    	return track;
    }

    private static void deadReckoning(TrackPoint[] track, boolean[] keep){
        double dist;
        int end = track.length-1;
		keep[0] = true;
		
		int origin = 0;
		int direction = 1;
		int index = 2;
		
		for (;index<=end;index++){
			dist = EarthGeometry.perpendicularDistance(track[index], track[origin], track[direction]);
			if (dist > maxError){
				direction = index;
				origin = index-1;
				keep[origin] = true;
			}
		}
		keep[end] = true;
    }
}
