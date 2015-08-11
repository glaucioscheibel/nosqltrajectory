package br.udesc.mca.modelo.segmento;

import java.util.Collection;

import javax.sql.RowSet;

import org.hibernate.Session;

import br.udesc.mca.modelo.ponto.Ponto;

public class SegmentoDAOPostgreSQL implements SegmentoDAO {

	private Session session;

	public SegmentoDAOPostgreSQL(Session session) {
		this.session = session;
	}

	@Override
	public int inserirSegmento(Segmento segmento) {
		this.session.save(segmento);
		return 0;
	}

	@Override
	public boolean excluirSegmento(long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Ponto selecionarSegmento(long id) {
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
