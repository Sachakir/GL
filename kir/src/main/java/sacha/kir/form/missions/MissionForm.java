package sacha.kir.form.missions;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class MissionForm {
		
	@NotNull
	@Size(min=1, message="Le titre de la mission ne doit pas être vide")
	@Size(max=60, message="Le titre de la mission doit faire moins de 60 caractères")
	private String titre;
	
	@NotNull
	private String description;
	
	@NotNull
	@Size(min=1, message="Champ obligatoire")
	private String date_debut;
	
	@NotNull
	@Size(min=1, message="Champ obligatoire")
	private String date_fin;
	
	@NotNull
	@Size(min=1, message="Choisissez un responsable de mission")
	private String responsable_id;
	
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

	public String getResponsable_id() {
		return responsable_id;
	}

	public void setResponsable_id(String responsable_id) {
		this.responsable_id = responsable_id;
	}
	
	@Override
    public String toString() {
        return date_debut + " " + date_fin + " " + description;
    }
		
}
