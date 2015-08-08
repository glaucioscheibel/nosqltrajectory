package br.udesc.mca.modelo.segmento;

import java.util.Collection;

import javax.sql.RowSet;

import br.udesc.mca.modelo.ponto.Ponto;

public interface SegmentoDAO {

	public int inserirSegmento(Segmento segmento);

	public boolean excluirSegmento(int id);

	public Ponto selecionarSegmento(int id);

	public boolean atualizarSegmento(Segmento segmento);

	public RowSet selecionarSegmentoRowSet();

	public Collection<Segmento> selecionarSegmentoCollection();

}
