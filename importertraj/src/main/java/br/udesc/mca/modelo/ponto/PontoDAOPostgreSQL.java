package br.udesc.mca.modelo.ponto;

import java.util.Collection;

import javax.sql.RowSet;

import org.hibernate.Session;

public class PontoDAOPostgreSQL implements PontoDAO {

	private Session session;

	public PontoDAOPostgreSQL(Session session) {
		this.session = session;
	}

	@Override
	public int inserirPonto(Ponto ponto) {
		this.session.save(ponto);
		return 0;
	}

	@Override
	public boolean excluirPonto(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Ponto selecionarPonto(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean atualizarPonto(Ponto ponto) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public RowSet selecionarPontoRowSet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Ponto> selecionarPontoCollection() {
		// TODO Auto-generated method stub
		return null;
	}

}
