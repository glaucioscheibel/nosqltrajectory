package br.udesc.mca.calculador;

import br.udesc.mca.matematica.Azimute;

public class TesteGeral {

	public static void main(String[] args) throws Exception {
		double distancia1 = Azimute.calculaDistanciaKM(-26.230674, -48.916587, -26.212370, -48.915916);
		
		System.out.println(Math.round(distancia1*1000));

	}

}
