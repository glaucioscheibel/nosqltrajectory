package br.udesc.mca.modelo.segmento;

import java.util.Collection;

import javax.sql.RowSet;

import org.hibernate.Query;
import org.hibernate.Session;

public class SegmentoDAOPostgreSQL implements SegmentoDAO {

	private Session sessao;

	public SegmentoDAOPostgreSQL(Session sessao) {
		this.sessao = sessao;
	}

	@Override
	public int inserirSegmento(Segmento segmento) {
		this.sessao.save(segmento);
		return 0;
	}

	@Override
	public boolean excluirSegmento(long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Segmento selecionarSegmento(long id) {
		Query consulta = sessao.createQuery("from Segmento where id = :parametro");
		consulta.setLong("parametro", id);
		return (Segmento) consulta.uniqueResult();
	}

	@Override
	public boolean atualizarSegmento(Segmento segmento) {
		this.sessao.update(segmento);
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