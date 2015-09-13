package br.udesc.mca.modelo.trajetoria;

import java.util.Collection;
import java.util.List;

import javax.sql.RowSet;

import org.hibernate.Query;
import org.hibernate.Session;

public class TrajetoriaDAOPostgreSQL implements TrajetoriaDAO {

	private Session sessao;

	public TrajetoriaDAOPostgreSQL(Session sessao) {
		this.sessao = sessao;
	}

	@Override
	public int inserirTrajetoria(Trajetoria trajetoria) {
		this.sessao.save(trajetoria);
		return 0;
	}

	@Override
	public boolean excluirTrajetoria(long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Trajetoria selecionarTrajetoria(long id) {
		Query consulta = sessao.createQuery("from Trajetoria where id = :parametro");
		consulta.setLong("parametro", id);
		return (Trajetoria) consulta.uniqueResult();
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
		Query consulta = null;
		List<Trajetoria> resultado = null;
		consulta = this.sessao.createQuery("from Trajetoria");
		resultado = consulta.list();
		return resultado;
	}

}
