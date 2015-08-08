package br.udesc.mca.modelo.ponto;

import java.util.Collection;

import javax.sql.RowSet;

public interface PontoDAO {

	public int inserirPonto(Ponto ponto);

	public boolean excluirPonto(int id);

	public Ponto selecionarPonto(int id);

	public boolean atualizarPonto(Ponto ponto);

	public RowSet selecionarPontoRowSet();

	public Collection<Ponto> selecionarPontoCollection();

}
