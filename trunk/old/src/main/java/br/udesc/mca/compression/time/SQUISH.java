package br.udesc.mca.compression.time;

import java.util.ArrayList;

import br.udesc.mca.compression.entities.TrackPoint;

public class SQUISH {

	private SQUISH() {
		throw new UnsupportedOperationException("Instanciation not allowed");
	}

	public static TrackPoint[] compress(TrackPoint[] stream, int bufferSize) {

		if (bufferSize <= 3)
			return stream;

		ArrayList<TrackPoint> buffer = new ArrayList<TrackPoint>();

		for (int i = 0; i < stream.length; i++) {

			buffer.add(stream[i]);

			if (buffer.size() > 2) {
				TrackPoint item = buffer.get(buffer.size() - 2);
				item.setSed(ErrorMetrics.getSynchronizedEuclideanDistance(item,
						buffer.get(buffer.size() - 3),
						buffer.get(buffer.size() - 1)));
			}

			if (buffer.size() > bufferSize) {

				// Find the minor sed in the buffer; first point is not
				// included; last point is not include because its the intent
				// point to get in the buffer;
				int minorSedPosition = 1;
				for (int j = 1; j < buffer.size() - 1; j++) {
					if (buffer.get(j).getSed() < buffer.get(minorSedPosition)
							.getSed())
						minorSedPosition = j;
				}

				// Remove the minor sed and update the neighbors;
				buffer.get(minorSedPosition - 1).setSed(
						buffer.get(minorSedPosition - 1).getSed()
								+ buffer.get(minorSedPosition).getSed());

				buffer.get(minorSedPosition + 1).setSed(
						buffer.get(minorSedPosition + 1).getSed()
								+ buffer.get(minorSedPosition).getSed());

				buffer.remove(minorSedPosition);
			}
		}

		return buffer.toArray(new TrackPoint[buffer.size()]);
	}
}
