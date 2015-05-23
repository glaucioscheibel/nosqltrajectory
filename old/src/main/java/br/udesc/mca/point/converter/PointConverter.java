package br.udesc.mca.point.converter;

import static org.apache.commons.math3.util.FastMath.abs;
import static org.apache.commons.math3.util.FastMath.floor;
import static org.apache.commons.math3.util.Precision.round;

/**
 * Conversor de dados do ponto. Com base no trabalho em:
 * http://www.movable-type.co.uk/scripts/latlong.html
 * 
 * @since 25/12/2014
 */
public final class PointConverter {

	public static final String degree = "d";
	public static final String degreeMinute = "dm";
	public static final String degreeMinuteSecond = "dms";

	/**
	 * Converte uma string no formato graus/minutos/segundos em graus decimais.
	 * 
	 * Este método é muito flexível quanto a coordenada informada, permitindo
	 * graus com sinal, ou gra-min-sec com um sufixo de direção (NSEW). Uma
	 * variadade de separadores são aceitos (ex -3° 37' 09"S, 73 59 11W).
	 * Segundos e minutos podem ser omitidos.
	 *
	 * @param coordinate
	 *            Graus ou gra/min/sec em vários formatos.
	 * @returns graus númericos.
	 */
	public static double converterToDecimalDegree(String coordinate) {
		double degree = 0;
		String aux = null;
		String[] vectorCoordinate = {};

		aux = coordinate;
		aux = aux.trim();
		aux = aux.replaceAll("^-", "");
		aux = aux.replaceAll("(?i)[NSELWO]$", ""); // aplicando case sensitive
													// via
													// (?i)

		vectorCoordinate = aux.split("[^0-9.,]+");

		switch (vectorCoordinate.length) {
		case 3: // trabalhando com três partes g/m/s
			degree = Double.parseDouble(vectorCoordinate[0]) / 1
					+ Double.parseDouble(vectorCoordinate[1]) / 60
					+ Double.parseDouble(vectorCoordinate[2]) / 3600;
			break;
		case 2:
			degree = Double.parseDouble(vectorCoordinate[0]) / 1
					+ Double.parseDouble(vectorCoordinate[1]) / 60;
			break;
		case 1:
			degree = Double.parseDouble(vectorCoordinate[0]);
			break;
		default:
			return 0;
		}

		// Se apresentar o ponto cardial S, O ou W o sinal é negativo
		if (coordinate.trim().startsWith("-")
				|| coordinate.trim().toUpperCase().endsWith("S")
				|| coordinate.trim().toUpperCase().endsWith("W")
				|| coordinate.trim().toUpperCase().endsWith("O")) {
			degree = -degree;
		}

		return degree;
	}

	/**
	 * Converte de graus decimais para graus/minutos/segundos.
	 * 
	 * @param degree
	 *            - Grau a ser convertido.
	 * @param format
	 *            - Retorna o valor como 'd', 'dm', 'dms' for grau, grau+minuto,
	 *            grau+minuto+segundo.
	 * @param decimalPlaces
	 *            - Número de casas decimais – padrão 0 podendo ainda aceitar 2
	 *            ou 4 casas.
	 * @returns Graus formatados em graus/minutos/segundos de acordo com o
	 *          fomato informado.
	 */

	public static String converterToDegreeMinuteSecond(double degree,
			String format, short decimalPlaces) {
		String dms, d, m, s;
		double min, sec = 0;

		if (format == null) {
			format = "dms";
		}

		if (decimalPlaces != 0 && decimalPlaces != 2 && decimalPlaces != 4) {
			decimalPlaces = 4;
		}

		degree = abs(degree);

		if (format.equalsIgnoreCase(PointConverter.degree)) {
			d = ((Double) round(degree, decimalPlaces)).toString();

			if (new Double(d).doubleValue() < 100) {
				d = "0" + d;
			}

			if (new Double(d).doubleValue() < 10) {
				d = "0" + d;
			}

			dms = d + '°';

		} else if (format.equalsIgnoreCase(PointConverter.degreeMinute)) {

			min = round((degree * 60), decimalPlaces);

			d = ((Double) floor(min / 60)).toString();

			m = ((Double) round((min % 60), decimalPlaces)).toString();

			if (new Double(d).doubleValue() < 100) {
				d = "0" + d;
			}

			if (new Double(d).doubleValue() < 10) {
				d = "0" + d;
			}

			if (new Double(m).doubleValue() < 10) {
				m = '0' + m;
			}
			dms = d + '°' + m + '′';

		} else {
			sec = round((degree * 3600), decimalPlaces);

			d = ((Double) floor(sec / 3600)).toString();

			m = ((Double) (floor(sec / 60) % 60)).toString();

			s = ((Double) round(floor(sec % 60), decimalPlaces)).toString();

			if (new Double(d).doubleValue() < 100) {
				d = "0" + d;
			}

			if (new Double(d).doubleValue() < 10) {
				d = "0" + d;
			}

			if (new Double(m).doubleValue() < 10) {
				m = '0' + m;
			}

			if (new Double(s).doubleValue() < 10) {
				m = '0' + s;
			}

			dms = d + '°' + m + '′' + s + '″';

		}

		return dms;
	}
}
