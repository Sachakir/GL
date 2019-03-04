package sacha.kir.bdd.mission;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "mission")
public class Mission
{
	@Id
	@Column(name="MissionID")
	private Long mission_id;
	
	@Column(name="Titre")
	@NotNull
	@Size(min=1, message="Titre de la mission vide")
	@Size(max=60, message="Titre de la mission de plus de 60 caractères")
	private String titre;
	
	@Column(name="Description")
	private String description;
	
	@Column(name="date_debut")
	@NotNull
	private LocalDate date_debut;
	
	@Column(name="date_fin")
	@NotNull
	private LocalDate date_fin;
	
	@Column(name="ResponsableID")
	private Long responsable_id;
	
	public Mission() {}

	public Mission(Long mission_id, String titre, String description, LocalDate date_debut, LocalDate date_fin, Long responsable_id)
	{
		this.mission_id = mission_id;
		this.titre = titre;
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
	
	public String getTitre() {
		return titre;
	}
	
	public void setTitre(String titre) {
		this.titre = titre;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getDate_debut() {
		return date_debut;
	}

	public void setDate_debut(LocalDate date_debut) {
		this.date_debut = date_debut;
	}

	public LocalDate getDate_fin() {
		return date_fin;
	}

	public void setDate_fin(LocalDate date_fin) {
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
