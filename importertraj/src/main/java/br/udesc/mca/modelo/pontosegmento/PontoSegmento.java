package br.udesc.mca.modelo.pontosegmento;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ponto_segmento")
public class PontoSegmento implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private PontoSegmentoId pontoSegmentoId;

	public PontoSegmentoId getPontoSegmentoId() {
		return pontoSegmentoId;
	}

	public void setPontoSegmentoId(PontoSegmentoId pontoSegmentoId) {
		this.pontoSegmentoId = pontoSegmentoId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pontoSegmentoId == null) ? 0 : pontoSegmentoId.hashCode());
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
		PontoSegmento other = (PontoSegmento) obj;
		if (pontoSegmentoId == null) {
			if (other.pontoSegmentoId != null)
				return false;
		} else if (!pontoSegmentoId.equals(other.pontoSegmentoId))
			return false;
		return true;
	}
}