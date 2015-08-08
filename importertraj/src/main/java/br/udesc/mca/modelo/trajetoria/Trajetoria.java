package br.udesc.mca.modelo.trajetoria;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.vividsolutions.jts.geom.LineString;

import br.udesc.mca.modelo.ponto.Ponto;
import br.udesc.mca.modelo.transporte.Transporte;

@Entity
@Table(name = "trajetoria")
public class Trajetoria implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "transporte_id", foreignKey = @ForeignKey(name = "trajetoria_transporte_id_fk") )
	private Transporte transporte;

	@Column(length = 30)
	private String base;

	@Column(length = 50)
	private String arquivo;

	private Double comprimento;

	private Double duracao;

	private Double velocidade_media;

	private LineString trajetoria;

	@OneToMany(mappedBy = "trajetoria")
	private List<Ponto> pontos;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getArquivo() {
		return arquivo;
	}

	public void setArquivo(String arquivo) {
		this.arquivo = arquivo;
	}

	public Double getComprimento() {
		return comprimento;
	}

	public void setComprimento(Double comprimento) {
		this.comprimento = comprimento;
	}

	public Double getDuracao() {
		return duracao;
	}

	public void setDuracao(Double duracao) {
		this.duracao = duracao;
	}

	public Double getVelocidade_media() {
		return velocidade_media;
	}

	public void setVelocidade_media(Double velocidade_media) {
		this.velocidade_media = velocidade_media;
	}

	public LineString getTrajetoria() {
		return trajetoria;
	}

	public void setTrajetoria(LineString trajetoria) {
		this.trajetoria = trajetoria;
	}

	public List<Ponto> getPontos() {
		return pontos;
	}

	public void setPontos(List<Ponto> pontos) {
		this.pontos = pontos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((arquivo == null) ? 0 : arquivo.hashCode());
		result = prime * result + ((base == null) ? 0 : base.hashCode());
		result = prime * result + ((comprimento == null) ? 0 : comprimento.hashCode());
		result = prime * result + ((duracao == null) ? 0 : duracao.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((pontos == null) ? 0 : pontos.hashCode());
		result = prime * result + ((trajetoria == null) ? 0 : trajetoria.hashCode());
		result = prime * result + ((velocidade_media == null) ? 0 : velocidade_media.hashCode());
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
		Trajetoria other = (Trajetoria) obj;
		if (arquivo == null) {
			if (other.arquivo != null)
				return false;
		} else if (!arquivo.equals(other.arquivo))
			return false;
		if (base == null) {
			if (other.base != null)
				return false;
		} else if (!base.equals(other.base))
			return false;
		if (comprimento == null) {
			if (other.comprimento != null)
				return false;
		} else if (!comprimento.equals(other.comprimento))
			return false;
		if (duracao == null) {
			if (other.duracao != null)
				return false;
		} else if (!duracao.equals(other.duracao))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (pontos == null) {
			if (other.pontos != null)
				return false;
		} else if (!pontos.equals(other.pontos))
			return false;
		if (trajetoria == null) {
			if (other.trajetoria != null)
				return false;
		} else if (!trajetoria.equals(other.trajetoria))
			return false;
		if (velocidade_media == null) {
			if (other.velocidade_media != null)
				return false;
		} else if (!velocidade_media.equals(other.velocidade_media))
			return false;
		return true;
	}

}
