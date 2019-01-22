package sacha.kir.bdd;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class Utilisateur {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long UID;

    private String Nom;
    private String Prenom;
    private String Numerotel;
    private String datenaissance;
    private long Jourscongesrestants;
    
    public Utilisateur() {}

	public Utilisateur(Long uID, String nom, String prenom, String numeroTel, String dateNaissance,
			long joursCongesRestants) 
	{
		UID = uID;
		Nom = nom;
		Prenom = prenom;
		Numerotel = numeroTel;
		datenaissance = dateNaissance;
		Jourscongesrestants = joursCongesRestants;
	}

	public Long getUID() {
		return UID;
	}

	public void setUID(Long uID) {
		UID = uID;
	}

	public String getNom() {
		return Nom;
	}

	public void setNom(String nom) {
		Nom = nom;
	}

	public String getPrenom() {
		return Prenom;
	}

	public void setPrenom(String prenom) {
		Prenom = prenom;
	}

	public String getNumeroTel() {
		return Numerotel;
	}

	public void setNumeroTel(String numeroTel) {
		Numerotel = numeroTel;
	}

	public String getDateNaissance() {
		return datenaissance;
	}

	public void setDateNaissance(String dateNaissance) {
		datenaissance = dateNaissance;
	}

	public long getJoursCongesRestants() {
		return Jourscongesrestants;
	}

	public void setJoursCongesRestants(long joursCongesRestants) {
		Jourscongesrestants = joursCongesRestants;
	}
	
	@Override
    public String toString() {
        return Prenom + " " + Nom;
    }
    
    
}