package br.udesc.mca.matematica;

import br.udesc.mca.modelo.ponto.Ponto;

/**
 * Classe que trabalha com a questão do cálculo de Azimute e Distância em KM.
 * Com base no trabalho em: http://www.movable-type.co.uk/scripts/latlong.html.
 * 
 * @since 25/12/2014
 */
public class Fisica {

	/**
	 * Valor da distância euclidiana sincronizada
	 */
	public static final int SED = 1;

	/**
	 * Valor da distância time ratio
	 */
	public static final int TIME_RATIO = 2;

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
	 * 
	 * @return A velocidade média.
	 */
	public static final double velocidadeMediaSistemaInternacional(double distanciaPercorrida, double duracao) {
		double vm = 0;

		if (duracao != 0) {
			vm = distanciaPercorrida / duracao;
		}
		return vm;
	}

	/**
	 * Retorna a velocidade média entre dois pontos
	 * 
	 * @param pontoA
	 * @param pontoB
	 * @return A velocidade em metros/segundo
	 */
	public static double velocidadeMediaSistemaInternacionalPonto(Ponto pontoA, Ponto pontoB) {
		double vm = 0;

		double tempo = pontoB.getTempo().getTime() - pontoA.getTempo().getTime();

		if (tempo == 0) {
			return 0;
		}

		tempo = tempo / 1000; // Milissegundos para Segundos
		double distancia = Azimute.calculaDistanciaKM(pontoA.getLatitude(), pontoA.getLongitude(), pontoB.getLatitude(),
				pontoB.getLongitude());

		vm = distancia / tempo;
		return vm;
	}

	/**
	 * Método que cálcula a distância euclidiana sincronizada. Com base na
	 * fórmula:
	 * 
	 * sed(A,B,C) = square root of ((x'B - xB)*(x'B - xB) + ((y'B - yB)*(y'B -
	 * yB)) x'B = xA + vXAC * (tb - ta) y'B = yA + vYAC * (tb - ta) vXAC = (xc -
	 * xa) / (tc - ta) vYAC = (yc - ya) / (tc - ta)
	 * 
	 * @param p
	 * @param a
	 * @param b
	 * 
	 * @return a distância euclidiana sincronizada
	 */
	public static double getSED(Ponto p, Ponto a, Ponto b) {

		double timeRatioA = b.getTempo().getTime() - a.getTempo().getTime();

		if (timeRatioA == 0) {
			return 0;
		}

		double velocityVectorX = (b.getLongitude() - a.getLongitude()) / (timeRatioA);

		double velocityVectorY = (b.getLatitude() - a.getLatitude()) / (timeRatioA);

		double predictedX = a.getLongitude() + velocityVectorX * (p.getTempo().getTime() - a.getTempo().getTime());

		double predictedY = a.getLatitude() + velocityVectorY * (p.getTempo().getTime() - a.getTempo().getTime());

		Ponto predicted = new Ponto();

		predicted.setLatitude(predictedY);
		predicted.setLongitude(predictedX);

		double sed = Azimute.calculaDistanciaKM(p.getLatitude(), p.getLongitude(), predicted.getLatitude(),
				predicted.getLongitude());

		return sed;
	}

	/**
	 * Método que cálcula a distância via time ratio distance.
	 * 
	 * @param p
	 * @param a
	 * @param b
	 * 
	 * @return a distância em metros
	 */
	public static double getTRD(Ponto p, Ponto a, Ponto b) {

		double timeIntervalE = b.getTempo().getTime() - a.getTempo().getTime();

		if (timeIntervalE == 0)
			return 0;
		else {
			double timeIntervalI = p.getTempo().getTime() - a.getTempo().getTime();
			double timeRatio = timeIntervalI / timeIntervalE;

			double xI = a.getLongitude() + (timeRatio * (b.getLongitude() - a.getLongitude()));
			double yI = a.getLatitude() + (timeRatio * (b.getLatitude() - a.getLatitude()));

			Ponto predicted = new Ponto();

			predicted.setLatitude(yI);
			predicted.setLongitude(xI);

			double distance = Azimute.calculaDistanciaKM(p.getLatitude(), p.getLongitude(), predicted.getLatitude(),
					predicted.getLongitude());

			return distance;
		}
	}

}
