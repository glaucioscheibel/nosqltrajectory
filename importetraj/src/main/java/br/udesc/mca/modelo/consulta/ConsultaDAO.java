package br.udesc.mca.modelo.consulta;

import java.util.Collection;

import javax.sql.RowSet;

public interface ConsultaDAO {

	public int inserirConsulta(Consulta consulta);

	public boolean excluirConsulta(long id);

	public Consulta selecionarConsulta(long id);

	public boolean atualizarConsulta(Consulta consulta);

	public RowSet selecionarConsultaRowSet();

	public Collection<Consulta> selecionarConsultaCollection();
}
