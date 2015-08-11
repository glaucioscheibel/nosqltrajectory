package br.udesc.mca.modelo.ponto;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.udesc.mca.modelo.trajetoria.Trajetoria;

@Entity
@Table(name = "ponto")
public class Ponto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "gen_ponto", sequenceName = "seq_pontoid")
	@GeneratedValue(generator="gen_ponto")
	@Column(name = "id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "trajetoria_id", foreignKey = @ForeignKey(name = "ponto_trajetoria_id_fk") )
	private Trajetoria trajetoria;

	private Double latitude;

	private Double longitude;

	private Double altitude;

	private Double velocidade;
	
	private Double acuracia;
	
	private Double bearing;

	private Timestamp tempo;

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

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getAltitude() {
		return altitude;
	}

	public void setAltitude(Double altitude) {
		this.altitude = altitude;
	}

	public Double getVelocidade() {
		return velocidade;
	}

	public void setVelocidade(Double velocidade) {
		this.velocidade = velocidade;
	}

	public Double getAcuracia() {
		return acuracia;
	}

	public void setAcuracia(Double acuracia) {
		this.acuracia = acuracia;
	}

	public Double getBearing() {
		return bearing;
	}

	public void setBearing(Double bearing) {
		this.bearing = bearing;
	}

	public Timestamp getTempo() {
		return tempo;
	}

	public void setTempo(Timestamp tempo) {
		this.tempo = tempo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acuracia == null) ? 0 : acuracia.hashCode());
		result = prime * result + ((altitude == null) ? 0 : altitude.hashCode());
		result = prime * result + ((bearing == null) ? 0 : bearing.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((latitude == null) ? 0 : latitude.hashCode());
		result = prime * result + ((longitude == null) ? 0 : longitude.hashCode());
		result = prime * result + ((tempo == null) ? 0 : tempo.hashCode());
		result = prime * result + ((trajetoria == null) ? 0 : trajetoria.hashCode());
		result = prime * result + ((velocidade == null) ? 0 : velocidade.hashCode());
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
		Ponto other = (Ponto) obj;
		if (acuracia == null) {
			if (other.acuracia != null)
				return false;
		} else if (!acuracia.equals(other.acuracia))
			return false;
		if (altitude == null) {
			if (other.altitude != null)
				return false;
		} else if (!altitude.equals(other.altitude))
			return false;
		if (bearing == null) {
			if (other.bearing != null)
				return false;
		} else if (!bearing.equals(other.bearing))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (latitude == null) {
			if (other.latitude != null)
				return false;
		} else if (!latitude.equals(other.latitude))
			return false;
		if (longitude == null) {
			if (other.longitude != null)
				return false;
		} else if (!longitude.equals(other.longitude))
			return false;
		if (tempo == null) {
			if (other.tempo != null)
				return false;
		} else if (!tempo.equals(other.tempo))
			return false;
		if (trajetoria == null) {
			if (other.trajetoria != null)
				return false;
		} else if (!trajetoria.equals(other.trajetoria))
			return false;
		if (velocidade == null) {
			if (other.velocidade != null)
				return false;
		} else if (!velocidade.equals(other.velocidade))
			return false;
		return true;
	}
}