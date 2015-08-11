package br.udesc.mca.exportador.kml;

import java.io.File;
import java.io.FileNotFoundException;

import de.micromata.opengis.kml.v_2_2_0.Kml;

//http://labs.micromata.de/projects/jak/quickstart.html
//https://developers.google.com/kml/documentation/kml_tut?hl=pt-br
public class GeraKML {

	public static void main(String[] args) {
		Kml kml = new Kml();

		kml.createAndSetPlacemark().withName("Teste").withOpen(Boolean.TRUE).createAndSetPoint()
				.addToCoordinates(-0.126236, 51.500152);

		try {
			kml.marshal(new File("teste.kml"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
