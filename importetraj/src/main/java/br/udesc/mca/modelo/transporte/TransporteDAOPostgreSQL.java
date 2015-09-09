package br.udesc.mca.modelo.transporte;

import java.util.Collection;

import javax.sql.RowSet;

import org.hibernate.Session;

public class TransporteDAOPostgreSQL implements TransporteDAO {

	private Session sessao;

	public TransporteDAOPostgreSQL(Session sessao) {
		this.sessao = sessao;
	}
	
	@Override
	public int inserirTransporte(Transporte transporte) {
		this.sessao.save(transporte);
		return 0;
	}

	@Override
	public boolean excluirTransporte(long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Transporte selecionarTransporte(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean atualizarTransporte(Transporte transporte) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public RowSet selecionarTransporteRowSet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Transporte> selecionarTransporteCollection() {
		// TODO Auto-generated method stub
		return null;
	}

}
