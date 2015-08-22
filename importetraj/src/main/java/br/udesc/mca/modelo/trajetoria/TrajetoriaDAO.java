package br.udesc.mca.modelo.trajetoria;

import java.util.Collection;

import javax.sql.RowSet;

public interface TrajetoriaDAO {

	public int inserirTrajetoria(Trajetoria trajetoria);

	public boolean excluirTrajetoria(long id);

	public Trajetoria selecionarrTrajetoria(long id);

	public boolean atualizarTrajetoria(Trajetoria trajetoria);

	public RowSet selecionarTrajetoriaRowSet();

	public Collection<Trajetoria> selecionarTrajetoriaCollection();

}
