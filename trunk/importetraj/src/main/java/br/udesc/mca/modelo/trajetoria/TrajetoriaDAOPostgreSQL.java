package br.udesc.mca.modelo.trajetoria;

import java.util.Collection;
import java.util.List;

import javax.sql.RowSet;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

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
		Transaction transacao = null;
		Query consulta = null;
		List<Trajetoria> resultado = null;
		
		transacao = this.sessao.beginTransaction();
		consulta = this.sessao.createQuery("from Trajetoria");
		resultado = consulta.list();
		transacao.commit();
		return resultado;
	}

}
