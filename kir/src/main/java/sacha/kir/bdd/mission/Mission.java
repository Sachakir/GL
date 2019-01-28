package sacha.kir.bdd.mission;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mission")
public class Mission 
{
	@Id
	@Column(name="MissionID")
	private Long mission_id;
	
	@Column(name="Description")
	private String description;
	
	@Column(name="date_debut")
	private String date_debut;
	
	@Column(name="date_fin")
	private String date_fin;
	
	@Column(name="ResponsableID")
	private Long responsable_id;
	
	public Mission() {}

	public Mission(Long mission_id, String description, String date_debut, String date_fin, Long responsable_id)
	{
		this.mission_id = mission_id;
		this.description = description;
		this.date_debut = date_debut;
		this.date_fin = date_fin;
		this.responsable_id = responsable_id;
	}

	public Long getMission_id() {
		return mission_id;
	}

	public void setMission_id(Long mission_id) {
		this.mission_id = mission_id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDate_debut() {
		return date_debut;
	}

	public void setDate_debut(String date_debut) {
		this.date_debut = date_debut;
	}

	public String getDate_fin() {
		return date_fin;
	}

	public void setDate_fin(String date_fin) {
		this.date_fin = date_fin;
	}

	public Long getResponsable_id() {
		return responsable_id;
	}

	public void setResponsable_id(Long responsable_id) {
		this.responsable_id = responsable_id;
	}
	
	@Override
    public String toString() {
        return date_debut + " " + date_fin + " " + description;
    }
	 
}
