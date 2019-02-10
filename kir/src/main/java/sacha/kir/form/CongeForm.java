package sacha.kir.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CongeForm {
	@NotNull
	@Size(min=1, max=42)
	private String username;
	@NotNull
	@Size(min=1, max=42)
	private String dateDebut;
	@NotNull
	@Size(min=1, max=42)
	private String dateFin;
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDateDebut() {
		return dateDebut;
	}
	public void setDatedebut(String dateDebut) {
		this.dateDebut = dateDebut;
	}
	public String getDateFin() {
		return dateFin;
	}
	public void setDateFin(String dateFin) {
		this.dateFin = dateFin;
	}
}
