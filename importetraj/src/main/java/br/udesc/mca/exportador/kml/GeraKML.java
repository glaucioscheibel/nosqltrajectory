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

	public static void main(String[] args) {
		boolean full = false;
		boolean lineString = false;
		String base = "schulz";
		final Kml kml = KmlFactory.createKml();
		final Document documento = kml.createAndSetDocument();
		final Style estilo = documento.createAndAddStyle().withId("icone");
		final IconStyle iconeEstilo = estilo.createAndSetIconStyle().withScale(1.0d);// .withColor("003366").withColorMode(ColorMode.NORMAL).withScale(0.7d);

		iconeEstilo.createAndSetIcon().withHref("http://maps.google.com/mapfiles/kml/paddle/blu-circle.png");
		// iconeEstilo.createAndSetIcon().withHref("http://maps.google.com/mapfiles/kml/paddle/blu-blank-lv.png");

		Session sessao = HibernateUtil.getSessionFactory().openSession();
		TrajetoriaDAOPostgreSQL trajetoriaDAOPostgreSQL = new TrajetoriaDAOPostgreSQL(sessao);

		Query consulta = null;
		List<Trajetoria> resultado = null;

		if (full) {
			consulta = sessao.getNamedQuery("consultaTrajetoriaBase");
			consulta.setString("base", base);
			resultado = consulta.list();

			for (Trajetoria trajetoria : resultado) {
				List<Ponto> listaPontoTrajetoria = trajetoria.getPontos();

				for (Ponto pontoTrajetoria : listaPontoTrajetoria) {
					documento.createAndAddPlacemark().withStyleUrl("#icone").createAndSetPoint()
							.withAltitudeMode(AltitudeMode.CLAMP_TO_GROUND)
							.addToCoordinates(pontoTrajetoria.getLongitude(), pontoTrajetoria.getLatitude());
				}
			}
			try {
				kml.marshal(new File("schulz.kml"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			Trajetoria trajetoria = trajetoriaDAOPostgreSQL.selecionarTrajetoria(61);

			if (trajetoria != null) {
				List<Ponto> pontos = trajetoria.getPontos();
				String valores = new String();
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
					kml.marshal(new File(trajetoria.getId() + "-" + trajetoria.getArquivo() + ".kml"));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}

		}
		sessao.close();
	}
}
