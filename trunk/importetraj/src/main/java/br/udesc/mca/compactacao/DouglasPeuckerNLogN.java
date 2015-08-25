package br.udesc.mca.compactacao;

import br.udesc.mca.matematica.Azimute;
import br.udesc.mca.modelo.ponto.Ponto;

public class DouglasPeuckerNLogN {

	private static double maxError;

	public DouglasPeuckerNLogN() {
		throw new UnsupportedOperationException("Não é possível instanciar a classe!");
	}

	/**
	 * Comprime uma trajetória removendo pontos, usando o algoritmo
	 * Douglas-Peucker
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
			int end = pontoTrajetoria.length - 1;
			boolean[] keep = new boolean[end + 1];
			keep[0] = true;
			keep[end] = true;
			DouglasPeuckerNLogN.douglasPeuckerNLogN(pontoTrajetoria, 0, end, keep);
			int count = 0;
			for (i = 0; i <= end; ++i) {
				if (keep[i]) {
					++count;
				}
			}

			Ponto[] result = new Ponto[count];
			int k = 0;
			for (i = 0; i <= end; ++i) {
				if (keep[i]) {
					result[k] = pontoTrajetoria[i];
					k++;
				}
			}
			return result;
		}
		return pontoTrajetoria;
	}

	static Integer[] stack; /* pilha recursiva */
	static int topo; /* apontador da pilha recursiva */

	public static void douglasPeuckerNLogN(Ponto[] V, int i, int j, boolean[] R) { // main
		int splitPos = 0;
		double tmp, dist;
		int k;

		R[i] = true; // "i" de V[i]

		stack = new Integer[V.length / 2];
		topo = -1;
		Stack_Push(j);
		do {
			dist = -1;
			j = stack[topo];

			if (i + 1 < j) {
				for (k = i + 1; k < j; k++) {
					tmp = Azimute.distanciaPerpendicular(V[k], V[i], V[j]);
					if (tmp > dist) {
						dist = tmp; /* limite máximo */
						splitPos = k;
					}
				}
			}
			if (dist > maxError) {
				Stack_Push(splitPos);
			} else {
				R[j] = true; // "j" de V[j] /* sai o segmento Vi para Vtop */
				i = Stack_Pop();
			}
		} while (!Stack_EmptyQ());
	}

	private static void Stack_Push(int e) { /* empilha o elemento na pilha */
		stack[++topo] = e;
	}

	private static int Stack_Pop() { /* pop retira elemento da pilha (zero se tiver vazio) */
		return stack[topo--];
	}

	private static boolean Stack_EmptyQ() { /* A pilha está vazia? */
		return topo < 0;
	}

}
