package br.udesc.mca.compactacao;

import br.udesc.mca.matematica.Azimute;
import br.udesc.mca.modelo.ponto.Ponto;

public class NormalOpeningWindow {

	private static double maxError;

	private NormalOpeningWindow() {
		throw new UnsupportedOperationException("Instanciation not allowed");
	}

	/**
	 * Comprime uma trajetória removendo pontos, usando o algoritmo Normal
	 * Opening Window.
	 * 
	 * @param pontosTrajetoria
	 *            pontos de uma trajetória
	 * @param epsilon
	 *            tolerância, em metros
	 * @return a trajetória comprimida
	 */
	public static Ponto[] compress(Ponto[] pontoTrajetoria, double epsilon) {
		if (pontoTrajetoria.length > 2) {
			int i;
			maxError = epsilon;
			int end = pontoTrajetoria.length;
			boolean[] keep = new boolean[end];
			normalOpeningWindow(pontoTrajetoria, keep);
			int count = 0;
			for (i = 0; i < end; ++i) {
				if (keep[i]) {
					++count;
				}
			}

			Ponto[] result = new Ponto[count];
			int k = 0;
			for (i = 0; i < end; ++i) {
				if (keep[i]) {
					result[k] = pontoTrajetoria[i];
					k++;
				}
			}
			return result;
		}
		return pontoTrajetoria;
	}

	private static void normalOpeningWindow(Ponto[] pontoTrajetoria, boolean[] keep) {
		double dist;

		keep[0] = true;
		int anchor = 0;
		int floater = 2;

		int end = pontoTrajetoria.length - 1;
		Ponto anchorPt = pontoTrajetoria[anchor];
		int index;
		for (; floater <= end; floater++) {
			for (index = anchor + 1; index < floater; index++) {
				dist = Azimute.distanciaPerpendicular(pontoTrajetoria[index], anchorPt, pontoTrajetoria[floater]);
				if (dist > maxError) {
					keep[index] = true;
					anchor = index;
					anchorPt = pontoTrajetoria[anchor];
					floater = index + 1; // floater já é +1. O outro +1 vem do
											// for
					break;
				}
			}
		}
		keep[end] = true;
	}

}
