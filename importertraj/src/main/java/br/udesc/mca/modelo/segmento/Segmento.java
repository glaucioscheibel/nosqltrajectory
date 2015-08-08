package br.udesc.mca.modelo.segmento;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.udesc.mca.modelo.pontosegmento.PontoSegmento;

@Entity
@Table(name = "segmento")
public class Segmento implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Integer id;

	private Double azimute;

	@Column(name = "diferenca_azimute")
	private Double diferencaAzimute;

	@OneToMany
	@JoinColumn(name = "segmento_id")
	private List<PontoSegmento> pontoSegmentoLista;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getAzimute() {
		return azimute;
	}

	public void setAzimute(Double azimute) {
		this.azimute = azimute;
	}

	public Double getDiferencaAzimute() {
		return diferencaAzimute;
	}

	public void setDiferencaAzimute(Double diferencaAzimute) {
		this.diferencaAzimute = diferencaAzimute;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((azimute == null) ? 0 : azimute.hashCode());
		result = prime * result + ((diferencaAzimute == null) ? 0 : diferencaAzimute.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Segmento other = (Segmento) obj;
		if (azimute == null) {
			if (other.azimute != null)
				return false;
		} else if (!azimute.equals(other.azimute))
			return false;
		if (diferencaAzimute == null) {
			if (other.diferencaAzimute != null)
				return false;
		} else if (!diferencaAzimute.equals(other.diferencaAzimute))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}