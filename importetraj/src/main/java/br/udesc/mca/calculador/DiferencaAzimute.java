package br.udesc.mca.calculador;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import br.udesc.mca.conexao.HibernateUtil;
import br.udesc.mca.matematica.Azimute;
import br.udesc.mca.modelo.segmento.Segmento;
import br.udesc.mca.modelo.segmento.SegmentoDAOPostgreSQL;
import br.udesc.mca.modelo.trajetoria.Trajetoria;
import br.udesc.mca.modelo.trajetoria.TrajetoriaDAOPostgreSQL;

public class DiferencaAzimute {

	public static void main(String[] args) {
		Session sessao = HibernateUtil.getSessionFactory().openSession();
		Transaction transacao = null;
		TrajetoriaDAOPostgreSQL trajetoriaDAOPostgreSQL = new TrajetoriaDAOPostgreSQL(sessao);
		SegmentoDAOPostgreSQL segmentoDAOPostgreSQL = new SegmentoDAOPostgreSQL(sessao);
		Segmento segmento1 = null;
		Segmento segmento2 = null;
		int contaSegmento = 0;
		double difAzimute = 0;
		double difAzimutePositivo = 0;
		long contaTrajetoria = 0;
		long contaSegmentoTotal = 0;

		List<Trajetoria> trajetorias = (List<Trajetoria>) trajetoriaDAOPostgreSQL.selecionarTrajetoriaCollection();

		for (Trajetoria trajetoria : trajetorias) {
			contaTrajetoria++;
			System.out
					.println(contaTrajetoria + " trajetoria - " + trajetoria.getId() + " - " + trajetoria.getArquivo());
			Query consulta = sessao.getNamedQuery("consultaSegmentoTrajetoria");
			consulta.setLong("trajetoriaId", trajetoria.getId());

			List<Segmento> resultado = consulta.list();

			if (resultado.size() == 1) {
				System.out.println("#Só um segmento: " + trajetoria.getArquivo().trim().toLowerCase());
				continue;
			}

			for (Segmento segmento : resultado) {
				contaSegmentoTotal++;

				System.out.println(contaSegmentoTotal + " segmento - " + segmento.getId());

				if (contaSegmento == 0) {
					if (segmento2 != null) {
						segmento1 = segmento2;
					} else {
						segmento1 = segmento;
					}
					contaSegmento++;
				}
				segmento2 = segmento;

				transacao = sessao.beginTransaction();

				difAzimute = Azimute.diferencaAzimute(segmento1.getAzimute(), segmento2.getAzimute());
				difAzimutePositivo = Azimute.diferencaAzimutePositivo(segmento1.getAzimute(), segmento2.getAzimute());

				segmento1.setDiferencaAzimute(difAzimute);
				segmento1.setDiferencaAzimutePositiva(difAzimutePositivo);
				segmentoDAOPostgreSQL.atualizarSegmento(segmento1);

				contaSegmento = 0;
				transacao.commit();
			}
			segmento1 = null;
			segmento2 = null;
		}
		sessao.close();
	}
}
