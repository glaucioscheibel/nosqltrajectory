package br.udesc.mca.modelo.trajetoria;

import java.util.Collection;

import javax.sql.RowSet;

public interface TrajetoriaDAO {

	public int inserirTrajetoria(Trajetoria trajetoria);

	public boolean excluirTrajetoria(int id);

	public Trajetoria selecionarrTrajetoria(int id);

	public boolean atualizarTrajetoria(Trajetoria trajetoria);

	public RowSet selecionarTrajetoriaRowSet();

	public Collection<Trajetoria> selecionarTrajetoriaCollection();

}
