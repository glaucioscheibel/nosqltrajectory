package br.udesc.mca.modelo.consulta;

import java.util.Collection;

import javax.sql.RowSet;

import org.hibernate.Session;

public class ConsultaDAOPostgreSQL implements ConsultaDAO{

	private Session session;

	public ConsultaDAOPostgreSQL(Session session) {
		this.session = session;
	}

	@Override
	public int inserirConsulta(Consulta consulta) {
		this.session.save(consulta);
		return 0;
	}

	@Override
	public boolean excluirConsulta(long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Consulta selecionarConsulta(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean atualizarConsulta(Consulta consulta) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public RowSet selecionarConsultaRowSet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Consulta> selecionarConsultaCollection() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
