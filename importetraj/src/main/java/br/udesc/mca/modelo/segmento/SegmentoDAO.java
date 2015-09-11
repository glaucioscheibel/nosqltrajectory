package br.udesc.mca.modelo.segmento;

import java.util.Collection;

import javax.sql.RowSet;

public interface SegmentoDAO {

	public int inserirSegmento(Segmento segmento);

	public boolean excluirSegmento(long id);

	public Segmento selecionarSegmento(long id);

	public boolean atualizarSegmento(Segmento segmento);

	public RowSet selecionarSegmentoRowSet();

	public Collection<Segmento> selecionarSegmentoCollection();

}
