package sacha.kir.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class RemboursementForm {

	@NotNull
	@Min(value=0, message="Choisissez une mission")
	private String mission;
	
	@NotNull
	@Size(min=1, max=60, message="Le titre doit être composé de 1 à 60 caractères")
	private String titre;
	
	@NotNull
	@Pattern(regexp = "^[1-9][0-9]*((,|.)[0-9]+)?$", message="Montant incorrect")
	private String montant;
	
	@NotNull
	private String date;
	
	@NotNull
	private String motif;
	
	@NotNull
	private String moisNote;
	
	
	//Getters
	public String getMission() {
		return mission;
	}
	
	public String getTitre() {
		return titre;
	}
	
	public String getMontant() {
		return montant;
	}
	
	public String getDate() {
		return date;
	}
	
	public String getMotif() {
		return motif;
	}
	
	public String getMoisNote() {
		return moisNote;
	}
	
	//Setters
	public void setMission(String mission) {
		this.mission = mission;
	}
	
	public void setTitre(String titre) {
		this.titre = titre;
	}
	
	public void setMontant(String montant) {
		this.montant = montant;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public void setMotif(String motif) {
		this.motif = motif;
	}
	
	public void setMoisNote(String moisNote) {
		this.moisNote = moisNote;
	}
	
}
