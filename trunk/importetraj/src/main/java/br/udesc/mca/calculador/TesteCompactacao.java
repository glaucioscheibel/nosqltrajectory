package br.udesc.mca.calculador;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import br.udesc.mca.conexao.HibernateUtil;
import br.udesc.mca.matematica.Azimute;
import br.udesc.mca.modelo.ponto.Ponto;
import br.udesc.mca.modelo.trajetoria.Trajetoria;
import br.udesc.mca.modelo.trajetoria.TrajetoriaDAOPostgreSQL;
import de.micromata.opengis.kml.v_2_2_0.AltitudeMode;
import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.IconStyle;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.KmlFactory;
import de.micromata.opengis.kml.v_2_2_0.Style;

public class TesteCompactacao {

	private static final String BeforeOpeningWindow = "BeforeOpeningWindow";
	// private static final String DouglasPeucker = "DouglasPeucker";


	public static void main(String[] args) {

		Session sessao = HibernateUtil.getSessionFactory().openSession();
		TrajetoriaDAOPostgreSQL trajetoriaDAOPostgreSQL = new TrajetoriaDAOPostgreSQL(sessao);

		Trajetoria trajetoria = trajetoriaDAOPostgreSQL.selecionarTrajetoria(31);
		List<Ponto> pontosTrajetoria = trajetoria.getPontos();
		int tamanho = pontosTrajetoria.size();
		int conta = 0;
		Ponto[] pontos = new Ponto[tamanho];
		String spontosOriginal = new String();
		String spontosComprimidos = new String();

		for (Ponto ponto : pontosTrajetoria) {
			pontos[conta] = ponto;
			conta++;
			spontosOriginal += ponto.getId() + ", ";
		}

		/*Ponto[] pontosComprimidosDouglas = DouglasPeuckerNLogN.compress(pontos, 0.06);
		for (int i = 0; i < pontosComprimidosDouglas.length; i++) {
			if (i < pontosComprimidosDouglas.length - 1) {
				spontosComprimidos += pontosComprimidosDouglas[i].getId() + ", ";
			} else {
				spontosComprimidos += pontosComprimidosDouglas[i].getId();
			}
		}*/
		System.out.println("Pontos.....................: " + spontosOriginal);
		//System.out.println("Comprimidos DouglasPeucker.: " + spontosComprimidos);
		TesteCompactacao.exportaKML(trajetoria, pontos, "original");
		//TesteCompactacao.exportaKML(trajetoria, pontosComprimidosDouglas, TesteCompactacao.DouglasPeucker);
		//TesteCompactacao.exportaCVS(trajetoria, pontosComprimidosDouglas, TesteCompactacao.DouglasPeucker);

		spontosComprimidos = "";
		Ponto[] pontosComprimidosBefore = br.udesc.mca.compactacao.BeforeOpeningWindow.compress(pontos, 0.025);
		for (int i = 0; i < pontosComprimidosBefore.length; i++) {
			if (i < pontosComprimidosBefore.length - 1) {
				spontosComprimidos += pontosComprimidosBefore[i].getId() + ", ";
			} else {
				spontosComprimidos += pontosComprimidosBefore[i].getId();
			}
		}
		System.out.println("Comprimidos BeforeOpeningWindow: " + spontosComprimidos);
		TesteCompactacao.exportaKML(trajetoria, pontosComprimidosBefore, TesteCompactacao.BeforeOpeningWindow);
		TesteCompactacao.exportaCVS(trajetoria, pontosComprimidosBefore, TesteCompactacao.BeforeOpeningWindow);

		/*
		 * spontosComprimidos = ""; Ponto[] pontosComprimidosDead =
		 * DeadReckoning.compress(pontos, 0.2); for (int i = 0; i <
		 * pontosComprimidosDead.length; i++) { if (i <
		 * pontosComprimidosDead.length - 1) { spontosComprimidos +=
		 * pontosComprimidosDead[i].getId() + ", "; } else { spontosComprimidos
		 * += pontosComprimidosDead[i].getId(); } } System.out.println(
		 * "Comprimidos dead: " + spontosComprimidos);
		 * TesteCompactacao.exportaKML(trajetoria, pontosComprimidosDead,
		 * "dead");
		 * 
		 * spontosComprimidos = ""; Ponto[] pontosComprimidosNormal =
		 * NormalOpeningWindow.compress(pontos, 0.2); for (int i = 0; i <
		 * pontosComprimidosNormal.length; i++) { if (i <
		 * pontosComprimidosNormal.length - 1) { spontosComprimidos +=
		 * pontosComprimidosNormal[i].getId() + ", "; } else {
		 * spontosComprimidos += pontosComprimidosNormal[i].getId(); } }
		 * System.out.println("Comprimidos norm: " + spontosComprimidos);
		 * TesteCompactacao.exportaKML(trajetoria, pontosComprimidosNormal,
		 * "norm");
		 * 
		 * spontosComprimidos = ""; Ponto[] pontosComprimidosTimeRatio =
		 * OpeningWindowTimeRatio.compress(pontos, 0.02, Fisica.TIME_RATIO); for
		 * (int i = 0; i < pontosComprimidosTimeRatio.length; i++) { if (i <
		 * pontosComprimidosTimeRatio.length - 1) { spontosComprimidos +=
		 * pontosComprimidosTimeRatio[i].getId() + ", "; } else {
		 * spontosComprimidos += pontosComprimidosTimeRatio[i].getId(); } }
		 * System.out.println("Comprimidos time: " + spontosComprimidos);
		 * TesteCompactacao.exportaKML(trajetoria, pontosComprimidosTimeRatio,
		 * "time");
		 */

		sessao.close();

	}

	public static void exportaKML(Trajetoria trajetoria, Ponto[] pontos, String algoritmo) {
		boolean lineString = false;
		final Kml kml = KmlFactory.createKml();
		final Document documento = kml.createAndSetDocument();
		final Style estilo = documento.createAndAddStyle().withId("icone");
		final IconStyle iconeEstilo = estilo.createAndSetIconStyle().withScale(1.0d);// .withColor("003366").withColorMode(ColorMode.NORMAL).withScale(0.7d);

		iconeEstilo.createAndSetIcon().withHref("http://maps.google.com/mapfiles/kml/paddle/blu-circle.png");

		if (trajetoria != null) {
			List<Coordinate> listaCoordenada = new ArrayList<Coordinate>();
			Coordinate coordenada = null;

			for (Ponto pontoTrajetoria : pontos) {
				if (lineString) {
					coordenada = new Coordinate(pontoTrajetoria.getLongitude(), pontoTrajetoria.getLatitude());
					listaCoordenada.add(coordenada);
				} else {
					documento.createAndAddPlacemark().withStyleUrl("#icone").createAndSetPoint()
							.withAltitudeMode(AltitudeMode.CLAMP_TO_GROUND)
							.addToCoordinates(pontoTrajetoria.getLongitude(), pontoTrajetoria.getLatitude());
				}
			}

			if (lineString) {
				documento.createAndAddPlacemark().createAndSetLineString().withExtrude(true).withTessellate(true)
						.withCoordinates(listaCoordenada);
			}
			try {
				kml.marshal(new File(trajetoria.getId() + "-" + trajetoria.getArquivo() + "-" + algoritmo + ".kml"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public static void exportaCVS(Trajetoria trajetoria, Ponto[] pontos, String algoritmo) {
		Ponto pontoA = null;
		Ponto pontoB = null;
		String azimuteTexto = new String();
		double azimuteA = 0;
		double azimuteB = 0;
		double[] azimute = new double[pontos.length - 1];
		double[] difAzimute = new double[azimute.length - 1];

		try {
			FileWriter writer = new FileWriter(trajetoria.getId() + "-" + algoritmo + ".csv");

			for (int i = 0; i < pontos.length; i++) {
				pontoA = pontos[i];

				if (i + 1 == pontos.length) {
					pontoB = null;
				} else {
					pontoB = pontos[i + 1];
				}

				if (pontoB != null) {
					azimute[i] = Azimute.azimute(pontoA.getLatitude(), pontoA.getLongitude(), pontoB.getLatitude(),
							pontoB.getLongitude());
				}
			}

			for (int i = 0; i < azimute.length; i++) {
				azimuteA = azimute[i];
				if (i + 1 == azimute.length) {
					azimuteB = 0;
				} else {
					azimuteB = azimute[i + 1];
				}

				if (azimuteB != 0) {
					difAzimute[i] = Azimute.diferencaAzimutePositivo(azimuteA, azimuteB);
				}
			}

			for (int i = 0; i < difAzimute.length; i++) {
				if (i + 1 == difAzimute.length) {
					azimuteTexto += difAzimute[i];
				} else {
					azimuteTexto += difAzimute[i] + ",";
				}
			}

			writer.append(azimuteTexto);
			writer.append('\n');
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}