package sacha.kir.form;


import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RemboursementForm {

	@NotNull
	private String mission;
	@NotNull
	@Size(min=1, max=60)
	private String titre;
	@NotNull
	@Min(value=0)
	private BigDecimal montant;
	@NotNull
	@Size(min=1, max=42)
	private String date;
	@NotNull
	private String motif;
	
	
	//Getters
	public String getMission() {
		return mission;
	}
	
	public String getTitre() {
		return titre;
	}
	
	public BigDecimal getMontant() {
		return montant;
	}
	
	public String getDate() {
		return date;
	}
	
	public String getMotif() {
		return motif;
	}
	
	//Setters
	public void setMission(String mission) {
		this.mission = mission;
	}
	
	public void setTitre(String titre) {
		this.titre = titre;
	}
	
	public void setMontant(BigDecimal montant) {
		this.montant = montant;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public void setMotif(String motif) {
		this.motif = motif;
	}
	
}
