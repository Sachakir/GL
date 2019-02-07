package sacha.kir.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserForm {
	@NotNull
	@Size(min=1, max=42)
	private String nom;
	@NotNull
	@Size(min=1, max=42)
	private String prenom;
	@NotNull
	@Size(min=1, max=42)
	private String numTel;
	@NotNull
	@Size(min=1, max=42)
	private String dateNaissance;
	@NotNull
	@Min(value = 0, message = "0 jours de conges minimum")
	private int joursCongesRest;
	@NotNull
	@Size(min=1, max=42)
	private String mdp;
	@NotNull
	@Size(min=1, max=42)
	private String role;
	
	private long uid;
	
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getNumTel() {
		return numTel;
	}
	public void setNumTel(String numTel) {
		this.numTel = numTel;
	}
	public String getDateNaissance() {
		return dateNaissance;
	}
	public void setDateNaissance(String dateNaissance) {
		this.dateNaissance = dateNaissance;
	}
	public int getJoursCongesRest() {
		return joursCongesRest;
	}
	public void setJoursCongesRest(int joursCongesRest) {
		this.joursCongesRest = joursCongesRest;
	}
	public String getMdp() {
		return mdp;
	}
	public void setMdp(String mdp) {
		this.mdp = mdp;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	
	
}
