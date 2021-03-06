package root.forms.utilisateur;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserForm {
	private Long uid;
	
	@NotNull
	@Size(min=1, max=42, message="Le nom doit faire entre 1 et 42 caractères")
	private String nom;
	
	@NotNull
	@Size(min=1, max=42, message="Le prénom doit faire entre 1 et 42 caractères")
	private String prenom;
	
	@NotNull
	@Pattern(regexp="^(?:(?:\\+|00)33|0)\\s*[1-9](?:[\\s.-]*\\d{2}){4}$", message="Numéro de téléphone entré non valide")
	private String numTel;
	
	@NotNull
	@Min(value = 0, message = "0 jours de conges minimum")
	private float joursCongesRest;
	
	@NotNull
	@Size(min=5, message="Le mot de passe doit être composé de 5 caractères minimum")
	@Size(max=42, message="Le mot de passe ne peut pas faire plus de 42 caractères")
	private String mdp;
	
	@NotNull
	@Min(value=1)
	private Long role_id;
	
	@NotNull
	@Min(value=1)
	private Long service_id;
	
	private boolean isAdmin;
	
	@NotNull
	@Min(value=0, message="0 jours de RTT minimum")
	private float rtt;
	
	@NotNull
	@Pattern(regexp="([1-9][0-9]+)|([0-9])", message="Nombre heures entrées non valide")
	private String heurestravail;
	
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
	
	public float getJoursCongesRest() {
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
	public float getRtt() {
		return rtt;
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
	
	public void setJoursCongesRest(float joursCongesRest) {
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
	public void setRtt(float n) {
		this.rtt=n;
	}

	public String getHeurestravail() {
		return heurestravail;
	}

	public void setHeurestravail(String heurestravail) {
		this.heurestravail = heurestravail;
	}
}
