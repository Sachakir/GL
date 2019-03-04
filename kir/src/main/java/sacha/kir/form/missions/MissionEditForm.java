package sacha.kir.form.missions;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class MissionEditForm {
	
	@NotNull
	private String description;
	
	@NotNull
	@Size(min=1, message="Champ obligatoire")
	private String date_fin;
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDate_fin() {
		return date_fin;
	}

	public void setDate_fin(String date_fin) {
		this.date_fin = date_fin;
	}	
}
