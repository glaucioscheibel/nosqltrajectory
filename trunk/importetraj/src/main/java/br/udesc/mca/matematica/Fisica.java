package br.udesc.mca.matematica;

/**
 * Classe que trabalha com a questão do cálculo de Azimute e Distância em KM.
 * Com base no trabalho em: http://www.movable-type.co.uk/scripts/latlong.html.
 * 
 * @since 25/12/2014
 */
public class Fisica {

	/**
	 * Método para calcular a velocidade média. Os parâmetros passados devem
	 * estar em metros e segundos. Delta T = Instante final - Instante inicial
	 * Delta S = Posição final - Posição inicial
	 * 
	 * @param posicao
	 *            inicial
	 * @param posicao
	 *            final
	 * @param tempo
	 *            inicial
	 * @param tempo
	 *            final
	 * @return A velocidade média
	 */
	public static final double velocidadeMediaSistemaInternacional(double posicaoInicial, double posicaoFinal,
			double tempoInicial, double tempoFinal) {
		double vm = 0;
		double deltaS = 0;
		double deltaT = 0;

		deltaS = tempoFinal - tempoInicial;
		deltaT = posicaoFinal - posicaoInicial;

		if (deltaT != 0) {
			vm = deltaS / deltaT;
		}

		return vm;
	}

	/**
	 * Método para calcular a velocidade média. Os parâmetros passados devem
	 * estar em metros e segundos, conforme o . 
	 * 
	 * @param distanciaPercorrida
	 * @param duracao

	 * @return A velocidade média.
	 */
	public static final double velocidadeMediaSistemaInternacional(double distanciaPercorrida, double duracao) {
		double vm = 0;

		if (duracao != 0) {
			vm = distanciaPercorrida / duracao;
		}
		return vm;
	}
}
