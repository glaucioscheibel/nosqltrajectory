package br.udesc.mca.modelo.usuario;

import java.util.Collection;

import javax.sql.RowSet;

import org.hibernate.Session;

public class UsuarioDAOPostgreSQL implements UsuarioDAO {

	private Session sessao;

	public UsuarioDAOPostgreSQL(Session sessao) {
		this.sessao = sessao;
	}

	@Override
	public int inserirUsuario(Usuario usuario) {
		this.sessao.save(usuario);
		return 0;
	}

	@Override
	public boolean excluirUsuario(long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Usuario selecionarUsuario(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean atualizarUsuario(Usuario usuario) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public RowSet selecionarUsuarioRowSet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Usuario> selecionarUsuarioCollection() {
		// TODO Auto-generated method stub
		return null;
	}

}
