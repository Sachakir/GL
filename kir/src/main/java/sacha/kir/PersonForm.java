package sacha.kir;
/*
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users")
public class PersonForm {
	@Id
    @NotNull
    @Size(min=2, max=30)
    private String name;

    @NotNull
    @Min(18)
    private Integer age;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String toString() {
        return "Person(Name: " + this.name + ", Age: " + this.age + ")";
    }
}
*/

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users")
public class PersonForm {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //@NotNull
    private Long UID;
    @NotNull
    @Size(min=2, max=30)
    private String Nom;
    @NotNull
    private String Prenom;
    //@NotNull
    private String Numerotel;
    //@NotNull
    private String datenaissance;
    //@NotNull
    private long Jourscongesrestants;
    
    public PersonForm() {}

	public PersonForm(Long uID, String nom, String prenom, String numeroTel, String dateNaissance,
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
