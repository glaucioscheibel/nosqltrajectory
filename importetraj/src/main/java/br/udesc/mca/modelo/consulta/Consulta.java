package br.udesc.mca.modelo.consulta;

import java.io.Serializable;
import java.sql.Date;
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

import com.vividsolutions.jts.geom.Polygon;

import br.udesc.mca.modelo.tipo.StringArrayUserType;
import br.udesc.mca.modelo.trajetoria.Trajetoria;

@Entity
@Table(name = "consulta")
@TypeDefs({ @TypeDef(name = "StringArrayObject", typeClass = StringArrayUserType.class) })
public class Consulta implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "gen_consulta", sequenceName = "seq_consultaid")
	@GeneratedValue(generator = "gen_consulta")
	@Column(name = "id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "trajetoria_id", foreignKey = @ForeignKey(name = "consulta_trajetoria_id_fk") )
	private Trajetoria trajetoria;

	@Column(name = "data_consulta")
	private Date dataConsulta;

	@Column(name = "data_pesquisa_inicio")
	private Date dataPesquisaInicio;

	@Column(name = "data_pesquisa_fim")
	private Date dataPesquisaFim;

	@Column(columnDefinition = "geometry(Polygon,4326)")
	@Type(type = "org.hibernate.spatial.GeometryType")
	private Polygon poligono;

	@Column(name = "poligono_inverso", columnDefinition = "geometry(Polygon,4326)")
	@Type(type = "org.hibernate.spatial.GeometryType")
	private Polygon poligonoInverso;

	@Column(name = "diferenca_azimute", columnDefinition = "varchar(100000)[]")
	@Type(type = "StringArrayObject")
	private String[] trajetoriaDiferencaAzimute;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Trajetoria getTrajetoria() {
		return trajetoria;
	}

	public void setTrajetoria(Trajetoria trajetoria) {
		this.trajetoria = trajetoria;
	}

	public Date getDataConsulta() {
		return dataConsulta;
	}

	public void setDataConsulta(Date dataConsulta) {
		this.dataConsulta = dataConsulta;
	}

	public Date getDataPesquisaInicio() {
		return dataPesquisaInicio;
	}

	public void setDataPesquisaInicio(Date dataPesquisaInicio) {
		this.dataPesquisaInicio = dataPesquisaInicio;
	}

	public Date getDataPesquisaFim() {
		return dataPesquisaFim;
	}

	public void setDataPesquisaFim(Date dataPesquisaFim) {
		this.dataPesquisaFim = dataPesquisaFim;
	}

	public Polygon getPoligono() {
		return poligono;
	}

	public void setPoligono(Polygon poligono) {
		this.poligono = poligono;
	}

	public Polygon getPoligonoInverso() {
		return poligonoInverso;
	}

	public void setPoligonoInverso(Polygon poligonoInverso) {
		this.poligonoInverso = poligonoInverso;
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
		result = prime * result + ((dataConsulta == null) ? 0 : dataConsulta.hashCode());
		result = prime * result + ((dataPesquisaFim == null) ? 0 : dataPesquisaFim.hashCode());
		result = prime * result + ((dataPesquisaInicio == null) ? 0 : dataPesquisaInicio.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((poligono == null) ? 0 : poligono.hashCode());
		result = prime * result + ((poligonoInverso == null) ? 0 : poligonoInverso.hashCode());
		result = prime * result + ((trajetoria == null) ? 0 : trajetoria.hashCode());
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
		Consulta other = (Consulta) obj;
		if (dataConsulta == null) {
			if (other.dataConsulta != null)
				return false;
		} else if (!dataConsulta.equals(other.dataConsulta))
			return false;
		if (dataPesquisaFim == null) {
			if (other.dataPesquisaFim != null)
				return false;
		} else if (!dataPesquisaFim.equals(other.dataPesquisaFim))
			return false;
		if (dataPesquisaInicio == null) {
			if (other.dataPesquisaInicio != null)
				return false;
		} else if (!dataPesquisaInicio.equals(other.dataPesquisaInicio))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (poligono == null) {
			if (other.poligono != null)
				return false;
		} else if (!poligono.equals(other.poligono))
			return false;
		if (poligonoInverso == null) {
			if (other.poligonoInverso != null)
				return false;
		} else if (!poligonoInverso.equals(other.poligonoInverso))
			return false;
		if (trajetoria == null) {
			if (other.trajetoria != null)
				return false;
		} else if (!trajetoria.equals(other.trajetoria))
			return false;
		if (!Arrays.equals(trajetoriaDiferencaAzimute, other.trajetoriaDiferencaAzimute))
			return false;
		return true;
	}
}