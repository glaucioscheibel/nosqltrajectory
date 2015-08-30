package br.udesc.mca.calculador;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.udesc.mca.conexao.HibernateUtil;
import br.udesc.mca.modelo.segmento.Segmento;

public class DiferencaAzimute {

	public static void main(String[] args) {
		Session sessao = HibernateUtil.getSessionFactory().openSession();

		Query consulta = sessao.getNamedQuery("consultaSegmentoTrajetoria");
		consulta.setBigInteger("trajetoriaId", new BigInteger("1"));
		List<Segmento> resultado = consulta.list();

		for (Segmento segmento : resultado) {
			System.out.println("Teste: " + segmento.getId() + "-" + segmento.getAzimute());
		}

	}

}
