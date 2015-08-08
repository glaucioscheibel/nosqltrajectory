package br.udesc.mca.modelo.segmento;

import java.util.Collection;

import javax.sql.RowSet;

import br.udesc.mca.modelo.ponto.Ponto;

public class SegmentoDAOPostgreSQL implements SegmentoDAO {

	@Override
	public int inserirSegmento(Segmento segmento) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean excluirSegmento(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Ponto selecionarSegmento(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean atualizarSegmento(Segmento segmento) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public RowSet selecionarSegmentoRowSet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Segmento> selecionarSegmentoCollection() {
		// TODO Auto-generated method stub
		return null;
	}

}
