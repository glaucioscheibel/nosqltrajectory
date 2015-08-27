package br.udesc.mca.modelo.trajetoria;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.LineString;

import br.udesc.mca.modelo.ponto.Ponto;
import br.udesc.mca.modelo.transporte.Transporte;
import br.udesc.mca.modelo.usuario.Usuario;

@Entity
@Table(name = "trajetoria")
@NamedQuery(name = "consultaTrajetoriaBase", query = "from Trajetoria t where t.base = :base")
public class Trajetoria implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "gen_trajetoria", sequenceName = "seq_trajetoriaid")
	@GeneratedValue(generator = "gen_trajetoria")
	@Column(name = "id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "transporte_id", foreignKey = @ForeignKey(name = "trajetoria_transporte_id_fk") )
	private Transporte transporte;

	@ManyToOne
	@JoinColumn(name = "usuario_id", foreignKey = @ForeignKey(name = "trajetoria_usuario_id_fk") )
	private Usuario usuario;

	@Column(length = 30)
	private String base;

	@Column(length = 50)
	private String arquivo;

	private Double comprimento;

	private Double duracao;

	@Column(name = "velocidade_media")
	private Double velocidadeMedia;

	private Date data;

	// @Column(columnDefinition = "geometry(LineString,4326)")
	// @Type(type = "org.hibernate.spatial.JTSGeometryType")
	@Column(columnDefinition = "geometry(LineString,4326)")
	@Type(type = "org.hibernate.spatial.GeometryType")
	private LineString trajetoria;

	@Column(name = "trajetoria_inversa", columnDefinition = "geometry(LineString,4326)")
	@Type(type = "org.hibernate.spatial.GeometryType")
	private LineString trajetoriaInversa;

	@OneToMany(mappedBy = "trajetoria")
	private List<Ponto> pontos;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Transporte getTransporte() {
		return transporte;
	}

	public void setTransporte(Transporte transporte) {
		this.transporte = transporte;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
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

	public Double getVelocidadeMedia() {
		return velocidadeMedia;
	}

	public void setVelocidadeMedia(Double velocidadeMedia) {
		this.velocidadeMedia = velocidadeMedia;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public LineString getTrajetoria() {
		return trajetoria;
	}

	public void setTrajetoria(LineString trajetoria) {
		this.trajetoria = trajetoria;
	}

	public LineString getTrajetoriaInversa() {
		return trajetoriaInversa;
	}

	public void setTrajetoriaInversa(LineString trajetoriaInversa) {
		this.trajetoriaInversa = trajetoriaInversa;
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
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((duracao == null) ? 0 : duracao.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((pontos == null) ? 0 : pontos.hashCode());
		result = prime * result + ((trajetoria == null) ? 0 : trajetoria.hashCode());
		result = prime * result + ((trajetoriaInversa == null) ? 0 : trajetoriaInversa.hashCode());
		result = prime * result + ((transporte == null) ? 0 : transporte.hashCode());
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
		result = prime * result + ((velocidadeMedia == null) ? 0 : velocidadeMedia.hashCode());
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
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
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
		if (trajetoriaInversa == null) {
			if (other.trajetoriaInversa != null)
				return false;
		} else if (!trajetoriaInversa.equals(other.trajetoriaInversa))
			return false;
		if (transporte == null) {
			if (other.transporte != null)
				return false;
		} else if (!transporte.equals(other.transporte))
			return false;
		if (usuario == null) {
			if (other.usuario != null)
				return false;
		} else if (!usuario.equals(other.usuario))
			return false;
		if (velocidadeMedia == null) {
			if (other.velocidadeMedia != null)
				return false;
		} else if (!velocidadeMedia.equals(other.velocidadeMedia))
			return false;
		return true;
	}
}