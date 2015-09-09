package br.udesc.mca.modelo.subtrajetoria;

import java.util.Collection;

import javax.sql.RowSet;

import org.hibernate.Session;

public class SubTrajetoriaDAOPostgreSQL implements SubTrajetoriaDAO {

	private Session sessao;

	public SubTrajetoriaDAOPostgreSQL(Session sessao) {
		this.sessao = sessao;
	}

	@Override
	public int inserirSubTrajetoria(SubTrajetoria subTrajetoria) {
		this.sessao.save(subTrajetoria);
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
