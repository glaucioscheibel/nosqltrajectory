package br.udesc.mca.exportador.kml;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.udesc.mca.conexao.HibernateUtil;
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
import de.micromata.opengis.kml.v_2_2_0.atom.Author;

//http://labs.micromata.de/projects/jak/quickstart.html
//https://developers.google.com/kml/documentation/kml_tut?hl=pt-br
//http://jakdemo.googlecode.com/svn-history/r18/trunk/jakdemo/src/main/java/de/micromata/jak/demo/Example1.java
//http://kml4earth.appspot.com/icons.html#pushpin
//https://github.com/micromata/javaapiforkml/blob/master/src/test/java/de/micromata/jak/KmlReferenceApiTest.java

/**
 * Classe responsável por gerar a exportação da base para um arquivo KML.
 * 
 * @since 25/12/2012
 */
public class GeraKML {

	public static final int LineStringFull = 1;
	public static final int LineStringHalf = 2;
	public static final int PointFull = 3;
	public static final int PointHalf = 4;

	public static void main(String[] args) {
		int full = GeraKML.LineStringHalf;
		// boolean lineString = false;
		String base = "tdrive";
		final Kml kml = KmlFactory.createKml();
		final Document documento = kml.createAndSetDocument();
		final Style estilo; // documento.createAndAddStyle().withId("icone");
		final IconStyle iconeEstilo; // estilo.createAndSetIconStyle().withScale(1.0d);//
										// .withColor("003366").withColorMode(ColorMode.NORMAL).withScale(0.7d);

		// iconeEstilo.createAndSetIcon().withHref("http://maps.google.com/mapfiles/kml/paddle/blu-circle.png");
		// iconeEstilo.createAndSetIcon().withHref("http://maps.google.com/mapfiles/kml/paddle/blu-blank-lv.png");

		Session sessao = HibernateUtil.getSessionFactory().openSession();
		TrajetoriaDAOPostgreSQL trajetoriaDAOPostgreSQL = new TrajetoriaDAOPostgreSQL(sessao);

		Query consulta = null;
		List<Trajetoria> resultado = null;
		Trajetoria trajetoria = null;

		switch (full) {
		case GeraKML.LineStringFull:
			consulta = sessao.getNamedQuery("consultaTrajetoriaBase");
			consulta.setString("base", base);
			resultado = consulta.list();

			for (Trajetoria trajetoriaResultado : resultado) {
				List<Ponto> listaPontoTrajetoria = trajetoriaResultado.getPontos();
				List<Coordinate> listaCoordenada = new ArrayList<Coordinate>();
				Coordinate coordenada = null;

				for (Ponto pontoTrajetoria : listaPontoTrajetoria) {
					coordenada = new Coordinate(pontoTrajetoria.getLongitude(), pontoTrajetoria.getLatitude());
					listaCoordenada.add(coordenada);
				}
				documento.createAndAddPlacemark().withName(trajetoriaResultado.getId().toString())
						.createAndSetLineString().withExtrude(true).withTessellate(true)
						.withCoordinates(listaCoordenada);
			}

			try {
				kml.marshal(new File(base + ".kml"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			break;
		case GeraKML.LineStringHalf:
			trajetoria = trajetoriaDAOPostgreSQL.selecionarTrajetoria(9233);

			if (trajetoria != null) {
				List<Ponto> pontos = trajetoria.getPontos();
				List<Coordinate> listaCoordenada = new ArrayList<Coordinate>();
				Coordinate coordenada = null;

				for (Ponto pontoTrajetoria : pontos) {
					coordenada = new Coordinate(pontoTrajetoria.getLongitude(), pontoTrajetoria.getLatitude());
					listaCoordenada.add(coordenada);
				}

				documento.createAndAddPlacemark().createAndSetLineString().withExtrude(true).withTessellate(true)
						.withCoordinates(listaCoordenada);
				try {
					kml.marshal(new File(trajetoria.getId() + "_" + base + "_" + trajetoria.getArquivo() + ".kml"));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			break;
		case GeraKML.PointFull:
			estilo = documento.createAndAddStyle().withId("icone");
			iconeEstilo = estilo.createAndSetIconStyle().withScale(1.0d);// .withColor("003366").withColorMode(ColorMode.NORMAL).withScale(0.7d);

			// iconeEstilo.createAndSetIcon().withHref("http://maps.google.com/mapfiles/kml/paddle/blu-circle.png");
			iconeEstilo.createAndSetIcon().withHref("http://maps.google.com/mapfiles/kml/paddle/grn-blank-lv.png");

			consulta = sessao.getNamedQuery("consultaTrajetoriaBase");
			consulta.setString("base", base);
			resultado = consulta.list();

			for (Trajetoria trajetoriaResultado : resultado) {
				List<Ponto> listaPontoTrajetoria = trajetoriaResultado.getPontos();

				for (Ponto pontoTrajetoria : listaPontoTrajetoria) {
					documento.createAndAddPlacemark().withStyleUrl("#icone").createAndSetPoint()
							.withAltitudeMode(AltitudeMode.CLAMP_TO_GROUND)
							.addToCoordinates(pontoTrajetoria.getLongitude(), pontoTrajetoria.getLatitude());
				}
			}
			try {
				kml.marshal(new File(base + ".kml"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			break;
		case GeraKML.PointHalf:
			estilo = documento.createAndAddStyle().withId("icone");
			iconeEstilo = estilo.createAndSetIconStyle().withScale(1.0d);// .withColor("003366").withColorMode(ColorMode.NORMAL).withScale(0.7d);

			iconeEstilo.createAndSetIcon().withHref("http://maps.google.com/mapfiles/kml/paddle/blu-circle.png");
			// iconeEstilo.createAndSetIcon().withHref("http://maps.google.com/mapfiles/kml/paddle/grn-blank-lv.png");

			trajetoria = trajetoriaDAOPostgreSQL.selecionarTrajetoria(88);

			if (trajetoria != null) {
				List<Ponto> pontos = trajetoria.getPontos();

				for (Ponto pontoTrajetoria : pontos) {
					documento.createAndAddPlacemark().withStyleUrl("#icone").createAndSetPoint()
							.withAltitudeMode(AltitudeMode.CLAMP_TO_GROUND)
							.addToCoordinates(pontoTrajetoria.getLongitude(), pontoTrajetoria.getLatitude());
				}

				try {
					kml.marshal(new File(trajetoria.getId() + "_" + base + "_" + trajetoria.getArquivo() + ".kml"));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			break;
		default:
			break;
		}

		sessao.close();
		System.out.println("Terminou!");
	}
}
