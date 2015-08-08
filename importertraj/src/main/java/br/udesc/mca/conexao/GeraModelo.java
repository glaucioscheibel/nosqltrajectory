package br.udesc.mca.conexao;

import org.hibernate.Session;

public class GeraModelo {

	public static void main(String[] args) {
		Session sessao = null;
		sessao = HibernateUtil.getSessionFactory().openSession();
		System.out.println("Conectou!");
		sessao.close();		
	}
}
