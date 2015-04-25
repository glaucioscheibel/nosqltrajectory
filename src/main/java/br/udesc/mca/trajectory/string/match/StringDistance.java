package br.udesc.mca.trajectory.string.match;

/**
 * Interface para implementar a dist�ncia entre strings.
 */
public interface StringDistance {

	/**
	 * Retorna um valor entre 0 e 1 baseado em qu�o similar uma string � com a
	 * outra. Retornando 1 se forem identifica e 0 se forem muito diferentes
	 * 
	 * @param s1
	 *            A primeira String.
	 * @param s2
	 *            A segunda String.
	 * @return valor float entre 0 e 1 baseado em qu�o similar uma string � com
	 *         a outra.
	 */
	public float getDistance(String s1, String s2);
}
