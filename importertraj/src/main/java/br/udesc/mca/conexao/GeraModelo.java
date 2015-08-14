package br.udesc.mca.conexao;

import org.hibernate.Session;

//lembrar antes de executar o gerador do modelo,
//de criar a extensão espacial no banco alvo.
//Exemplo a seguir no PostgreSQL
//PostgreSQL = CREATE EXTENSION postgis;

/**
 * Classe responsável por gerar o modelo de trajetórias.
 * 
 * @since 25/12/2012
 */
public class GeraModelo {

	public static void main(String[] args) {
		Session sessao = null;
		sessao = HibernateUtil.getSessionFactory().openSession();
		System.out.println("Conectou e gerou o modelo!");
		sessao.close();
	}
}
