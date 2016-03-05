package br.udesc.mca.calculador;

import br.udesc.mca.matematica.Azimute;

public class TesteGeral {

	public static void main(String[] args) throws Exception {
		double distancia = Azimute.calculaDistanciaKM(-22.910547, -43.269951, -22.9105, -43.269966);
		double distancia1 = Azimute.calculaDistanciaMetros(-22.910547, -43.269951, -22.9105, -43.269966, false);

		System.out.println(Math.round(distancia));
		System.out.println(Math.round(distancia * 1000));
		System.out.println(Math.round(distancia1));
		System.out.println(Math.round(distancia1 * 1000));

	}

}
