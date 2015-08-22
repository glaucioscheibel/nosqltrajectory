package br.udesc.mca.modelo.trajetoria;

import java.util.Collection;
import java.util.List;

import javax.sql.RowSet;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

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
		Transaction transacao = null;
		Query consulta = null;
		List<Trajetoria> resultado = null;
		
		transacao = this.session.beginTransaction();
		consulta = this.session.createQuery("from Trajetoria");
		resultado = consulta.list();
		transacao.commit();
		return resultado;
	}

}
