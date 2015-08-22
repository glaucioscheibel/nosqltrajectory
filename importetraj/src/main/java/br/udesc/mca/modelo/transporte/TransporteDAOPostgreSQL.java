package br.udesc.mca.modelo.transporte;

import java.util.Collection;

import javax.sql.RowSet;

public class TransporteDAOPostgreSQL implements TransporteDAO {

	@Override
	public int inserirTransporte(Transporte transporte) {
		// TODO Auto-generated method stub
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
