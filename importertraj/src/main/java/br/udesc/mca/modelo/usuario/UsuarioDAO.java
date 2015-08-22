package br.udesc.mca.modelo.usuario;

import java.util.Collection;

import javax.sql.RowSet;

public interface UsuarioDAO {

	public int inserirUsuario(Usuario usuario);

	public boolean excluirUsuario(long id);

	public Usuario selecionarUsuario(long id);

	public boolean atualizarUsuario(Usuario usuario);

	public RowSet selecionarUsuarioRowSet();

	public Collection<Usuario> selecionarUsuarioCollection();

}
