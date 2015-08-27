package br.udesc.mca.compactacao;

import br.udesc.mca.matematica.Fisica;
import br.udesc.mca.modelo.ponto.Ponto;

public class OpeningWindowTimeRatio {

	private static double maxError;

	private OpeningWindowTimeRatio() {
		throw new UnsupportedOperationException("Não é possível instanciar a classe!");
	}

	/**
	 * Comprime uma trajetória removendo pontos, usando o algoritmo,
	 * OpeningWindowTimeRatio
	 * 
	 * @param pontosTrajetoria
	 *            pontos de uma trajetória
	 * @param epsilon
	 *            tolerância, em metros
	 * @return a trajetória comprimida
	 */
	public static Ponto[] compress(Ponto[] pontoTrajetoria, double epsilon, int errorMetric) {
		if (pontoTrajetoria.length > 2) {
			int i;
			maxError = epsilon;
			int end = pontoTrajetoria.length;
			boolean[] keep = new boolean[end];

			OpeningWindowTimeRatio.beforeOpeningWindow(pontoTrajetoria, keep, errorMetric);
			int count = 0;
			for (i = 0; i < end; ++i) {
				if (keep[i]) {
					++count;
				}
			}

			Ponto[] resultado = new Ponto[count];
			int k = 0;
			for (i = 0; i < end; ++i) {
				if (keep[i]) {
					resultado[k] = pontoTrajetoria[i];
					k++;
				}
			}
			return resultado;
		}
		return pontoTrajetoria;
	}

	private static void beforeOpeningWindow(Ponto[] pontoTrajetoria, boolean[] keep, int errorMetric) {
		double dist;

		keep[0] = true;
		int anchor = 0;
		int floater = 2;

		int end = pontoTrajetoria.length - 1;
		Ponto anchorPt = pontoTrajetoria[anchor];
		int index;
		for (; floater <= end; floater++) {
			for (index = anchor + 1; index < floater; index++) {

				if (errorMetric == Fisica.SED) {
					dist = Fisica.getSED(pontoTrajetoria[index], anchorPt, pontoTrajetoria[floater]);
				} else {
					dist = Fisica.getTRD(pontoTrajetoria[index], anchorPt, pontoTrajetoria[floater]);
				}

				if (dist > maxError) {
					anchor = floater - 1;
					keep[anchor] = true;
					anchorPt = pontoTrajetoria[anchor];
					// floater já é +1. O outro +1 vem do for
					break;
				}
			}
		}
		keep[end] = true;
	}
}
