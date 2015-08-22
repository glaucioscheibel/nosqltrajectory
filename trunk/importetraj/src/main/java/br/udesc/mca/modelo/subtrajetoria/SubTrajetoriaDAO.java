package br.udesc.mca.modelo.subtrajetoria;

import java.util.Collection;

import javax.sql.RowSet;

public interface SubTrajetoriaDAO {

	public int inserirSubTrajetoria(SubTrajetoria subTrajetoria);

	public boolean excluirSubTrajetoria(long id);

	public SubTrajetoria selecionarSubTrajetoria(long id);

	public boolean atualizarSubTrajetoria(SubTrajetoria subTrajetoria);

	public RowSet selecionarSubTrajetoriaRowSet();

	public Collection<SubTrajetoria> selecionarSubTrajetoriaCollection();
	
}
