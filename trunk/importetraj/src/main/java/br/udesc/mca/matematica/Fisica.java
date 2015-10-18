package br.udesc.mca.matematica;

import java.sql.Timestamp;

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
	 * estar em metros e segundos.
	 * 
	 * @param distanciaPercorrida
	 * @param duracao
	 * @param arredondamento
	 * 
	 * @return A velocidade média.
	 */
	public static final double velocidadeMediaSistemaInternacional(double distanciaPercorrida, double duracao,
			boolean arredondamento) {
		double vm = 0;

		if (duracao != 0) {
			vm = distanciaPercorrida / duracao;
		}

		if (arredondamento) {
			vm = Math.round(vm);
		}
		return vm;
	}

	/**
	 * Retorna a velocidade média entre dois pontos com base em metros por
	 * segundo
	 * 
	 * @param pontoA
	 * @param pontoB
	 * @param arredondamento
	 * 
	 * @return A velocidade em metros/segundo
	 */
	public static double velocidadeMediaSistemaInternacionalMPSPonto(Ponto pontoA, Ponto pontoB,
			boolean arredondamento) {
		double vm = 0;

		double tempo = pontoB.getTempo().getTime() - pontoA.getTempo().getTime();

		if (tempo == 0) {
			return 0;
		}

		tempo = tempo / 1000; // Milissegundos para Segundos
		double distancia = Azimute.calculaDistanciaMetros(pontoA.getLatitude(), pontoA.getLongitude(),
				pontoB.getLatitude(), pontoB.getLongitude(), true);

		vm = distancia / tempo;
		
		if (arredondamento) {
			vm = Math.round(vm);
		}
		
		return vm;
	}

	/**
	 * Tempo em segundos entre dois ponto B-A.
	 * 
	 * @param pontoA
	 * @param pontoB
	 * @param arredondamento
	 * 
	 * @return Segundos entre dois pontos
	 */
	public static double duracaoSegundos(Timestamp pontoA, Timestamp pontoB, boolean arredondamento) {
		double duracao = 0;

		duracao = ((pontoB.getTime() - pontoA.getTime()) / 1000) % 60;

		if (arredondamento) {
			duracao = Math.round(duracao);
		}

		return duracao;
	}

	/**
	 * Tempo em segundos entre dois ponto B-A.
	 * 
	 * @param pontoA
	 * @param pontoB
	 * @param arredondamento
	 * 
	 * @return Segundos entre dois pontos
	 */
	public static double duracaoSegundos(String tempoCorrido, boolean arredondamento) {
		double duracao = 0;

		duracao = ((Double.parseDouble(tempoCorrido) / 1000) % 60);

		if (arredondamento) {
			duracao = Math.round(duracao);
		}

		return duracao;
	}

	/**
	 * Retorna a velocidade média entre dois pontos kilometros por hora
	 * 
	 * @param pontoA
	 * @param pontoB
	 * @return A velocidade em metros/segundo
	 */
	public static double velocidadeMediaSistemaInternacionalKMHPonto(Ponto pontoA, Ponto pontoB) {
		double vm = 0;

		double tempo = pontoB.getTempo().getTime() - pontoA.getTempo().getTime();

		if (tempo == 0) {
			return 0;
		}

		tempo = tempo / 1000; // Milissegundos para Segundos
		double distancia = Azimute.calculaDistanciaKM(pontoA.getLatitude(), pontoA.getLongitude(), pontoB.getLatitude(),
				pontoB.getLongitude());

		vm = distancia / tempo;
		vm = (vm * 3600.0) / 1000.0;
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

	/**
	 * sed(A,B,C) = square root of ((x'B - xB)*(x'B - xB) + ((y'B - yB)*(y'B -
	 * yB)) x'B = xA + vXAC * (tb - ta) y'B = yA + vYAC * (tb - ta) vXAC = (xc -
	 * xa) / (tc - ta) vYAC = (yc - ya) / (tc - ta)
	 * 
	 * @param ponto
	 *            (B)
	 * @param pontoInicial
	 *            (A)
	 * @param pontoFinal
	 *            (C)
	 * @return
	 */
	public static double getDistanciaEuclidianaCartesiadaSincronizada(Ponto ponto, Ponto pontoInicial,
			Ponto pontoFinal) {

		double timeRatioA = pontoFinal.getTempo().getTime() - pontoInicial.getTempo().getTime();

		if (timeRatioA == 0)
			return 0;

		Ponto datum = pontoInicial;

		double velocityVectorX = (Fisica.getProjectedX(pontoFinal, datum) - Fisica.getProjectedX(pontoInicial, datum))
				/ (timeRatioA);

		double velocityVectorY = (Fisica.getProjectedY(pontoFinal, datum) - Fisica.getProjectedY(pontoInicial, datum))
				/ (timeRatioA);

		double predictedX = getProjectedX(pontoInicial, datum)
				+ velocityVectorX * (ponto.getTempo().getTime() - pontoInicial.getTempo().getTime());

		double predictedY = getProjectedY(pontoInicial, datum)
				+ velocityVectorY * (ponto.getTempo().getTime() - pontoInicial.getTempo().getTime());

		double sed = Fisica.getPointsDistance(getProjectedX(ponto, datum), getProjectedY(ponto, datum), predictedX,
				predictedY);

		return sed;
	}

	public static double getDistanciaCartesianaTimeRatio(Ponto ponto, Ponto pontoInicial, Ponto pontoFinal) {

		double timeIntervalE = pontoFinal.getTempo().getTime() - pontoInicial.getTempo().getTime();

		if (timeIntervalE == 0)
			return 0;
		else {

			Ponto datum = pontoInicial;

			double timeIntervalI = ponto.getTempo().getTime() - pontoInicial.getTempo().getTime();
			double timeRatio = timeIntervalI / timeIntervalE;

			double xI = getProjectedX(pontoInicial, datum)
					+ (timeRatio * (getProjectedX(pontoFinal, datum) - getProjectedX(pontoInicial, datum)));
			double yI = getProjectedY(pontoInicial, datum)
					+ (timeRatio * (getProjectedY(pontoFinal, datum) - getProjectedY(pontoInicial, datum)));

			return Fisica.getPointsDistance(getProjectedX(ponto, datum), getProjectedY(ponto, datum), xI, yI);
		}
	}

	/**
	 * Calcula a projeção de X com base na latitude/longitude em um plano
	 * cartesiano baseado em um ponto datum (referência).
	 * 
	 * @see http://abe-research.illinois.edu/courses/tsm352/lectures/
	 *      Lat_Long_Conversion.pptx
	 * 
	 * @param ponto
	 *            (Lat/Lon)
	 * @param datum
	 *            (Lat/Long) - The minor long point in the observed trajectory,
	 *            so all values will be positive in relation to it's value.
	 * @return projected X (in meters)
	 * 
	 */
	public static double getProjectedX(Ponto ponto, Ponto datum) {
		return ((ponto.getLongitude() - datum.getLongitude())
				* ((Math.PI / 180) * Azimute.RAIO_TERRA_KM * Math.cos(ponto.getLatitude() * (Math.PI / 180))));
	}

	/**
	 * Calcula a projeção de Y com base na latitude/longitude em um plano
	 * cartesiano baseado em um ponto datum (referência).
	 * 
	 * @see http://abe-research.illinois.edu/courses/tsm352/lectures/
	 *      Lat_Long_Conversion.pptx
	 * 
	 * @param ponto
	 *            (Lat/Lon)
	 * @param datum
	 *            (Lat/Long) - The minor long point in the observed trajectory,
	 *            so all values will be positive in relation to it's value.
	 * @return projeção em Y (em metros)
	 * 
	 */
	public static double getProjectedY(Ponto ponto, Ponto datum) {
		return (ponto.getLatitude() - datum.getLatitude()) * (Math.PI / 180) * Azimute.RAIO_TERRA_KM;
	}

	public static double getPointsDistance(double ax, double ay, double bx, double by) {
		return Math.sqrt(Math.pow(ax - bx, 2) + Math.pow(ay - by, 2));
	}

}
