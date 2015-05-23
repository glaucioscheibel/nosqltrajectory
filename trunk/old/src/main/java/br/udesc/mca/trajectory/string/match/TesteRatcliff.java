package br.udesc.mca.trajectory.string.match;

public class TesteRatcliff {

	public static void main(String[] args) {
		Ratcliff teste = new Ratcliff();
		float resultado = teste.RatcliffObershelpSimilarity("alexandre", "aleksander");
		System.out.println(resultado);

	}

}
