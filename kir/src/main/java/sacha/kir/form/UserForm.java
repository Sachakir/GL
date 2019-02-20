package sacha.kir.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserForm {
	private Long uid;
	
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
	@Min(value=1)
	private Long role_id;
	
	@NotNull
	@Min(value=1)
	private Long service_id;
	
	private boolean isAdmin;
	
	public UserForm() {
		isAdmin = false;
		role_id = (long)0;
		service_id = (long)0;
	}
	
	// Getters
	public long getUid() {
		return uid;
	}
	
	public String getNom() {
		return nom;
	}
	
	public String getPrenom() {
		return prenom;
	}
	
	public String getNumTel() {
		return numTel;
	}
	
	public String getDateNaissance() {
		return dateNaissance;
	}
	
	public int getJoursCongesRest() {
		return joursCongesRest;
	}
	
	public String getMdp() {
		return mdp;
	}
	
	public long getRole_id() {
		return role_id;
	}
	
	public long getService_id() {
		return service_id;
	}
	
	public boolean getIsAdmin() {
		return isAdmin;
	}
	
	// Setters
	public void setUid(long uid) {
		this.uid = uid;
	}
	
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	
	public void setNumTel(String numTel) {
		this.numTel = numTel;
	}
	
	public void setDateNaissance(String dateNaissance) {
		this.dateNaissance = dateNaissance;
	}
	
	public void setJoursCongesRest(int joursCongesRest) {
		this.joursCongesRest = joursCongesRest;
	}
	
	public void setMdp(String mdp) {
		this.mdp = mdp;
	}
	
	public void setRole_id(Long role_id) {
		this.role_id = role_id;
	}
	
	public void setService_id(long service_id) {
		this.service_id = service_id;
	}
	
	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
}
