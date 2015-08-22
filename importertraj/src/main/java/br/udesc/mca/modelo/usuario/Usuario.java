package br.udesc.mca.modelo.usuario;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "usuario")
@NamedQuery(name = "consultaUsuarioDescricao", query = "from Usuario u where u.descricao = :descricao")
public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "gen_usuario", sequenceName = "seq_usuarioid")
	@GeneratedValue(generator = "gen_usuario")
	@Column(name = "id")
	private Long id;

	@Column(length = 50)
	private String descricao;

	@Column(name = "modelo_celular", length = 50)
	private String modeloCelular;

	@Column(name = "versao_android", length = 50)
	private String android;

	@Column(length = 50)
	private String device;

	@Column(name = "versao_coletor", length = 50)
	private String versaoColetor;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getModeloCelular() {
		return modeloCelular;
	}

	public void setModeloCelular(String modeloCelular) {
		this.modeloCelular = modeloCelular;
	}

	public String getAndroid() {
		return android;
	}

	public void setAndroid(String android) {
		this.android = android;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getVersaoColetor() {
		return versaoColetor;
	}

	public void setVersaoColetor(String versaoColetor) {
		this.versaoColetor = versaoColetor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((android == null) ? 0 : android.hashCode());
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((device == null) ? 0 : device.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((modeloCelular == null) ? 0 : modeloCelular.hashCode());
		result = prime * result + ((versaoColetor == null) ? 0 : versaoColetor.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (android == null) {
			if (other.android != null)
				return false;
		} else if (!android.equals(other.android))
			return false;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (device == null) {
			if (other.device != null)
				return false;
		} else if (!device.equals(other.device))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (modeloCelular == null) {
			if (other.modeloCelular != null)
				return false;
		} else if (!modeloCelular.equals(other.modeloCelular))
			return false;
		if (versaoColetor == null) {
			if (other.versaoColetor != null)
				return false;
		} else if (!versaoColetor.equals(other.versaoColetor))
			return false;
		return true;
	}
}
