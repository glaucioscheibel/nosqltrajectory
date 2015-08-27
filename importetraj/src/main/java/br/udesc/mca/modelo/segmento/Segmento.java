package br.udesc.mca.modelo.segmento;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.udesc.mca.modelo.ponto.Ponto;

@Entity
@Table(name = "segmento")
@NamedQuery(name = "consultaSegmento", query = "from Segmento s where t.base = :base")
public class Segmento implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "gen_segmento", sequenceName = "seq_segmentoid")
	@GeneratedValue(generator = "gen_segmento")
	@Column(name = "id")
	private Long id;

	private Double azimute;

	@Column(name = "diferenca_azimute")
	private Double diferencaAzimute;

	// Está mapeado para gerar as chaves estrangeiras com nome correto, mas tem
	// um bug no hibernate que não faz que isso seja respeitado.
	// Somente na versão 5 terá correção, e talvez saia na 4.
	// https://hibernate.atlassian.net/browse/HHH-8862
	@ManyToMany
	@JoinTable(name = "ponto_segmento", foreignKey = @ForeignKey(name = "ponto_segmento_segmento_id_fk") , inverseForeignKey = @ForeignKey(name = "ponto_segmento_ponto_id_fk") , joinColumns = {
			@JoinColumn(name = "segmento_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "ponto_id", referencedColumnName = "id") })
	private Set<Ponto> ponto = new HashSet<Ponto>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public Set<Ponto> getPonto() {
		return ponto;
	}

	public void setPonto(Set<Ponto> ponto) {
		this.ponto = ponto;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((azimute == null) ? 0 : azimute.hashCode());
		result = prime * result + ((diferencaAzimute == null) ? 0 : diferencaAzimute.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((ponto == null) ? 0 : ponto.hashCode());
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
		if (ponto == null) {
			if (other.ponto != null)
				return false;
		} else if (!ponto.equals(other.ponto))
			return false;
		return true;
	}
}