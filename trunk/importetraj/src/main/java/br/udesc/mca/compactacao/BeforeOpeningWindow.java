package br.udesc.mca.compactacao;

import br.udesc.mca.matematica.Azimute;
import br.udesc.mca.modelo.ponto.Ponto;

public class BeforeOpeningWindow {

	private static double maxError;

	private BeforeOpeningWindow() {
		throw new UnsupportedOperationException("Não é possível instanciar a classe!");
	}

	/**
	 * Comprime uma trajetória removendo pontos, usando o algoritmo Before
	 * Opening Window
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

			BeforeOpeningWindow.beforeOpeningWindow(pontoTrajetoria, keep);
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

	private static void beforeOpeningWindow(Ponto[] pontoTrajetoria, boolean[] keep) {
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