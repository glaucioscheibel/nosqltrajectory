package br.udesc.mca.point.converter;

/**
 * Conversor de dados do ponto. Com base no trabalho em:
 * http://www.movable-type.co.uk/scripts/latlong.html
 * 
 * @since 25/12/2014
 */
public final class PointConverter {

	/**
	 * Converte uma string no formato graus/minutos/segundos em graus númericos.
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
	public static double converterDecimalDegress(String coordinate) {
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
}
