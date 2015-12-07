package br.udesc.mca.exportador.csv;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.hibernate.Session;

import br.udesc.mca.conexao.HibernateUtil;
import br.udesc.mca.modelo.ponto.Ponto;
import br.udesc.mca.modelo.trajetoria.Trajetoria;
import br.udesc.mca.modelo.trajetoria.TrajetoriaDAOPostgreSQL;

public class GeraCSV {

	public static void main(String[] args) {
		Session sessao = HibernateUtil.getSessionFactory().openSession();
		TrajetoriaDAOPostgreSQL trajetoriaDAOPostgreSQL = new TrajetoriaDAOPostgreSQL(sessao);
		FileWriter arquivoCVS = null;

		Trajetoria trajetoria = trajetoriaDAOPostgreSQL.selecionarTrajetoria(39);

		if (trajetoria != null) {
			try {
				List<Ponto> pontos = trajetoria.getPontos();
				arquivoCVS = new FileWriter(trajetoria.getId() + "_" + trajetoria.getBase() + ".csv");
				arquivoCVS.append("latitude,longitude\n");
				for (Ponto ponto : pontos) {
					arquivoCVS.append(ponto.getLatitude() + "," + ponto.getLongitude() + "\n");
				}
				arquivoCVS.flush();
				arquivoCVS.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		sessao.close();
		System.out.println("Terminou!");

	}
}
