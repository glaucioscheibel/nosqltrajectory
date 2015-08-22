package br.udesc.mca.modelo.subtrajetoria;

import java.io.Serializable;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.vividsolutions.jts.geom.LineString;

import br.udesc.mca.modelo.consulta.Consulta;
import br.udesc.mca.modelo.tipo.StringArrayUserType;

@Entity
@Table(name = "subtrajetoria")
@TypeDefs({ @TypeDef(name = "StringArrayObject", typeClass = StringArrayUserType.class) })
public class SubTrajetoria implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "gen_subtrajetoria", sequenceName = "seq_subtrajetoriaid")
	@GeneratedValue(generator = "gen_subtrajetoria")
	@Column(name = "id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "consulta_id", foreignKey = @ForeignKey(name = "subtrajetoria_consulta_id_fk") )
	private Consulta consulta;

	@Column(columnDefinition = "geometry(LineString,4326)")
	@Type(type = "org.hibernate.spatial.GeometryType")
	private LineString subtrajetoria;

	private Double comprimento;

	private Double duracao;

	@Column(name = "diferenca_azimute", columnDefinition = "varchar(100000)[]")
	@Type(type = "StringArrayObject")
	private String[] trajetoriaDiferencaAzimute;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Consulta getConsulta() {
		return consulta;
	}

	public void setConsulta(Consulta consulta) {
		this.consulta = consulta;
	}

	public LineString getSubtrajetoria() {
		return subtrajetoria;
	}

	public void setSubtrajetoria(LineString subtrajetoria) {
		this.subtrajetoria = subtrajetoria;
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

	public String[] getTrajetoriaDiferencaAzimute() {
		return trajetoriaDiferencaAzimute;
	}

	public void setTrajetoriaDiferencaAzimute(String[] trajetoriaDiferencaAzimute) {
		this.trajetoriaDiferencaAzimute = trajetoriaDiferencaAzimute;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comprimento == null) ? 0 : comprimento.hashCode());
		result = prime * result + ((consulta == null) ? 0 : consulta.hashCode());
		result = prime * result + ((duracao == null) ? 0 : duracao.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((subtrajetoria == null) ? 0 : subtrajetoria.hashCode());
		result = prime * result + Arrays.hashCode(trajetoriaDiferencaAzimute);
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
		SubTrajetoria other = (SubTrajetoria) obj;
		if (comprimento == null) {
			if (other.comprimento != null)
				return false;
		} else if (!comprimento.equals(other.comprimento))
			return false;
		if (consulta == null) {
			if (other.consulta != null)
				return false;
		} else if (!consulta.equals(other.consulta))
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
		if (subtrajetoria == null) {
			if (other.subtrajetoria != null)
				return false;
		} else if (!subtrajetoria.equals(other.subtrajetoria))
			return false;
		if (!Arrays.equals(trajetoriaDiferencaAzimute, other.trajetoriaDiferencaAzimute))
			return false;
		return true;
	}
}