package br.udesc.mca.modelo.subtrajetoria;

import java.util.Collection;

import javax.sql.RowSet;

import org.hibernate.Session;

public class SubTrajetoriaDAOPostgreSQL implements SubTrajetoriaDAO {

	private Session session;

	public SubTrajetoriaDAOPostgreSQL(Session session) {
		this.session = session;
	}

	@Override
	public int inserirSubTrajetoria(SubTrajetoria subTrajetoria) {
		this.session.save(subTrajetoria);
		return 0;
	}

	@Override
	public boolean excluirSubTrajetoria(long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public SubTrajetoria selecionarSubTrajetoria(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean atualizarSubTrajetoria(SubTrajetoria subTrajetoria) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public RowSet selecionarSubTrajetoriaRowSet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<SubTrajetoria> selecionarSubTrajetoriaCollection() {
		// TODO Auto-generated method stub
		return null;
	}

}
