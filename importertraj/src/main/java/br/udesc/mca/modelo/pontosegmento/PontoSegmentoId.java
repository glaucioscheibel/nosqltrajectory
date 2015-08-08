package br.udesc.mca.modelo.pontosegmento;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.udesc.mca.modelo.ponto.Ponto;
import br.udesc.mca.modelo.segmento.Segmento;

@Embeddable
public class PontoSegmentoId implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ponto_id", foreignKey = @ForeignKey(name = "ponto_segmento_ponto_id_fk"))
	private Ponto ponto;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "segmento_id", foreignKey = @ForeignKey(name = "ponto_segmento_segmento_id_fk"))
	private Segmento segmento;

	public Ponto getPonto() {
		return ponto;
	}

	public void setPonto(Ponto ponto) {
		this.ponto = ponto;
	}

	public Segmento getSegmento() {
		return segmento;
	}

	public void setSegmento(Segmento segmento) {
		this.segmento = segmento;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ponto == null) ? 0 : ponto.hashCode());
		result = prime * result + ((segmento == null) ? 0 : segmento.hashCode());
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
		PontoSegmentoId other = (PontoSegmentoId) obj;
		if (ponto == null) {
			if (other.ponto != null)
				return false;
		} else if (!ponto.equals(other.ponto))
			return false;
		if (segmento == null) {
			if (other.segmento != null)
				return false;
		} else if (!segmento.equals(other.segmento))
			return false;
		return true;
	}

}
