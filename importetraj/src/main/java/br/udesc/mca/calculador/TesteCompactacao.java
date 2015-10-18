package br.udesc.mca.calculador;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import br.udesc.mca.compactacao.BeforeOpeningWindow;
import br.udesc.mca.compactacao.DeadReckoning;
import br.udesc.mca.compactacao.DouglasPeuckerNLogN;
import br.udesc.mca.compactacao.NormalOpeningWindow;
import br.udesc.mca.compactacao.OpeningWindowTimeRatio;
import br.udesc.mca.conexao.HibernateUtil;
import br.udesc.mca.matematica.Fisica;
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

	public static void main(String[] args) {

		Session sessao = HibernateUtil.getSessionFactory().openSession();
		TrajetoriaDAOPostgreSQL trajetoriaDAOPostgreSQL = new TrajetoriaDAOPostgreSQL(sessao);

		Trajetoria trajetoria = trajetoriaDAOPostgreSQL.selecionarTrajetoria(61);
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

		Ponto[] pontosComprimidosDouglas = DouglasPeuckerNLogN.compress(pontos, 0.2);
		for (int i = 0; i < pontosComprimidosDouglas.length; i++) {
			if (i < pontosComprimidosDouglas.length - 1) {
				spontosComprimidos += pontosComprimidosDouglas[i].getId() + ", ";
			} else {
				spontosComprimidos += pontosComprimidosDouglas[i].getId();
			}
		}
		System.out.println("Pontos..........: " + spontosOriginal);
		System.out.println("Comprimidos doug: " + spontosComprimidos);
		TesteCompactacao.exportaKML(trajetoria, pontos, "original");
		TesteCompactacao.exportaKML(trajetoria, pontosComprimidosDouglas, "doug");

		spontosComprimidos = "";
		Ponto[] pontosComprimidosBefore = BeforeOpeningWindow.compress(pontos, 0.2);
		for (int i = 0; i < pontosComprimidosBefore.length; i++) {
			if (i < pontosComprimidosBefore.length - 1) {
				spontosComprimidos += pontosComprimidosBefore[i].getId() + ", ";
			} else {
				spontosComprimidos += pontosComprimidosBefore[i].getId();
			}
		}
		System.out.println("Comprimidos beow: " + spontosComprimidos);
		TesteCompactacao.exportaKML(trajetoria, pontosComprimidosBefore, "beow");

		spontosComprimidos = "";
		Ponto[] pontosComprimidosDead = DeadReckoning.compress(pontos, 0.2);
		for (int i = 0; i < pontosComprimidosDead.length; i++) {
			if (i < pontosComprimidosDead.length - 1) {
				spontosComprimidos += pontosComprimidosDead[i].getId() + ", ";
			} else {
				spontosComprimidos += pontosComprimidosDead[i].getId();
			}
		}
		System.out.println("Comprimidos dead: " + spontosComprimidos);
		TesteCompactacao.exportaKML(trajetoria, pontosComprimidosDead, "dead");

		spontosComprimidos = "";
		Ponto[] pontosComprimidosNormal = NormalOpeningWindow.compress(pontos, 0.2);
		for (int i = 0; i < pontosComprimidosNormal.length; i++) {
			if (i < pontosComprimidosNormal.length - 1) {
				spontosComprimidos += pontosComprimidosNormal[i].getId() + ", ";
			} else {
				spontosComprimidos += pontosComprimidosNormal[i].getId();
			}
		}
		System.out.println("Comprimidos norm: " + spontosComprimidos);
		TesteCompactacao.exportaKML(trajetoria, pontosComprimidosNormal, "norm");

		spontosComprimidos = "";
		Ponto[] pontosComprimidosTimeRatio = OpeningWindowTimeRatio.compress(pontos, 0.02, Fisica.TIME_RATIO);
		for (int i = 0; i < pontosComprimidosTimeRatio.length; i++) {
			if (i < pontosComprimidosTimeRatio.length - 1) {
				spontosComprimidos += pontosComprimidosTimeRatio[i].getId() + ", ";
			} else {
				spontosComprimidos += pontosComprimidosTimeRatio[i].getId();
			}
		}
		System.out.println("Comprimidos time: " + spontosComprimidos);
		TesteCompactacao.exportaKML(trajetoria, pontosComprimidosTimeRatio, "time");

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

}