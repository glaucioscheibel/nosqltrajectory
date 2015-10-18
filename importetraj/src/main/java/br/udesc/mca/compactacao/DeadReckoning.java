package br.udesc.mca.compactacao;

import br.udesc.mca.matematica.Azimute;
import br.udesc.mca.modelo.ponto.Ponto;

public class DeadReckoning {
	private static double maxError;

	private DeadReckoning() {
		throw new UnsupportedOperationException("Não é possível instanciar a classe!");
	}

	/**
	 * Comprime uma trajetória removendo pontos, usando o algoritmo Dead
	 * Reckoning.
	 * 
	 * @param pontoTrajetoria
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
			deadReckoning(pontoTrajetoria, keep);
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

	private static void deadReckoning(Ponto[] pontoTrajetoria, boolean[] keep) {
		double dist;
		int end = pontoTrajetoria.length - 1;
		keep[0] = true;

		int origin = 0;
		int direction = 1;
		int index = 2;

		for (; index <= end; index++) {
			dist = Azimute.distanciaPerpendicular(pontoTrajetoria[index], pontoTrajetoria[origin],
					pontoTrajetoria[direction]);
			if (dist > maxError) {
				direction = index;
				origin = index - 1;
				keep[origin] = true;
			}
		}
		keep[end] = true;
	}

}
