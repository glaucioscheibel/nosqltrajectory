package br.udesc.mca.trajectory.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Trajectory implements Serializable {
	private static final long serialVersionUID = 1L;
	@JsonProperty("_id")
	@Id
	private long id;
	private String description;

	private int userId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModified;
	@OneToMany(cascade = CascadeType.ALL)
	private List<TrajectoryVersion> versions;

	public Trajectory() {
	}

	public Trajectory(long id) {
		this.id = id;
	}

	public Trajectory(long id, String description) {
		this.id = id;
		this.description = description;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Date getLastModified() {
		return this.lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public List<TrajectoryVersion> getVersions() {
		return this.versions;
	}

	public void addVersion(TrajectoryVersion version) {
		if (this.versions == null) {
			this.versions = new ArrayList<>();
		}
		this.versions.add(version);
	}

	public void removeVersion(TrajectoryVersion version) {
		if (this.versions != null) {
			this.versions.remove(version);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result
				+ ((lastModified == null) ? 0 : lastModified.hashCode());
		result = prime * result + userId;
		result = prime * result
				+ ((versions == null) ? 0 : versions.hashCode());
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
		Trajectory other = (Trajectory) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id != other.id)
			return false;
		if (lastModified == null) {
			if (other.lastModified != null)
				return false;
		} else if (!lastModified.equals(other.lastModified))
			return false;
		if (userId != other.userId)
			return false;
		if (versions == null) {
			if (other.versions != null)
				return false;
		} else if (!versions.equals(other.versions))
			return false;
		return true;
	}
}