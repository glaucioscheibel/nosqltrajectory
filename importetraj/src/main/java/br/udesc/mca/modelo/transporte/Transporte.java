package br.udesc.mca.modelo.transporte;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "transporte")
@NamedQuery(name = "consultaTransporteDescricao", query = "from Transporte t where t.descricao = :descricao")
public class Transporte implements Serializable {

	private static final long serialVersionUID = 1L;

	public enum TipoTransporte {
		AIRPLANE, BIKE, BOAT, BUS, CAR, MOTORCYCLE, RUN, SUBWAY, TAXI, TRAIN, WALK, DRONE;
	}

	@Id
	@SequenceGenerator(name = "gen_transporte", sequenceName = "seq_transporteid")
	@GeneratedValue(generator = "gen_transporte")
	@Column(name = "id")
	private Long id;

	@Column(length = 50)
	private String descricao;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
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
		Transporte other = (Transporte) obj;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}