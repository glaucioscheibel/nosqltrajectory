package br.udesc.mca.modelo.transporte;

import java.util.Collection;

import javax.sql.RowSet;

public interface TransporteDAO {

	public int inserirTransporte(Transporte transporte);

	public boolean excluirTransporte(int id);

	public Transporte selecionarTransporte(int id);

	public boolean atualizarTransporte(Transporte transporte);

	public RowSet selecionarTransporteRowSet();

	public Collection<Transporte> selecionarTransporteCollection();

}
