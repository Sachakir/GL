package root.bdd.utilisateur;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users")
public class Utilisateur {
    
    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private Long UID;
    
    @NotNull
    @Size(min=1, max=42)
    private String Nom;
    
    @NotNull
    private String Prenom;
    
    @NotNull
    private String Numerotel;
    
    @NotNull
	private float Jourscongesrestants;
    
    @NotNull
    private float rtt;
    
    @NotNull
    private long HeuresContrat;
   

	public Utilisateur() {}

	public Utilisateur(Long uID, String nom, String prenom, String numeroTel,
			long joursCongesRestants,float rTT,long Heurescontrat) 
	{
		UID = uID;
		Nom = nom;
		Prenom = prenom;
		Numerotel = numeroTel;
		Jourscongesrestants = joursCongesRestants;
		rtt = rTT;
		HeuresContrat = Heurescontrat;
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

	public float getJoursCongesRestants() {
		return Jourscongesrestants;
	}

	public void setJoursCongesRestants(float joursCongesRestants) {
		Jourscongesrestants = joursCongesRestants;
	}
	 
    public float getRtt() {
		return rtt;
	}

	public void setRtt(float rtt) {
		this.rtt = rtt;
	}

	public long getHeuresContrat() {
		return HeuresContrat;
	}

	public void setHeuresContrat(long heuresContrat) {
		HeuresContrat = heuresContrat;
	}

	@Override
    public String toString() {
        return Prenom + " " + Nom;
    }
    
    
}