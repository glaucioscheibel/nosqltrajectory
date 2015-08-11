package br.udesc.mca.modelo.trajetoria;

import java.util.Collection;

import javax.sql.RowSet;

import org.hibernate.Session;

public class TrajetoriaDAOPostgreSQL implements TrajetoriaDAO {

	private Session session;

	public TrajetoriaDAOPostgreSQL(Session session) {
		this.session = session;
	}

	@Override
	public int inserirTrajetoria(Trajetoria trajetoria) {
		this.session.save(trajetoria);
		return 0;
	}

	@Override
	public boolean excluirTrajetoria(long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Trajetoria selecionarrTrajetoria(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean atualizarTrajetoria(Trajetoria trajetoria) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public RowSet selecionarTrajetoriaRowSet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Trajetoria> selecionarTrajetoriaCollection() {
		// TODO Auto-generated method stub
		return null;
	}

}
